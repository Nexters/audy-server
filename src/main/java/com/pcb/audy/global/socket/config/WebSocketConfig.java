package com.pcb.audy.global.socket.config;

import com.pcb.audy.global.socket.handler.CustomHandshakeInterceptor;
import com.pcb.audy.global.socket.handler.SocketErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SocketErrorHandler socketErrorHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/course/{courseId}")
                .setAllowedOriginPatterns("*")
                .addInterceptors(customHandshakeInterceptor())
                .withSockJS();
        registry.setErrorHandler(socketErrorHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 클라이언트에게 메시지 전달
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트에서 보낸 메시지 받기
    }

    @Bean
    public HandshakeInterceptor customHandshakeInterceptor() {
        return new CustomHandshakeInterceptor();
    }
}
