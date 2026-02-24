package com.ltx.cache;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * 缓存配置
 *
 *
 * @author tianxing
 */
@EnableConfigurationProperties(CacheProperties.class)
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 自定义Redis缓存配置
     *
     * @param cacheProperties 缓存配置属性
     * @return Redis缓存配置
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        // 获取默认配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 指定key的序列化方式为String
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        // 指定value的序列化方式为json
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            // 缓存过期时间
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            // 键前缀
            config = config.computePrefixWith((cacheName) -> redisProperties.getKeyPrefix() + cacheName + ":");
        }
        if (!redisProperties.isCacheNullValues()) {
            // 禁用缓存空值
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            // 禁用键前缀
            config = config.disableKeyPrefix();
        }
        return config;
    }
}

