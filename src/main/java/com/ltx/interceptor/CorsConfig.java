package com.ltx.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 *
 * @author tianxing
 */
@Configuration
public class CorsConfig {

    /**
     * 获取跨域过滤器
     *
     * @return 跨域过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        // 配置跨域
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        // 配置跨域源
        configuration.addAllowedOriginPattern("*");
        // 允许携带cookie跨域
        configuration.setAllowCredentials(true);
        // "/**"表示处理所有的跨域请求
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}