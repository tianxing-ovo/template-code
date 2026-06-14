package com.ltx.common.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 自定义过滤器
 *
 * @author tianxing
 */
@Slf4j
public class CustomFilter extends OncePerRequestFilter {

    /**
     * 对每个请求执行一次过滤操作
     * 执行顺序: before filter1 -> before filter2 -> controller -> after filter2 -> after filter1
     *
     * @param request     请求对象
     * @param response    响应对象
     * @param filterChain 过滤器链
     */
    @SneakyThrows
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, FilterChain filterChain) {
        // controller处理请求前顺序执行
        log.info("before filter");
        // 将请求和响应传递给下一个过滤器或目标Servlet
        filterChain.doFilter(request, response);
        // controller处理完请求并生成响应后逆序执行
        log.info("after filter");
    }
}
