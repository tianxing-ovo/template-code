package com.ltx.aop;

import com.ltx.annotation.PreAuthorize;
import com.ltx.entity.po.User;
import com.ltx.enums.ErrorCodeEnum;
import com.ltx.exception.CustomException;
import com.ltx.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 切面类(order值小的切面先执行)
 *
 * @author tianxing
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class UserAop {

    /**
     * 切入点
     */
    @Pointcut("execution(public com.ltx.entity.po.User com.ltx.aop.UserService.hello(String))")
    public void pointcut() {

    }

    /**
     * 前置通知: 在目标方法执行前执行
     */
    @Before("pointcut()")
    public void before() {
        System.out.println("Before Aop");
    }

    /**
     * 前置通知: 在带有指定注解的方法执行前执行
     *
     * @param joinPoint    连接点
     * @param preAuthorize 权限校验注解
     */
    @Before("@annotation(preAuthorize)")
    public void beforeAnnotation(JoinPoint joinPoint, PreAuthorize preAuthorize) {
        // 获取请求参数数组
        Object[] args = joinPoint.getArgs();
        log.info("请求参数: {}", Arrays.toString(args));
        // 获取注解中的角色数组
        String[] roles = preAuthorize.hasAnyRole();
        // 获取用户角色
        String role = args[0].toString();
        // 设置用户角色
        UserContext.get().setRole(role);
        // 判断用户角色是否在注解指定的角色中
        if (!Arrays.asList(roles).contains(role)) {
            throw new CustomException(ErrorCodeEnum.UNAUTHORIZED);
        }
    }

    /**
     * 后置通知: 在目标方法执行后执行(方法没有返回值时使用)
     */
    @After("pointcut()")
    public void after() {
        System.out.println("After Aop");
    }

    /**
     * 返回通知: 在目标方法返回结果后执行(方法有返回值时使用)
     *
     * @param user 目标方法的返回值
     */
    @AfterReturning(value = "pointcut()", returning = "user")
    public void afterReturning(User user) {
        // 修改返回值
        user.setName("张三");
        System.out.println("AfterReturning Aop");
    }

    /**
     * 异常通知: 在目标方法抛出异常后执行
     *
     * @param ex 异常对象
     */
    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterThrowing(Exception ex) {
        System.out.println(ex.getMessage());
        System.out.println("AfterThrowing Aop");
    }

    /**
     * 环绕通知: 在目标方法执行前后都执行
     *
     * @param joinPoint 连接点
     * @return 目标方法的返回值
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行目标方法之前的操作
        System.out.println("Before Around AOP: " + joinPoint.getSignature().getName());
        // 执行目标方法并获取返回值
        Object result = joinPoint.proceed();
        // 执行目标方法之后的操作
        System.out.println("After Around AOP: " + joinPoint.getSignature().getName());
        // 返回结果
        return result;
    }
}
