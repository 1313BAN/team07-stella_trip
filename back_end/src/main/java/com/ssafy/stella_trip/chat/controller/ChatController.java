package com.ssafy.stella_trip.chat.controller;

import com.ssafy.stella_trip.chat.dto.ChatMessageDTO;
import com.ssafy.stella_trip.chat.service.ChatService;
import com.ssafy.stella_trip.dao.chat.ChatDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void handleMessage(Message<ChatMessageDTO> message) {
        chatService.handleMessage(message);
    }
}
