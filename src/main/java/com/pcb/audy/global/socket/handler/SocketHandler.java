package com.pcb.audy.global.socket.handler;

import com.pcb.audy.global.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketHandler implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String authorization = accessor.getFirstNativeHeader(JwtUtils.ACCESS_TOKEN_HEADER);
        String token = authorization.replace(JwtUtils.TOKEN_TYPE, "");

        if (accessor.getCommand().equals(StompCommand.CONNECT)) {
            if (jwtUtils.getEmail(token) == null) {
                // throw new GlobalException(INVALID_TOKEN);
                // socket Errorhandler 생성 예정
            }
        }

        return message;
    }
}
