package com.pcb.audy.global.socket.config;

import com.pcb.audy.global.socket.handler.CustomHandshakeInterceptor;
import com.pcb.audy.global.socket.handler.SocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SocketHandler socketHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/course")
                .setAllowedOriginPatterns("*")
                .addInterceptors(customHandshakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 클라이언트에게 메시지 전달
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트에서 보낸 메시지 받기
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(socketHandler);
    }

    @Bean
    public HandshakeInterceptor customHandshakeInterceptor() {
        return new CustomHandshakeInterceptor();
    }
}
