package com.ltx.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用响应对象
 *
 * @author tianxing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    // 状态码
    private Integer code;
    // 消息
    private String msg;
    // 数据
    private Map<String, Object> data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 成功
     *
     * @return 通用响应对象
     */
    public static Result success() {
        return new Result(200, "success");
    }

    /**
     * 成功
     *
     * @param msg 消息
     * @return 通用响应对象
     */
    public static Result success(String msg) {
        return new Result(200, msg);
    }

    /**
     * 失败
     *
     * @param code 状态码
     * @param msg  消息
     * @return 通用响应对象
     */
    public static Result fail(Integer code, String msg) {
        return new Result(code, msg);
    }

    /**
     * 失败
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     * @return 通用响应对象
     */
    public static Result fail(Integer code, String msg, Map<String, Object> data) {
        return new Result(code, msg, data);
    }

    /**
     * 存放数据
     *
     * @param key   键
     * @param value 值
     * @return 通用响应对象
     */
    public Result put(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, value);
        return this;
    }
}
