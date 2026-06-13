package com.ltx.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.ltx.annotation.PreAuthorize;
import com.ltx.service.UserExportService;
import com.ltx.service.UserImportService;
import com.ltx.entity.Result;
import com.ltx.entity.po.User;
import com.ltx.entity.request.ExportRequestBody;
import com.ltx.entity.request.UserRequestBody;
import com.ltx.enums.Role;
import com.ltx.exception.CustomException;
import com.ltx.mapper.UserMapper;
import com.ltx.util.UserContext;
import com.ltx.valid.InsertGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 用户控制器
 *
 * @author tianxing
 */
@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserExportService userExportService;
    private final UserImportService userImportService;

    /**
     * 查询用户信息
     *
     * @return 通用响应对象
     */
    @PreAuthorize(hasAnyRole = "admin")
    @GetMapping("/users/me")
    public Result queryCurrentUser(@RequestParam("role") Role role) {
        log.info("role: {}", role);
        return Result.success().put("user", UserContext.get());
    }

    /**
     * 查询指定用户
     */
    @GetMapping("/users/{id}")
    @Cacheable(value = "userCache", key = "T(com.ltx.constant.RedisConstant).CACHE_USER_KEY+#id", unless = "#result == null")
    public User query(@PathVariable Integer id) {
        return userMapper.selectById(id);
    }

    /**
     * 查询用户列表
     *
     * @param requestBody 请求体
     * @return 用户列表
     */
    @GetMapping("/users")
    @Cacheable(value = "userCache", key = "T(com.ltx.constant.RedisConstant).CACHE_USER_KEY + (#requestBody.id == null && #requestBody.age == null && #requestBody.name == null ? 'allUsers' : #requestBody.id + ':' + #requestBody.age + ':' + #requestBody.name)", unless = "#result == null || #result.size() == 0")
    public List<User> queryUserList(UserRequestBody requestBody) {
        return userMapper.queryUserList(requestBody);
    }

    /**
     * 新增
     *
     * @param user 用户
     * @return 用户
     */
    @PostMapping("/users")
    @CachePut(value = "userCache", key = "#user.id")
    public User add(@RequestBody User user) {
        userMapper.add(user);
        return user;
    }

    /**
     * 删除
     *
     * @param id 用户id
     */
    @DeleteMapping("/users/{id}")
    @CacheEvict(value = "userCache", key = "#id")
    public void delete(@PathVariable Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 更新
     *
     * @param id 用户id
     */
    @PutMapping("/users/{id}")
    @CacheEvict(value = "userCache", key = "#id")
    public void update(@PathVariable Integer id) {
        User user = new User();
        user.setId(id);
        user.setAge(20);
        userMapper.updateById(user);
    }

    /**
     * 测试国际化功能
     */
    @GetMapping("/test/i18n")
    public void testInternationalization() {
        throw new CustomException(404, "未找到");
    }

    /**
     * 测试实体类校验功能
     *
     * @param requestBody 请求体
     * @return 通用响应对象
     */
    @PostMapping("/test/validation")
    public Result testValidation(@Validated(InsertGroup.class) @RequestBody UserRequestBody requestBody) {
        return Result.success().put("requestBody", requestBody);
    }

    /**
     * 测试单个参数校验功能
     *
     * @param name 名字
     * @return 通用响应对象
     */
    @GetMapping("/test/validation")
    public Result testValidation(@RequestParam("name") @Size(min = 1, max = 10) String name) {
        return Result.success().put("name", name);
    }

    /**
     * 导入用户数据
     *
     * @param file 文件
     * @return 通用响应对象
     */
    @PostMapping("/users/import")
    public Result importUsers(@RequestPart("file") MultipartFile file) {
        List<User> userList = userImportService.importUsers(file);
        return Result.success().put("userList", userList);
    }

    /**
     * 使用easyExcel库导出CSV文件到浏览器
     *
     * @param response    响应
     * @param requestBody 请求体
     */
    @PostMapping("/users/export")
    public void export(HttpServletResponse response, @RequestBody ExportRequestBody requestBody) {
        List<User> list = userExportService.getExportUsers();
        userExportService.export(response, list, requestBody, User.class, ExcelTypeEnum.CSV);
    }

    /**
     * 使用easyExcel库异步导出CSV文件到本地
     *
     * @param requestBody 请求体
     */
    @PostMapping("/users/export/local")
    public void asyncExport(@RequestBody ExportRequestBody requestBody) {
        // 在主线程中提前获取userId(避免异步线程中ThreadLocal为空)
        Integer userId = UserContext.get().getId();
        userExportService.asyncExport(requestBody, userId);
    }
}
