package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_USER;

import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.global.exception.GlobalException;

public class UserValidator {
    public static void validate(User user) {
        if (!isExistUser(user)) {
            throw new GlobalException(NOT_FOUND_USER);
        }
    }

    private static boolean isExistUser(User user) {
        return user != null;
    }
}
