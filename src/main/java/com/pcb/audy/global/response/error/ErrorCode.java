package com.pcb.audy.global.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "알 수 없는 에러가 발생했습니다.");

    final private HttpStatus status;
    final private String errorCode;
    final private String message;
}
