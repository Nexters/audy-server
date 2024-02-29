package com.pcb.audy.global.socket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.auth.socket.SocketPrincipal;
import com.pcb.audy.global.redis.RedisProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
@RequiredArgsConstructor
public class CustomChannelInterceptor implements ChannelInterceptor {
    private final RedisProvider redisProvider;
    private final ObjectMapper objectMapper;
    private final String SOCKET_PREFIX = "socket:";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (isConnectRequest(accessor)) {
            Map<String, Object> keys = accessor.getSessionAttributes();
            SocketPrincipal socketPrincipal =
                    objectMapper.convertValue(accessor.getUser(), SocketPrincipal.class);
            String courseId = objectMapper.convertValue(keys.get("courseId"), String.class);
            redisProvider.setValues(getKey(courseId), socketPrincipal.getUser().getUserId(), Integer.MAX_VALUE);
        }

        if (isDisconnectRequest(accessor)) {
            Map<String, Object> keys = accessor.getSessionAttributes();
            SocketPrincipal socketPrincipal =
                    objectMapper.convertValue(accessor.getUser(), SocketPrincipal.class);
            String courseId = objectMapper.convertValue(keys.get("courseId"), String.class);
            redisProvider.deleteValue(getKey(courseId), socketPrincipal.getUser().getUserId());
        }

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {}

    private String getKey(String courseId) {
        return SOCKET_PREFIX + courseId;
    }

    private boolean isConnectRequest(StompHeaderAccessor accessor) {
        return accessor != null && StompCommand.CONNECT.equals(accessor.getCommand());
    }

    private boolean isDisconnectRequest(StompHeaderAccessor accessor) {
        return accessor != null && StompCommand.DISCONNECT.equals(accessor.getCommand());
    }
}
