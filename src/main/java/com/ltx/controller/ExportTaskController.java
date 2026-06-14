package com.ltx.controller;

import com.ltx.common.Result;
import com.ltx.service.ExportTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 删除指定的导出任务
     *
     * @param id 任务ID
     * @return 通用响应对象
     */
    @DeleteMapping("/export-tasks/{id}")
    public Result deleteExportTask(@PathVariable Long id) {
        boolean success = exportTaskService.deleteExportTask(id);
        if (success) {
            return Result.success();
        }
        return Result.fail(400, "删除失败，任务不存在或状态不符合要求");
    }
}
