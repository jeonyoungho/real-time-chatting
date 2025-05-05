package org.example.realtimechatting.service;

import com.google.common.collect.Lists;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.realtimechatting.dto.ChatMessageDto;
import org.example.realtimechatting.model.ChatRoom;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private static final String CHAT_ROOM_USERS_KEY_FORMAT = "chat:room:%s:users";

    private static final Map<String, ChatRoom> chatRoomStore = new LinkedHashMap<>();

    private final RedisTemplate<String, Object> redisTemplate;

    public List<ChatRoom> findAllRoom() {
        if (chatRoomStore.isEmpty()) {
            return Lists.newArrayList();
        }

        return Lists.newArrayList(chatRoomStore.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRoomStore.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.of(randomId, name);
        chatRoomStore.put(randomId, chatRoom);

        return chatRoom;
    }

    public void enterRoom(ChatMessageDto dto) {
        String chatRoomUsersKey = String.format(CHAT_ROOM_USERS_KEY_FORMAT, dto.getRoomId());
        redisTemplate.opsForSet().add(chatRoomUsersKey, dto.getWriter());

        dto.setMessage(dto.getWriter() + "님이 채팅방에 참여하였습니다.");
        sendMessage(dto);
    }

    public void leaveRoom(ChatMessageDto dto) {
        String chatRoomUsersKey = String.format(CHAT_ROOM_USERS_KEY_FORMAT, dto.getRoomId());
        redisTemplate.opsForSet().remove(chatRoomUsersKey, dto.getWriter());

        dto.setMessage(dto.getWriter() + "님이 채팅방을 나갔습니다.");
        sendMessage(dto);
    }

    public void sendMessage(ChatMessageDto dto) {
        redisTemplate.convertAndSend(getChatRoomTopic(dto.getRoomId()), dto);
    }


    private String getChatRoomTopic(String roomId) {
        return "chat:room:" + roomId;
    }
}
