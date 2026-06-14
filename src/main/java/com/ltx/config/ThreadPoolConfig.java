package com.ltx.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;


/**
 * 线程池配置
 *
 * @author tianxing
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 存在自定义线程池时 -> 不会自动配置ThreadPoolTaskExecutor
     *
     * @param properties 自定义线程池属性
     * @return 自定义线程池
     */
    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties properties) {
        // 任务队列: 存储待处理的任务
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(100000);
        // 线程工厂: 创建新线程,可以通过它自定义线程名
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        // 拒绝策略: 线程池和队列都满时如何处理新提交的任务
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                properties.getUnit(), workQueue, threadFactory, handler);
    }

    /**
     * 执行异步任务的线程池
     * TaskExecutionAutoConfiguration已经将TaskExecutionProperties注册到Spring容器中
     *
     * @param properties 异步任务线程池属性
     * @return 异步任务线程池
     */
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor(TaskExecutionProperties properties) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        return new ThreadPoolTaskExecutorBuilder()
                .corePoolSize(pool.getCoreSize())
                .maxPoolSize(pool.getMaxSize())
                .keepAlive(pool.getKeepAlive())
                .queueCapacity(pool.getQueueCapacity())
                .threadNamePrefix(properties.getThreadNamePrefix()).build();
    }
}
