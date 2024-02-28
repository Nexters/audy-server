package com.pcb.audy.global.socket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.jwt.JwtUtils;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.socket.handler.CustomChannelInterceptor;
import com.pcb.audy.global.socket.handler.CustomHandshakeInterceptor;
import com.pcb.audy.global.socket.handler.CustomPrincipalHandshakeHandler;
import com.pcb.audy.global.socket.handler.SocketErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SocketErrorHandler socketErrorHandler;
    private final RedisProvider redisProvider;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/course/{courseId}")
                .setAllowedOriginPatterns("*")
                .addInterceptors(customHandshakeInterceptor())
                .setHandshakeHandler(customPrincipalHandshakeHandler())
                .withSockJS();
        registry.setErrorHandler(socketErrorHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 클라이언트에게 메시지 전달
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트에서 보낸 메시지 받기
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(customChannelInterceptor());
    }

    @Bean
    public HandshakeHandler customPrincipalHandshakeHandler() {
        return new CustomPrincipalHandshakeHandler(userRepository, objectMapper);
    }

    @Bean
    public ChannelInterceptor customChannelInterceptor() {
        return new CustomChannelInterceptor(redisProvider, objectMapper);
    }

    @Bean
    public HandshakeInterceptor customHandshakeInterceptor() {
        return new CustomHandshakeInterceptor(jwtUtils);
    }
}
