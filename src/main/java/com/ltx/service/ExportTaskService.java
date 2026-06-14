package com.ltx.service;

import com.ltx.entity.po.ExportTask;

import java.util.List;

/**
 * 导出任务服务接口
 *
 * @author tianxing
 */
public interface ExportTaskService {

    List<ExportTask> queryExportTaskList();

    /**
     * 删除指定的导出任务
     *
     * @param id 任务ID
     * @return 是否删除成功
     */
    boolean deleteExportTask(Long id);
}
