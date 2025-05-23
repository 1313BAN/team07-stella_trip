package com.ssafy.stella_trip.chat.controller;

import com.ssafy.stella_trip.chat.dto.ChatMessageDTO;
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

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatDAO chatDAO;

    @MessageMapping("/chat/message")
    public void handleMessage(Message<ChatMessageDTO> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Principal userPrincipal = accessor.getUser(); // ✅ 여기서 Principal 추출
        String sender = (userPrincipal != null) ? userPrincipal.getName() : "anonymous";

        ChatMessageDTO payload = message.getPayload();
        payload.setUsername(sender);
        log.info("sender: {}, roomId: {}, message: {}", sender, payload.getRoomId(), payload.getMessage());

        chatDAO.addChat(payload.getUsername(), payload.getRoomId(), payload.getMessage());
        messagingTemplate.convertAndSend("/sub/chat/room/" + payload.getRoomId(), payload);
    }

}
