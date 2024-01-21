package com.pcb.audy.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // 성공
    SUCCESS(0, "실행에 성공했습니다."),

    // 실패
    INTERNAL_SERVER_ERROR(1000, "알 수 없는 에러가 발생했습니다.");

    private Integer code;
    private String message;
}
