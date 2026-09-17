package com.ltx.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ltx.common.exception.CustomException;
import com.ltx.common.util.UserContext;
import com.ltx.config.FileStorageProperties;
import com.ltx.entity.po.ExportTask;
import com.ltx.entity.po.User;
import com.ltx.enums.ExportStatus;
import com.ltx.mapper.ExportTaskMapper;
import com.ltx.service.ExportTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final FileStorageProperties fileStorageProperties;

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
        if (exportStatus == ExportStatus.SUCCESS.getValue() && exportTask.getFileKey() != null) {
            File file = fileStorageProperties.getStoragePath().resolve(exportTask.getFileKey()).toFile();
            if (file.exists()) {
                FileUtil.del(file);
            }
        }
        exportTaskMapper.deleteById(id);
        return true;
    }

    @Override
    public ResponseEntity<Resource> downloadExportFile(Long id) {
        Integer userId = UserContext.get().getId();
        // 查询指定的导出任务是否存在并且为⌈成功⌋状态
        ExportTask exportTask = exportTaskMapper.selectOne(new LambdaQueryWrapper<ExportTask>()
                .eq(ExportTask::getId, id)
                .eq(ExportTask::getUserId, userId)
                .eq(ExportTask::getExportStatus, ExportStatus.SUCCESS.getValue()));
        if (exportTask == null) {
            throw new CustomException(404, "导出文件不存在或任务尚未完成");
        }
        // 获取导出文件
        File file = fileStorageProperties.getStoragePath().resolve(exportTask.getFileKey()).toFile();
        // 生成文件名
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "用户数据_" + timeStr + ".xlsx";
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentLength(file.length())
                .body(new FileSystemResource(file));
    }
}
