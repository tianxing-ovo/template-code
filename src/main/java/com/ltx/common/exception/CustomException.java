package com.ltx.common.exception;

import com.ltx.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;

/**
 * 自定义异常
 *
 * @author tianxing
 */
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
