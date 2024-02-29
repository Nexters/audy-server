package com.pcb.audy.global.socket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.auth.socket.SocketPrincipal;
import com.pcb.audy.global.validator.UserValidator;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Slf4j
@RequiredArgsConstructor
public class CustomPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected Principal determineUser(
            ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        User user =
                userRepository.findByEmail(
                        objectMapper.convertValue(attributes.get("email"), String.class));
        UserValidator.validate(user);
        return SocketPrincipal.builder().user(user).build();
    }
}
