package com.ltx.service;

/**
 * 认证服务接口
 *
 * @author tianxing
 */
public interface AuthService {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 令牌
     */
    String login(String username, String password);

    /**
     * 登出
     *
     * @param token 令牌
     */
    void logout(String token);
}
