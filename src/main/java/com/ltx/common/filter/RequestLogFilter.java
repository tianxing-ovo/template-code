package com.ltx.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 请求日志过滤器
 *
 * @author tianxing
 */
@Slf4j
public class RequestLogFilter extends OncePerRequestFilter {

    /**
     * 对每个请求执行一次过滤操作
     * 执行顺序: before filter1 -> before filter2 -> controller -> after filter2 -> after filter1
     *
     * @param request     请求对象
     * @param response    响应对象
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // controller处理请求前顺序执行
        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("[HTTP-IN] {} {}", method, uri);
        // 将请求和响应传递给下一个过滤器或目标Servlet
        filterChain.doFilter(request, response);
        // controller处理完请求并生成响应后逆序执行
        long cost = System.currentTimeMillis() - startTime;
        log.info("[HTTP-OUT] {} {} | 状态码: {} | 耗时: {}ms", method, uri, response.getStatus(), cost);
    }
}
