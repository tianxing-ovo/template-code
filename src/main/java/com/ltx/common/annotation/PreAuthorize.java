package com.ltx.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验注解
 *
 * @author tianxing
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreAuthorize {

    /**
     * 允许访问的角色列表(具备任一角色即可访问)
     *
     * @return 允许访问的角色列表
     */
    String[] hasAnyRole() default {};
}
