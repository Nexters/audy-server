package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.*;

import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.global.exception.GlobalException;

public class PinValidator {
    public static void validate(PinRedisRes pinRedisRes) {
        if (!isExistPin(pinRedisRes)) {
            throw new GlobalException(NOT_FOUND_PIN);
        }
    }

    public static void checkIsExceedPinLimit(int pinCnt) {
        if (isExceed(pinCnt)) {
            throw new GlobalException(EXCEED_PIN_LIMIT);
        }
    }

    private static boolean isExistPin(PinRedisRes pinRedisRes) {
        return pinRedisRes != null;
    }

    private static boolean isExceed(int pinCnt) {
        return pinCnt >= 15;
    }
}
