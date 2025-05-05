package org.example.realtimechatting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.realtimechatting.model.ChatRoom;
import org.example.realtimechatting.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Slf4j
public class ChatRoomViewController {
    private final ChatService chatService;

    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public ModelAndView rooms(){
        log.info("# All Chat Rooms");
        ModelAndView mv = new ModelAndView("chat/rooms");
        mv.addObject("list", chatService.findAllRoom());

        return mv;
    }

    //채팅방 개설
    @PostMapping(value = "/room")
    public String create(@RequestParam String name, RedirectAttributes rttr){
        log.info("# Create Chat Room , name: " + name);

        ChatRoom chatRoom = chatService.createRoom(name);

        rttr.addFlashAttribute("roomName", chatRoom.getName());

        return "redirect:/chat/rooms";
    }

    //채팅방 조회
    @GetMapping("/room")
    public void getRoom(String roomId, Model model, @RequestParam String userName){
        log.info("# get Chat Room, roomID : " + roomId);

        model.addAttribute("room", chatService.findRoomById(roomId));
        model.addAttribute("userName", userName);
    }
}
