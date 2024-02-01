package com.pcb.audy.global.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.response.BasicResponse;
import com.pcb.audy.global.response.ResultCode;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandler {

    public static void setErrorResponse(HttpServletResponse response, ResultCode resultCode) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(SC_OK);
        response.setCharacterEncoding(UTF_8.name());
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(BasicResponse.error(resultCode)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
