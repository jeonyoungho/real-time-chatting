package org.example.realtimechatting.stomp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.realtimechatting.dto.ChatMessageDto;
import org.example.realtimechatting.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final ChatService chatService;

    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDto dto){
        log.info("[CHAT][ENTER] dto => {}", dto);
        chatService.enterRoom(dto);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto dto){
        log.info("[CHAT][SEND] dto => {}", dto);
        chatService.sendMessage(dto);
    }

    @MessageMapping(value = "/chat/leave")
    public void leave(ChatMessageDto dto){
        log.info("[CHAT][LEAVE] dto => {}", dto);
        chatService.leaveRoom(dto);
    }
}
