package com.ltx.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Redisson配置
 *
 * @author tianxing
 */
@Configuration
public class RedissonConfig {

    /**
     * 创建Redisson客户端
     *
     * @return {@link RedissonClient}
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        // 创建配置
        Config config = new Config();
        // 单节点模式
        config.useSingleServer().setAddress("redis://localhost:6379").setPassword("123");
        // 根据Config创建出RedissonClient实例
        return Redisson.create(config);
    }
}
