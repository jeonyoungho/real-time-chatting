package org.example.realtimechatting.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.realtimechatting.domain.ChatRoom;
import org.example.realtimechatting.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
public class ChatController {

    private final ChatService chatService;

    @RequestMapping("/chatList")
    public String chatList(Model model){
        List<ChatRoom> roomList = chatService.findAllRoom();

        model.addAttribute("roomList",roomList);

        return "chat/chatList";
    }

    @PostMapping("createRoom")
    public String createRoom(Model model, @RequestParam String roomName, @RequestParam String userName) {
        ChatRoom room = chatService.createRoom(roomName);

        model.addAttribute("roomId", room.getId());
        model.addAttribute("userName", userName);

        return "chat/chatRoom";
    }

    @GetMapping("/chatRoom")
    public String chatRoom(Model model, @RequestParam String roomId, @RequestParam String userName){
        ChatRoom room = chatService.findRoomById(roomId);

        model.addAttribute("roomId", room.getId());
        model.addAttribute("userName", userName);

        return "chat/chatRoom";
    }
}
