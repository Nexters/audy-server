package com.pcb.audy.global.exception;

import com.pcb.audy.global.response.ResultCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final ResultCode resultCode;

    public GlobalException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
