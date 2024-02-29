package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.*;

import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.global.exception.GlobalException;
import org.springframework.util.StringUtils;

public class PinValidator {
    private static final int MAX_LENGTH = 10;

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

    public static void validateName(String name) {
        if (!isValidName(name)) {
            throw new GlobalException(VALID_PIN_NAME);
        }
    }

    private static boolean isExistPin(PinRedisRes pinRedisRes) {
        return pinRedisRes != null;
    }

    private static boolean isExceed(int pinCnt) {
        return pinCnt >= 15;
    }

    private static boolean isValidName(String name) {
        return StringUtils.hasLength(name) && name.length() <= MAX_LENGTH;
    }
}
