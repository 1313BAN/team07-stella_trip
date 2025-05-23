package com.ssafy.stella_trip.chat.service;

import com.ssafy.stella_trip.chat.dto.ChatDTO;
import com.ssafy.stella_trip.chat.dto.ChatMessageDTO;
import com.ssafy.stella_trip.dao.chat.ChatDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatDAO chatDAO;
    private final SimpMessagingTemplate messagingTemplate;

    public void handleMessage(Message<ChatMessageDTO> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Principal userPrincipal = accessor.getUser(); // ✅ 여기서 Principal 추출
        String sender = (userPrincipal != null) ? userPrincipal.getName() : "anonymous";

        ChatMessageDTO payload = message.getPayload();
        // 입력 검증 추가
        if (payload.getRoomId() <= 0 || payload.getMessage() == null || payload.getMessage().trim().isEmpty()) {
            log.warn("Invalid message payload: {}", payload);
            return;
        }
        payload.setUsername(sender);
        log.info("sender: {}, roomId: {}, message: {}", sender, payload.getRoomId(), payload.getMessage());
        try{
            ChatDTO chatDTO = new ChatDTO(0, payload.getRoomId(), sender, payload.getMessage(), null);
            chatDAO.addChat(chatDTO);
            payload.setChatId(chatDTO.getChatId());
            messagingTemplate.convertAndSend("/sub/chat/room/" + payload.getRoomId(), payload);
        } catch (Exception e) {
            log.error("Error while sending message: {}", e.getMessage());
            // Handle the error accordingly
        }
    }
}
