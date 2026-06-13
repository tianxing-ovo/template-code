package com.ltx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误状态码枚举
 *
 * @author tianxing
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
    UNAUTHORIZED(201, "权限不足"),
    LOGIN_FAILED(202, "登录失败"),
    TOKEN_IS_NULL(203, "token为空"),
    TOKEN_IS_MISTAKE(204, "token错误"),
    USER_HAS_EXITED(205, "用户已退出,请重新登录");

    private final int code;
    private final String message;

    /**
     * 根据错误状态码获取提示信息
     *
     * @param code 错误状态码
     * @return 提示信息
     */
    public static String getMessageByCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode.message;
            }
        }
        return "";
    }
}
