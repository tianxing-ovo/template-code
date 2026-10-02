package com.ltx.common.interceptor;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.ltx.common.Result;
import com.ltx.common.constant.Constant;
import com.ltx.common.util.JwtUtil;
import com.ltx.common.util.RedisUtil;
import com.ltx.common.util.ServletUtil;
import com.ltx.common.util.UserContext;
import com.ltx.entity.po.User;
import com.ltx.enums.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户上下文拦截器
 *
 * @author tianxing
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UserContextInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    /**
     * 执行时机: 请求到达Controller之前
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否继续执行后续操作
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        // 从请求头中获取token
        String token = request.getHeader(Constant.TOKEN);
        // 如果token为空
        if (StrUtil.isBlank(token)) {
            ServletUtil.write(response, Result.fail(ErrorCode.TOKEN_IS_NULL));
            return false;
        }
        Claims claims;
        try {
            // 校验token
            claims = jwtUtil.getPayLoad(token);
        } catch (ExpiredJwtException e) {
            ServletUtil.write(response, Result.fail(ErrorCode.TOKEN_EXPIRED));
            return false;
        } catch (Exception e) {
            ServletUtil.write(response, Result.fail(ErrorCode.TOKEN_INVALID));
            return false;
        }
        // 获取用户
        User user = Convert.convert(User.class, claims.get(Constant.USER));
        // key = login:user:<userId>
        String key = Constant.LOGIN_USER_KEY + user.getId();
        // 获取Redis中的token
        String currentToken = redisUtil.get(key);
        // 如果Redis中没有token
        if (currentToken == null) {
            ServletUtil.write(response, Result.fail(ErrorCode.USER_HAS_EXITED));
            return false;
        }
        // 如果Redis中的token与请求中的token不一致
        if (!currentToken.equals(token)) {
            ServletUtil.write(response, Result.fail(ErrorCode.ACCOUNT_LOGGED_IN_ELSEWHERE));
            return false;
        }
        // 保存用户到ThreadLocal中
        UserContext.set(user);
        return true;
    }

    /**
     * 执行时机: 整个请求处理完成(视图渲染完毕)后
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @param ex       异常
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        // 清除ThreadLocal中的用户(避免内存泄漏)
        UserContext.remove();
    }
}
