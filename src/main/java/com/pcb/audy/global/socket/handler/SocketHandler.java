package com.pcb.audy.global.socket.handler;

import static com.pcb.audy.global.jwt.JwtUtils.ACCESS_TOKEN_NAME;
import static com.pcb.audy.global.jwt.JwtUtils.REFRESH_TOKEN_NAME;
import static com.pcb.audy.global.jwt.JwtUtils.TOKEN_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.jwt.JwtUtils;
import com.pcb.audy.global.validator.TokenValidator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler implements ChannelInterceptor {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (!CollectionUtils.isEmpty(sessionAttributes)) {
                String accessCookie =
                        objectMapper.convertValue(sessionAttributes.get(ACCESS_TOKEN_NAME), String.class);
                log.info("accessCookie in socket: " + accessCookie);
                TokenValidator.validate(accessCookie);

                String accessToken = accessCookie.replace(TOKEN_TYPE, "");
                if (jwtUtils.getEmail(accessToken) == null) {
                    String refreshCookie =
                            objectMapper.convertValue(sessionAttributes.get(REFRESH_TOKEN_NAME), String.class);
                    log.info("refreshCookie in socket: " + refreshCookie);
                    TokenValidator.validate(refreshCookie);

                    String refreshToken = refreshCookie.replace(TOKEN_TYPE, "");
                    String email = jwtUtils.getEmail(refreshToken);
                    TokenValidator.validateEmail(email);
                }
            }
        }

        return message;
    }
}
