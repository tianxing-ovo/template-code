package com.ltx.controller;

import com.ltx.common.Result;
import com.ltx.common.exception.CustomException;
import com.ltx.common.valid.InsertGroup;
import com.ltx.entity.request.UserRequestBody;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 测试控制器
 *
 * @author tianxing
 */
@RestController
@RequestMapping("/test")
@Validated
@Slf4j
public class TestController {

    /**
     * 测试国际化功能
     */
    @GetMapping("/i18n")
    public void testInternationalization() {
        throw new CustomException(404, "未找到");
    }

    /**
     * 测试实体类校验功能
     *
     * @param requestBody 请求体
     * @return 通用响应对象
     */
    @PostMapping("/validation")
    public Result testValidation(@Validated(InsertGroup.class) @RequestBody UserRequestBody requestBody) {
        return Result.success().put("requestBody", requestBody);
    }

    /**
     * 测试单个参数校验功能
     *
     * @param name 名字
     * @return 通用响应对象
     */
    @GetMapping("/validation")
    public Result testValidation(@RequestParam("name") @Size(min = 1, max = 10) String name) {
        return Result.success().put("name", name);
    }
}
