package com.pcb.audy.global.socket.handler;

import static com.pcb.audy.global.jwt.JwtUtils.ACCESS_TOKEN_NAME;
import static com.pcb.audy.global.jwt.JwtUtils.REFRESH_TOKEN_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletServerRequest) {
            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
            Cookie accessCookie = WebUtils.getCookie(servletRequest, ACCESS_TOKEN_NAME);
            if (accessCookie != null) {
                attributes.put(ACCESS_TOKEN_NAME, accessCookie.getValue());
            }

            Cookie refreshCookie = WebUtils.getCookie(servletRequest, REFRESH_TOKEN_NAME);
            if (refreshCookie != null) {
                attributes.put(REFRESH_TOKEN_NAME, refreshCookie.getValue());
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {}
}
