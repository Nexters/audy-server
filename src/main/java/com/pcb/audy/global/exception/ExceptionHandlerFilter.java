package com.pcb.audy.global.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.response.BasicResponse;
import com.pcb.audy.global.response.ResultCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (GlobalException e) {
            errorResponse(response, e.getResultCode());
        }
    }

    private void errorResponse(HttpServletResponse response, ResultCode resultCode)
            throws IOException {
        response.setCharacterEncoding(UTF_8.name());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(SC_OK);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(BasicResponse.error(resultCode)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.getWriter().close();
        }
    }
}
