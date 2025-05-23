package com.ssafy.stella_trip.chat.config;

import com.ssafy.stella_trip.chat.interceptor.JwtChannelInterceptor;
import com.ssafy.stella_trip.chat.interceptor.RoomAvailableInterceptor;
import com.ssafy.stella_trip.dao.chat.ChatDAO;
import com.ssafy.stella_trip.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenProvider jwtProvider;
    private final ChatDAO chatDAO;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/v1/websocket")
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 메시지 구독용
        registry.setApplicationDestinationPrefixes("/pub"); // 메시지 발행용
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new JwtChannelInterceptor(jwtProvider), new RoomAvailableInterceptor(chatDAO));
    }

}
