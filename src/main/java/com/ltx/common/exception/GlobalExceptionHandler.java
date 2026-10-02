package com.ltx.common.exception;


import com.ltx.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 集中处理所有控制器层(Controller)抛出的异常 -> 将异常信息返回给前端(json格式)
 * warn: 用户操作失误
 * error: 系统自身故障
 *
 * @author tianxing
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param ex 业务异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result handleBusinessException(BusinessException ex) {
        String message = ex.getMessage();
        log.warn("【业务异常】code: {}, message: {}", ex.getCode(), message);
        return Result.fail(ex.getCode(), message);
    }

    /**
     * 处理方法参数无效异常(对象级校验)
     *
     * @param ex 方法参数无效异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 提取第一条错误信息作为提示文案
        String firstMessage = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse("请求参数无效");
        // {key=字段名称, value=错误信息}
        Map<String, Object> errorMap = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("【方法参数无效异常】{}", errorMap);
        return Result.fail(HttpStatus.BAD_REQUEST.value(), firstMessage, errorMap);
    }

    /**
     * 处理约束违反异常(单参数校验)
     *
     * @param ex 约束违反异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        // 提取第一条错误信息作为提示文案
        String firstMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse("请求参数无效");
        // {key=字段名称, value=错误信息}
        Map<String, Object> errorMap = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            // 提取参数名(methodName.paramName -> paramName)
            String path = constraintViolation.getPropertyPath().toString();
            String field = path.substring(path.lastIndexOf('.') + 1);
            errorMap.put(field, constraintViolation.getMessage());
        }
        log.warn("【约束违反异常】{}", errorMap);
        return Result.fail(HttpStatus.BAD_REQUEST.value(), firstMessage, errorMap);
    }


    /**
     * 处理Http消息不可读异常
     *
     * @param ex Http消息不可读异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("【Http消息不可读异常】{}", ex.getMessage());
        return Result.fail(HttpStatus.BAD_REQUEST.value(), "Http消息不可读");
    }

    /**
     * 处理不支持的HTTP请求方法异常
     *
     * @param ex 不支持的HTTP请求方法异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("【不支持的HTTP请求方法异常】{}", ex.getMessage());
        return Result.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), "不支持的HTTP请求方法");
    }

    /**
     * 处理未知异常
     *
     * @param ex 异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception ex) {
        log.error("【系统未知异常】", ex);
        return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统繁忙请稍后重试");
    }
}
