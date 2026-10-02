package com.ltx.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ltx.common.filter.RequestLogFilter;

/**
 * 过滤器配置
 *
 * @author tianxing
 */
@Configuration
public class FilterConfig {

    /**
     * 注册请求日志与耗时统计过滤器
     *
     * @return 过滤器注册对象
     */
    @Bean
    public FilterRegistrationBean<RequestLogFilter> registerRequestLogFilter() {
        FilterRegistrationBean<RequestLogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestLogFilter());
        // 拦截所有路径
        registration.addUrlPatterns("/*");
        // 设置过滤器的执行顺序 -> Order小的优先级高
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }
}
