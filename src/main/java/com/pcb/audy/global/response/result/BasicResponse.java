package com.pcb.audy.global.response.result;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BasicResponse<T> implements Serializable {

    private final HttpStatus status;
    private final String code;
    private final String message;
    private final Object data;

    public static <T> BasicResponse<T> success() {
        return BasicResponse.<T>builder()
                .status(ResultCode.SUCCESS.getStatus())
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> BasicResponse<T> success(T data) {
        return BasicResponse.<T>builder()
                .status(ResultCode.SUCCESS.getStatus())
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }
}
