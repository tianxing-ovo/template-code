package com.ltx.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ltx.common.filter.CustomFilter;

/**
 * 过滤器配置
 *
 * @author tianxing
 */
@Configuration
public class FilterConfig {

    /**
     * 注册自定义过滤器
     *
     * @return 过滤器注册对象
     */
    @Bean
    public FilterRegistrationBean<CustomFilter> registerCustomFilter() {
        FilterRegistrationBean<CustomFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CustomFilter());
        // 拦截所有路径
        registration.addUrlPatterns("/*");
        // 设置过滤器的执行顺序 -> Order小的优先级高
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }
}
