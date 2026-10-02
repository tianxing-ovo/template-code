package com.ltx.common.exception;

import com.ltx.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常
 *
 * @author tianxing
 */
@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
