package com.pcb.audy.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // 성공
    SUCCESS(0, "실행에 성공했습니다."),

    // 실패
    INTERNAL_SERVER_ERROR(1000, "알 수 없는 에러가 발생했습니다."),
    UNKNOWN_SOCIAL(1001, "알 수 없는 소셜입니다."),
    INVALID_TOKEN(1002, "유효하지 않은 토큰입니다."),

    NOT_FOUND_USER(2000, "유저를 찾을 수 없습니다."),

    NOT_FOUND_COURSE(3000, "코스를 찾을 수 없습니다."),
    NOT_ADMIN_COURSE(3001, "해당 코스에 변경 권한이 없습니다."),

    NOT_FOUND_PIN(4000, "핀을 찾을 수 없습니다."),

    NOT_FOUND_EDITOR(5000, "에디터를 찾을 수 없습니다."),
    VALID_KEY(5001, "올바르지 않은 키입니다."),
    ALREADY_EXIST_EDITOR(5002, "이미 코스에 포함된 멤버입니다.");

    private Integer code;
    private String message;
}
