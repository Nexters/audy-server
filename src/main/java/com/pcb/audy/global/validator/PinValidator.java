package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_PIN;

import com.pcb.audy.domain.pin.entity.Pin;
import com.pcb.audy.global.exception.GlobalException;

public class PinValidator {
    public static void validate(Pin pin) {
        if (!isExistPin(pin)) {
            throw new GlobalException(NOT_FOUND_PIN);
        }
    }

    private static boolean isExistPin(Pin pin) {
        return pin != null;
    }
}
