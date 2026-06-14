package com.ltx.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 敏感信息注解
 *
 * @author tianxing
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SensitiveInfo {

    /**
     * @return 需要脱敏处理的字段
     */
    String[] value();
}

