package com.ltx.common.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 常量
 *
 * @author tianxing
 */
public interface Constant {
    String USER_HOME = System.getProperty("user.home");
    Path DESKTOP_PATH = Paths.get(USER_HOME, "Desktop");
    String COMMA = ",";
    String TOKEN = "token";
    // 用户
    String USER = "user";
    // 权限列表
    String AUTHORITIES = "authorities";
    long TWO_HOURS = 1000 * 60 * 60 * 2;
    String LOGIN_TOKEN_KEY = "login:token:";
    // 角色前缀
    String ROLE_PREFIX = "ROLE_";
}
