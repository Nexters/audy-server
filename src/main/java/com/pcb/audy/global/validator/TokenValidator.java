package com.pcb.audy.global.validator;

import static com.pcb.audy.global.jwt.JwtUtils.TOKEN_TYPE;
import static com.pcb.audy.global.response.ResultCode.INVALID_TOKEN;

import com.pcb.audy.global.exception.GlobalException;
import jakarta.servlet.http.Cookie;

public class TokenValidator {
    public static void validate(String cookie) {
        if (!isValidCookie(cookie)) {
            throw new GlobalException(INVALID_TOKEN);
        }
    }

    public static void validate(Cookie cookie) {
        if (!isValidCookie(cookie)) {
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

    private static boolean isValidCookie(Cookie cookie) {
        return cookie != null && cookie.getValue().startsWith(TOKEN_TYPE);
    }

    private static boolean isValidCookie(String cookie) {
        return cookie != null && cookie.startsWith(TOKEN_TYPE);
    }
}
