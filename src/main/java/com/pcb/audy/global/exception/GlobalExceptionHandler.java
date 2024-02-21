package com.pcb.audy.global.exception;

import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @ExceptionHandler(GlobalException.class)
    public BasicResponse<Void> handleException(GlobalException e) {
        return BasicResponse.error(e.getResultCode());
    }

    @MessageExceptionHandler(GlobalException.class)
    public void handleSocketException(GlobalException e) {
        simpMessagingTemplate.convertAndSend("/sub/error", BasicResponse.error(e.getResultCode()));
    }
}
