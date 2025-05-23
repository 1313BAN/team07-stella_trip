package com.ssafy.stella_trip.chat.interceptor;

import com.ssafy.stella_trip.dao.chat.ChatDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@RequiredArgsConstructor
public class RoomAvailableInterceptor implements ChannelInterceptor {

    private final ChatDAO chatDAO;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination != null && destination.startsWith("/sub/chat/room/")) {
                try {
                    int roomId = Integer.parseInt(destination.substring("/sub/chat/room/".length()));
                    if (!chatDAO.existsById(roomId)) {
                        throw new IllegalArgumentException("존재하지 않는 채팅방입니다: " + roomId);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("잘못된 채팅방 ID 형식입니다: " + destination);
                }

            }
        }
        return message;
    }

}
