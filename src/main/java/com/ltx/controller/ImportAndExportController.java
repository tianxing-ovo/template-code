package com.ltx.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.ltx.easyexcel.service.ExportService;
import com.ltx.easyexcel.service.ImportService;
import com.ltx.entity.Result;
import com.ltx.entity.po.User;
import com.ltx.entity.request.ExportRequestBody;
import com.ltx.mapper.UserMapper;
import com.ltx.service.ExportTaskService;
import com.ltx.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 导入和导出控制器
 *
 * @author tianxing
 */
@RestController
@Slf4j
public class ImportAndExportController {

    @Resource
    private ExportService exportService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ImportService importService;

    @Resource
    private ExportTaskService exportTaskService;

    /**
     * 使用poi库导入xlsx文件
     *
     * @param file 文件
     * @return 通用响应对象
     */
    @PostMapping("/import-by-poi")
    public Result importByPoi(@RequestPart("file") MultipartFile file) {
        List<User> userList = importService.importByPoi(file);
        return Result.success().put("userList", userList);
    }

    /**
     * 使用easyExcel库导入
     *
     * @param file 文件
     * @return 通用响应对象
     */
    @PostMapping("/import-by-easyexcel")
    public Result importByEasyExcel(@RequestPart("file") MultipartFile file) {
        List<User> userList = importService.importByEasyExcel(file);
        return Result.success().put("userList", userList);
    }

    /**
     * 使用easyExcel库导出CSV文件到浏览器
     *
     * @param response    响应
     * @param requestBody 请求体
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ExportRequestBody requestBody) {
        List<User> list = userMapper.select();
        exportService.export(response, list, requestBody, User.class, ExcelTypeEnum.CSV);
    }

    /**
     * 使用easyExcel库异步导出CSV文件到本地
     *
     * @param requestBody 请求体
     */
    @PostMapping("/export-to-local")
    public void asyncExport(@RequestBody ExportRequestBody requestBody) {
        // 在主线程中提前获取userId(避免异步线程中ThreadLocal为空)
        Integer userId = UserContext.get().getId();
        exportService.asyncExport(requestBody, userId);
    }

    /**
     * 查询当前用户导出任务列表
     *
     * @return 通用响应对象
     */
    @GetMapping("/export-task-list")
    public Result exportTaskList() {
        return Result.success().put("exportTaskList", exportTaskService.queryExportTaskList());
    }
}
