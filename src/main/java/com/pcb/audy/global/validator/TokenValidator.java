package com.pcb.audy.global.validator;

import static com.pcb.audy.global.jwt.JwtUtils.TOKEN_TYPE;
import static com.pcb.audy.global.response.ResultCode.INVALID_TOKEN;

import com.pcb.audy.global.exception.GlobalException;

public class TokenValidator {
    public static void validate(String cookie) {
        if (!isExistCookie(cookie)) {
            throw new GlobalException(INVALID_TOKEN);
        }
    }

    public static void validateEmail(String email) {
        if (!isExistEmail(email)) {
            throw new GlobalException(INVALID_TOKEN);
        }
    }

    private static boolean isExistEmail(String email) {
        return email != null;
    }

    private static boolean isExistCookie(String cookie) {
        return cookie != null && cookie.startsWith(TOKEN_TYPE);
    }
}
