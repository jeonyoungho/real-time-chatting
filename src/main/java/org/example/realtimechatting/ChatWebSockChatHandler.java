package org.example.realtimechatting;

import io.micrometer.common.util.StringUtils;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.realtimechatting.domain.ChatRoom;
import org.example.realtimechatting.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatWebSockChatHandler extends TextWebSocketHandler {

    private static final String DISPLAY_MESSAGE_FORMAT = "%s: %s";

    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = getParameterFromQuery(session.getUri(), "roomId");
        String userName = getParameterFromQuery(session.getUri(), "userName");

        chatService.enterRoom(roomId, session);

        session.getAttributes().put("userName", userName);
        session.getAttributes().put("roomId", roomId);
    }

    private String getParameterFromQuery(URI uri, String paramName) {
        String query = uri.getQuery();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userName = (String) session.getAttributes().get("userName");
        String roomId = (String) session.getAttributes().get("roomId");
        String msgPayload = message.getPayload();

        ChatRoom chatRoom = chatService.findRoomById(roomId);
        String displayMessage = String.format(DISPLAY_MESSAGE_FORMAT, userName, msgPayload);
        sendToRoom(chatRoom, new TextMessage(displayMessage));
    }

    private void sendToRoom(ChatRoom chatRoom, TextMessage message){
        Set<WebSocketSession> sessions = chatRoom.getSessions();

        sessions.parallelStream().forEach( roomSession -> {
            try {
                roomSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            String roomId = (String) session.getAttributes().get("roomId");
            String userName = (String) session.getAttributes().get("userName");

            if (StringUtils.isNotBlank(roomId)) {
                chatService.remove(roomId, session);
            }

            log.info("[ChatWebSocketHandler] WebSocket connection closed: user={}, chatRoom={}, session={}, status={}",
                     userName, roomId, session.getId(), status);
        } catch (Exception e) {
            log.error("[ChatWebSocketHandler] Error occurred during connection closure", e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("[ChatWebSocketHandler] WebSocket Transport Error! sessionId={}", session.getId(), exception);

        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

}
