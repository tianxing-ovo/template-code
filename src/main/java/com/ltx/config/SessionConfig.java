package com.ltx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;


/**
 * springSession配置类
 *
 * @author tianxing
 */
@Configuration
public class SessionConfig {

    /**
     * cookie序列化器
     *
     * @return {@link CookieSerializer}
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        // 设置作用域
        cookieSerializer.setDomainName(".xxx.com");
        // 设置cookie名字
        cookieSerializer.setCookieName("xxx-session");
        return cookieSerializer;
    }


    /**
     * Redis序列化器
     *
     * @return {@link RedisSerializer}
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
