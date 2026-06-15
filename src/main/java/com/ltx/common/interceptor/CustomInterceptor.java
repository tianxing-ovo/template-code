package com.ltx.common.interceptor;

import com.ltx.entity.po.User;
import com.ltx.common.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 *
 * @author tianxing
 */
@Component
@Slf4j
public class CustomInterceptor implements HandlerInterceptor {

    /**
     * 执行时机: 请求到达Controller之前
     * 执行顺序: 多个拦截器按照设定的Order顺序执行
     * 返回true: 继续执行后续拦截器和处理器
     * 返回false: 后续拦截器和处理器不再执行 -> 会执行preHandle返回true的拦截器的afterCompletion方法
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否继续执行后续操作
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        log.info("preHandle");
        User user = new User();
        user.setId(1);
        // 保存用户信息到ThreadLocal中
        UserContext.set(user);
        return true;
    }

    /**
     * 执行时机: 所有preHandle方法返回true + Controller方法执行完毕 + 视图渲染之前
     * 执行顺序: 多个拦截器按照设定的Order逆序执行
     * 注意: Controller抛出异常则不会执行
     *
     * @param request      请求
     * @param response     响应
     * @param handler      处理器
     * @param modelAndView 模型和视图
     */
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler, ModelAndView modelAndView) {
        log.info("postHandle");
    }

    /**
     * 执行时机: 整个请求处理完成(视图渲染完毕)后
     * 执行顺序: 多个拦截器按照设定的Order逆序执行
     * 注意: 仅对preHandle返回true的拦截器生效
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @param ex       异常
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler, Exception ex) {
        log.info("afterCompletion");
        // 清除ThreadLocal中的用户信息(避免内存泄漏)
        UserContext.remove();
    }
}
