package com.pcb.audy.domain.socket.handler;

import static com.pcb.audy.global.response.ResultCode.INVALID_TOKEN;

import com.pcb.audy.global.exception.GlobalException;
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

        String authorization = accessor.getFirstNativeHeader("Authorization");
        String token = authorization.replace("Bearer ", "");

        if (accessor.getCommand() == StompCommand.CONNECT) {
            if (jwtUtils.getEmail(token) == null) {
                throw new GlobalException(INVALID_TOKEN);
            }
        }

        return message;
    }
}
