package com.ltx.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 线程池属性
 *
 * @author tianxing
 */
@Component
@ConfigurationProperties("thread-pool")
@Data
public class ThreadPoolConfigProperties {
    // 核心线程数
    private int corePoolSize;
    // 最大线程数
    private int maximumPoolSize;
    // 非核心线程的存活时间
    private long keepAliveTime;
    // 存活时间的时间单位
    private TimeUnit unit;
}
