package org.example.realtimechatting.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Setter
@Getter
public class ChatRoom {
    private String id;
    private String name;
    private Set<WebSocketSession> sessions;

    public static ChatRoom of(String id, String name) {
        ChatRoom room = new ChatRoom();
        room.id = id;
        room.name = name;
        room.sessions = new HashSet<>();
        return room;
    }

    public void addUserChatSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeUserChatSession(WebSocketSession session) {
        sessions.remove(session);
    }
}