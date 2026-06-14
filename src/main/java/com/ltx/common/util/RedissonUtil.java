package com.ltx.common.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.util.concurrent.TimeUnit;

/**
 * Redisson工具类
 *
 * @author tianxing
 */
@Component
@Indexed
@RequiredArgsConstructor
public class RedissonUtil {

    private final RedissonClient redissonClient;

    /**
     * 获取分布式锁
     *
     * @param name 锁名称
     * @return 分布式锁
     */
    public RLock getLock(String name) {
        return redissonClient.getLock(name);
    }

    /**
     * 尝试获取锁
     *
     * @param lock      分布式锁
     * @param waitTime  等待时间
     * @param leaseTime 持有时间
     * @return 是否获取到锁
     */
    @SneakyThrows
    public boolean tryLock(RLock lock, long waitTime, long leaseTime) {
        return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * 释放锁
     *
     * @param lock 分布式锁
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }
}
