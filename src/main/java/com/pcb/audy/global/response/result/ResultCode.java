package com.pcb.audy.global.response.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(HttpStatus.OK,"S001","실행에 성공했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
