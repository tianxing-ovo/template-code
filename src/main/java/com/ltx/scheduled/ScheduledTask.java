package com.ltx.scheduled;


import com.ltx.util.RedissonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author tianxing
 */
@Component
@Slf4j(topic = "ScheduledTask")
@RequiredArgsConstructor
public class ScheduledTask {

    // 秒 分 时 日 月 周
    private final String cron = "* * * * * ?";

    private final RedissonUtil redissonUtil;

    /**
     * 通用的定时任务代码
     *
     * @param runnable  具体的定时任务代码逻辑
     * @param name      分布式锁的名称
     * @param waitTime  尝试获取锁时的等待时间(单位为秒) -> 指定的等待时间内未能获取到锁 -> 返回false
     * @param leaseTime 获取锁成功后的持有时间(单位为秒)
     */
    public void runTask(Runnable runnable, String name, long waitTime, long leaseTime) {
        // 分布式锁保证定时任务只执行一次
        RLock lock = redissonUtil.getLock(name);
        try {
            if (!redissonUtil.tryLock(lock, waitTime, leaseTime)) {
                log.info("获取锁失败");
                return;
            }
            // 执行具体的定时任务代码逻辑
            runnable.run();
        } finally {
            // 释放锁
            if (lock.isHeldByCurrentThread()) {
                redissonUtil.unlock(lock);
            }
        }
    }

    /**
     * 每秒执行一次
     */
    @Scheduled(cron = cron)
    @Async
    public void runTask() {
        runTask(() -> log.info("定时任务执行中..."), "lock", 1, 1);
    }
}
