package com.ltx.controller;

import com.ltx.common.Result;
import com.ltx.common.constant.Constant;
import com.ltx.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 认证控制器
 *
 * @author tianxing
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {

    private final AuthService authService;

    /**
     * 登录页面
     *
     * @return 登录视图
     */
    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 通用响应对象
     */
    @PostMapping("/login")
    public Result login(@RequestParam @NotBlank(message = "用户名不能为空") String username,
                        @RequestParam @NotBlank(message = "密码不能为空") String password) {
        String token = authService.login(username, password);
        return Result.success("登录成功").put(Constant.TOKEN, token);
    }

    /**
     * 登出
     *
     * @param request 请求
     * @return 通用响应对象
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader(Constant.TOKEN);
        authService.logout(token);
        return Result.success("退出成功");
    }
}
