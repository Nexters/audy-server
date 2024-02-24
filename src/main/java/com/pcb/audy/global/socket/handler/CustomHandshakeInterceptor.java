package com.pcb.audy.global.socket.handler;

import static com.pcb.audy.global.jwt.JwtUtils.ACCESS_TOKEN_NAME;
import static com.pcb.audy.global.jwt.JwtUtils.REFRESH_TOKEN_NAME;
import static com.pcb.audy.global.jwt.JwtUtils.TOKEN_TYPE;

import com.pcb.audy.global.jwt.JwtUtils;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.TokenValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

@Slf4j
@RequiredArgsConstructor
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    private final RedisProvider redisProvider;
    private final JwtUtils jwtUtils;
    private final String SOCKET_PREFIX = "socket:";

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletServerRequest) {
            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
            String email = getEmail(servletRequest, ACCESS_TOKEN_NAME);
            if (email == null) {
                email = getEmail(servletRequest, REFRESH_TOKEN_NAME);
                TokenValidator.validateEmail(email);
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        if (request instanceof ServletServerHttpRequest servletServerRequest) {
            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
            if (servletRequest.getRequestURI() != null) {
                String email = getEmail(servletRequest, REFRESH_TOKEN_NAME);
                String courseId = servletRequest.getRequestURI().replace("/course/", "");
                redisProvider.setValues(SOCKET_PREFIX + courseId, email, Integer.MAX_VALUE);
            }
        }
    }

    private String getEmail(HttpServletRequest request, String tokenName) {
        Cookie cookie = WebUtils.getCookie(request, tokenName);
        TokenValidator.validate(cookie);

        String token = cookie.getValue().replace(TOKEN_TYPE, "");
        log.info(tokenName + " in socket: " + token);
        return jwtUtils.getEmail(token);
    }
}
