package org.example.realtimechatting.domain;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class ChatRoom {
    private String id;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoom of(String id, String name) {
        ChatRoom room = new ChatRoom();
        room.id = id;
        room.name = name;
        return room;
    }

    public void addUserChatSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeUserChatSession(WebSocketSession session) {
        sessions.remove(session);
    }
}