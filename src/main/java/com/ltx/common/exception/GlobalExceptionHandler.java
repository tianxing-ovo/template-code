package com.ltx.common.exception;


import com.ltx.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理所有控制器层(Controller)抛出的异常 -> 将异常信息返回给前端(json格式)
 *
 * @author tianxing
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     *
     * @param customException 自定义异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = CustomException.class)
    public Result handleAccessDeniedException(CustomException customException) {
        String message = customException.getMessage();
        log.warn("【业务异常】code: {}, message: {}", customException.getCode(), message);
        return Result.fail(customException.getCode(), message);
    }

    /**
     * 处理实体类校验异常
     *
     * @param methodArgumentNotValidException 方法参数无效异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handleEntityNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        // key=字段名称,value=错误信息
        Map<String, Object> errorMap = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        log.warn("【参数校验异常】{}", errorMap);
        return Result.fail(HttpStatus.BAD_REQUEST.value(), "方法参数无效", errorMap);
    }

    /**
     * 处理单个参数校验异常
     *
     * @param constraintViolationException 约束冲突异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handleSingleParameterNotValidException(ConstraintViolationException constraintViolationException) {
        // key=字段名称,value=错误信息
        Map<String, Object> errorMap = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolationException.getConstraintViolations()) {
            errorMap.put(constraintViolation.getPropertyPath().toString().split("\\.")[1], constraintViolation.getMessage());
        }
        log.warn("【单参数校验异常】{}", errorMap);
        return Result.fail(HttpStatus.BAD_REQUEST.value(), "方法参数无效", errorMap);
    }


    /**
     * 处理参数反序列化异常
     *
     * @param e Http消息不可读异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("【参数反序列化异常】{}", e.getMessage());
        return Result.fail(HttpStatus.BAD_REQUEST.value(), "请求参数格式错误: " + e.getMessage());
    }

    /**
     * 处理未知异常
     *
     * @param e 异常
     * @return 通用响应对象
     */
    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e) {
        log.error("【系统未知异常】错误信息: {}, 异常类型: {}", e.getMessage(), e.getClass(), e);
        return Result.fail(203, e.getMessage());
    }
}
