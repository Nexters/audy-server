package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_PIN;

import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.global.exception.GlobalException;

public class PinValidator {
    public static void validate(PinRedisRes pinRedisRes) {
        if (!isExistPin(pinRedisRes)) {
            throw new GlobalException(NOT_FOUND_PIN);
        }
    }

    private static boolean isExistPin(PinRedisRes pinRedisRes) {
        return pinRedisRes != null;
    }
}
