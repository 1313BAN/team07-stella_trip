package com.ssafy.stella_trip.chat.interceptor;

import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            // CONNECT와 SEND 모두 처리
            if (StompCommand.CONNECT.equals(accessor.getCommand()) ||
                StompCommand.SEND.equals(accessor.getCommand())) {
                String token = accessor.getFirstNativeHeader("Authorization");

                // CONNECT에서는 헤더에서 토큰을 가져옵니다
                if (token != null && token.startsWith("Bearer ")) {
                    try {
                        token = token.substring(7);
                        setAuthenticationFromToken(accessor, token);
                    } catch (Exception e) {
                        // 인증 실패 시 로그만 남기고 연결은 허용
                        log.error("WebSocket 인증 실패: " + e.getMessage());
                    }
                }
            }
        }

        return message;
    }

    private void setAuthenticationFromToken(StompHeaderAccessor accessor, String token) {
        if (jwtProvider.validateToken(token)) {
            int userId = jwtProvider.getUserIdFromToken(token);
            String name = jwtProvider.getUserNameFromToken(token);
            String email = jwtProvider.getEmailFromToken(token);
            List<String> roles = jwtProvider.getRolesFromToken(token);
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new).toList();

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            new JwtUserInfo(userId, name, email),
                            null,
                            authorities);
            accessor.setUser(auth);
        }
    }
}
