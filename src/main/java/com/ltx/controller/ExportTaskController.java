package com.ltx.controller;

import com.ltx.entity.Result;
import com.ltx.service.ExportTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 导出任务控制器
 *
 * @author tianxing
 */
@RestController
@RequiredArgsConstructor
public class ExportTaskController {

    private final ExportTaskService exportTaskService;

    /**
     * 查询当前用户导出任务列表
     *
     * @return 通用响应对象
     */
    @GetMapping("/export-tasks")
    public Result exportTaskList() {
        return Result.success().put("exportTaskList", exportTaskService.queryExportTaskList());
    }
}
