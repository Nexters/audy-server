package com.pcb.audy.global.exception;

import com.pcb.audy.global.response.BasicResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public BasicResponse<Void> handleException(GlobalException e) {
        return BasicResponse.error(e.getResultCode());
    }
}
