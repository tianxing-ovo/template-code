package com.ltx.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.ltx.common.constant.Constant;
import com.ltx.common.easyexcel.stylestrategy.CustomCellStyleStrategy;
import com.ltx.common.easyexcel.writehandler.CustomCellWriteHandler;
import com.ltx.entity.po.ExportTask;
import com.ltx.entity.po.User;
import com.ltx.entity.request.ExportRequestBody;
import com.ltx.enums.ExportStatus;
import com.ltx.mapper.ExportTaskMapper;
import com.ltx.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户导出服务类
 *
 * @author tianxing
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserExportService {

    private final UserMapper userMapper;
    private final ExportTaskMapper exportTaskMapper;

    /**
     * 获取用于导出的用户列表
     *
     * @return 用户列表
     */
    public List<User> getExportUsers() {
        // 查询前10个用户
        Page<User> pageParam = new Page<>(1, 10);
        pageParam.setSearchCount(false);
        return userMapper.selectPage(pageParam, null).getRecords();
    }

    /**
     * 导出文件到浏览器
     *
     * @param response    响应
     * @param list        数据列表
     * @param requestBody 请求体
     * @param clazz       数据类型
     */
    @SneakyThrows
    public <T> void export(HttpServletResponse response, List<T> list, ExportRequestBody requestBody, Class<T> clazz) {
        String fileName = requestBody.getFileName();
        List<String> fieldList = requestBody.getFieldList();
        // 设置响应内容类型和字符集
        response.setContentType(ExcelUtil.XLSX_CONTENT_TYPE + ";charset=UTF-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        EasyExcel.write(response.getOutputStream(), clazz).excelType(ExcelTypeEnum.XLSX)
                .registerWriteHandler(new CustomCellStyleStrategy())
                .registerWriteHandler(new CustomCellWriteHandler())
                .includeColumnFieldNames(fieldList)
                .orderByIncludeColumn(true)
                .sheet().doWrite(list);
    }

    /**
     * 导出文件到本地
     *
     * @param list        数据列表
     * @param fileName    文件名称
     * @param requestBody 请求体
     */
    private <T> void exportToLocal(List<T> list, String fileName, ExportRequestBody requestBody) {
        List<String> fieldList = requestBody.getFieldList();
        EasyExcel.write(Constant.DESKTOP_PATH.resolve(fileName).toFile(), User.class)
                .excelType(ExcelTypeEnum.XLSX)
                .registerWriteHandler(new CustomCellStyleStrategy())
                .registerWriteHandler(new CustomCellWriteHandler())
                .includeColumnFieldNames(fieldList)
                .orderByIncludeColumn(true)
                .sheet().doWrite(list);
    }

    /**
     * 创建排队中的导出任务
     *
     * @param requestBody 请求体
     * @param userId      用户ID
     * @return 导出任务实体
     */
    public ExportTask createPendingTask(ExportRequestBody requestBody, Integer userId) {
        String baseFileName = requestBody.getFileName();
        // 提取文件名和扩展名
        String mainName = StrUtil.blankToDefault(FileUtil.mainName(baseFileName), "export");
        String extName = StrUtil.blankToDefault(FileUtil.extName(baseFileName), "xlsx");
        // 组装最终文件名
        String fileName = StrUtil.format("{}-{}-{}.{}", mainName, userId, System.currentTimeMillis(), extName);
        // 创建导出任务实体
        ExportTask exportTask = new ExportTask();
        exportTask.setUserId(userId);
        exportTask.setFileName(fileName);
        exportTask.setExportStatus(ExportStatus.PENDING.getValue());
        // 插入数据库
        exportTaskMapper.insert(exportTask);
        return exportTask;
    }

    /**
     * 异步导出Excel文件到本地
     *
     * @param requestBody 请求体
     * @param taskId      任务ID
     * @param fileName    预先生成的文件名
     */
    @Async
    public void asyncExport(ExportRequestBody requestBody, Long taskId, String fileName) {
        // 更新任务状态为⌈导出中⌋
        ExportTask updateTask = new ExportTask();
        updateTask.setId(taskId);
        updateTask.setExportStatus(ExportStatus.EXPORTING.getValue());
        exportTaskMapper.updateById(updateTask);
        try {
            // 查询用户数据
            List<User> userList = userMapper.selectList(null);
            // 导出到本地
            exportToLocal(userList, fileName, requestBody);
            // 获取文件大小和记录数
            File file = Constant.DESKTOP_PATH.resolve(fileName).toFile();
            long fileSize = FileUtil.size(file);
            int totalRecords = userList.size();
            // 更新任务状态为⌈成功⌋
            ExportTask successTask = new ExportTask();
            successTask.setId(taskId);
            successTask.setExportStatus(ExportStatus.SUCCESS.getValue());
            successTask.setFilePath("/files/" + fileName);
            successTask.setFileSize(fileSize);
            successTask.setTotalRecords(totalRecords);
            exportTaskMapper.updateById(successTask);
        } catch (Exception e) {
            log.error("异步导出Excel文件到本地发生异常", e);
            // 更新任务状态为⌈失败⌋并记录错误原因
            ExportTask failTask = new ExportTask();
            failTask.setId(taskId);
            failTask.setExportStatus(ExportStatus.FAIL.getValue());
            failTask.setFailReason(e.getMessage());
            exportTaskMapper.updateById(failTask);
        }
    }
}
