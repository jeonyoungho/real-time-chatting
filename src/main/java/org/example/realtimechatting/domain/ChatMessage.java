package org.example.realtimechatting.domain;

import lombok.Getter;

@Getter
public class ChatMessage {
    private String roomId;
    private String userId;
    private String payload;

    public static ChatMessage of(String roomId, String userId, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.roomId = roomId;
        chatMessage.userId = userId;
        chatMessage.payload = message;
        return chatMessage;
    }
}
