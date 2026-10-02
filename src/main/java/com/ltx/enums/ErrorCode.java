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
    LOGIN_FAILED(202, "用户名或密码错误"),
    TOKEN_IS_NULL(203, "token为空"),
    TOKEN_EXPIRED(204, "token过期"),
    TOKEN_INVALID(205, "token无效"),
    USER_HAS_EXITED(206, "用户已退出"),
    ACCOUNT_DISABLED(207, "账号已被禁用"),
    ACCOUNT_LOCKED(208, "账号已被锁定"),
    ACCOUNT_LOGGED_IN_ELSEWHERE(209, "账号已在别处登录");

    private final int code;
    private final String message;

}
