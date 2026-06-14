package com.ltx.common.util;

import com.ltx.entity.po.User;

/**
 * 存储每个线程的用户信息
 *
 * @author tianxing
 */
public class UserContext {
    private static final ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static User get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     */
    public static void set(User user) {
        // ThreadLocalMap map = Thread.currentThread().threadLocals
        // ThreadLocalMap{key = THREAD_LOCAL, value = user}
        THREAD_LOCAL.set(user);
    }

    /**
     * 删除用户信息
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
