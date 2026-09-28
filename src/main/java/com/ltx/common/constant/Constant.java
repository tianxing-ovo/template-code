package com.ltx.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 常量
 *
 * @author tianxing
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {

    public static final String COMMA = ",";
    public static final String TOKEN = "token";
    // 用户
    public static final String USER = "user";
    // 权限列表
    public static final String AUTHORITIES = "authorities";
    public static final long TWO_HOURS = 1000 * 60 * 60 * 2L;
    public static final String LOGIN_TOKEN_KEY = "login:token:";
    // 角色前缀
    public static final String ROLE_PREFIX = "ROLE_";
}
