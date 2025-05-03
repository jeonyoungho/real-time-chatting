package org.example.realtimechatting.service;

import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.realtimechatting.domain.ChatRoom;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public void enterRoom(String roomId, WebSocketSession session) {
        ChatRoom chatRoom = findRoomById(roomId);
        chatRoom.addUserChatSession(session);
    }

    public List<ChatRoom> findAllRoom() {
        return Lists.newArrayList(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();

        ChatRoom chatRoom = ChatRoom.of(randomId, name);
        chatRooms.put(randomId, chatRoom);

        return chatRoom;
    }

    public void remove(String roomId, WebSocketSession session) {
        ChatRoom chatRoom = chatRooms.get(roomId);
        chatRoom.removeUserChatSession(session);
    }
}
