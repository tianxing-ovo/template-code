package com.ltx.service;

import com.ltx.entity.po.ExportTask;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 导出任务服务接口
 *
 * @author tianxing
 */
public interface ExportTaskService {

    /**
     * 查询导出任务列表
     *
     * @return 导出任务列表
     */
    List<ExportTask> queryExportTaskList();

    /**
     * 删除指定的导出任务
     *
     * @param id 任务ID
     * @return 是否删除成功
     */
    boolean deleteExportTask(Long id);

    /**
     * 下载指定的导出文件
     *
     * @param id 任务ID
     * @return 响应实体
     */
    ResponseEntity<Resource> downloadExportFile(Long id);
}
