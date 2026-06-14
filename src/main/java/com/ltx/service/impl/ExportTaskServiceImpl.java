package com.ltx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ltx.constant.Constant;
import com.ltx.entity.po.ExportTask;
import com.ltx.entity.po.User;
import com.ltx.enums.ExportStatus;
import com.ltx.mapper.ExportTaskMapper;
import com.ltx.service.ExportTaskService;
import com.ltx.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 导出任务服务实现类
 *
 * @author tianxing
 */
@Service
@RequiredArgsConstructor
public class ExportTaskServiceImpl implements ExportTaskService {

    private final ExportTaskMapper exportTaskMapper;

    /**
     * 查询当前用户的导出任务列表
     *
     * @return 导出任务列表
     */
    @Override
    public List<ExportTask> queryExportTaskList() {
        User user = UserContext.get();
        Integer userId = user != null ? user.getId() : null;
        if (userId == null) {
            return Collections.emptyList();
        }
        return exportTaskMapper.selectList(new LambdaQueryWrapper<ExportTask>().
                eq(ExportTask::getUserId, userId).orderByDesc(ExportTask::getCreateTime));
    }

    /**
     * 删除当前用户的导出任务
     *
     * @param id 任务ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteExportTask(Long id) {
        User user = UserContext.get();
        Integer userId = user != null ? user.getId() : null;
        if (userId == null) {
            return false;
        }
        ExportTask exportTask = exportTaskMapper.selectById(id);
        // 任务不存在或任务所有者不匹配直接返回失败
        if (exportTask == null || !Objects.equals(userId, exportTask.getUserId())) {
            return false;
        }
        // 仅允许删除⌈成功⌋和⌈失败⌋状态的任务
        Integer exportStatus = exportTask.getExportStatus();
        if (!ExportStatus.isFinished(exportStatus)) {
            return false;
        }
        // ⌈成功⌋状态需要清理本地物理文件
        if (exportStatus == ExportStatus.SUCCESS.getValue() && exportTask.getFileName() != null) {
            File file = Constant.DESKTOP_PATH.resolve(exportTask.getFileName()).toFile();
            if (file.exists()) {
                FileUtil.del(file);
            }
        }
        exportTaskMapper.deleteById(id);
        return true;
    }
}
