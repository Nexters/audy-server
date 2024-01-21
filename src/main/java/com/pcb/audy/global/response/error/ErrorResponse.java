package com.pcb.audy.global.response.error;

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
public class ErrorResponse<T> implements Serializable {

    private final HttpStatus status;
    private final String code;
    private final String message;

    public static <T> ErrorResponse<T> error(ErrorCode resultCode) {
        return ErrorResponse.<T>builder()
                .status(resultCode.getStatus())
                .code(resultCode.getErrorCode())
                .message(resultCode.getMessage())
                .build();
    }
}
