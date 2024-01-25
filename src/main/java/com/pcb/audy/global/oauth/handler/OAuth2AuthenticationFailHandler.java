package com.pcb.audy.global.oauth.handler;

import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_USER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.response.BasicResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2AuthenticationFailHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        settingResponse(response, BasicResponse.error(NOT_FOUND_USER));
    }

    private void settingResponse(HttpServletResponse response, BasicResponse<?> res)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String result = objectMapper.writeValueAsString(res);
        response.getWriter().write(result);
    }
}
