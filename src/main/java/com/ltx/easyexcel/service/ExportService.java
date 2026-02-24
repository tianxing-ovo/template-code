package com.ltx.easyexcel.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.ltx.constant.Constant;
import com.ltx.easyexcel.stylestrategy.CustomCellStyleStrategy;
import com.ltx.easyexcel.writehandler.CustomCellWriteHandler;
import com.ltx.entity.po.User;
import com.ltx.entity.request.ExportRequestBody;
import com.ltx.mapper.ExportTaskMapper;
import com.ltx.mapper.UserMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author tianxing
 */
@Service
@Slf4j
public class ExportService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ExportTaskMapper exportTaskMapper;

    /**
     * 导出文件到浏览器
     *
     * @param list        数据列表
     * @param requestBody 请求体
     * @param clazz       数据类型
     */
    @SneakyThrows
    public <T> void export(HttpServletResponse response, List<T> list, ExportRequestBody requestBody, Class<T> clazz,
            ExcelTypeEnum excelType) {
        String fileName = requestBody.getFileName();
        List<String> fieldList = requestBody.getFieldList();
        response.setContentType("text/csv;charset=UTF-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        EasyExcel.write(response.getOutputStream(), clazz).excelType(excelType)
                .registerWriteHandler(new CustomCellStyleStrategy())
                .registerWriteHandler(new CustomCellWriteHandler())
                .includeColumnFieldNames(fieldList).sheet().doWrite(list);
    }

    /**
     * 导出CSV文件到本地
     *
     * @param list        数据列表
     * @param requestBody 请求体
     */
    private <T> void exportToLocal(List<T> list, ExportRequestBody requestBody) {
        String fileName = requestBody.getFileName();
        List<String> fieldList = requestBody.getFieldList();
        EasyExcel.write(Constant.DESKTOP_PATH.resolve(fileName).toFile(), User.class)
                .excelType(ExcelTypeEnum.CSV)
                .registerWriteHandler(new CustomCellStyleStrategy())
                .registerWriteHandler(new CustomCellWriteHandler())
                .includeColumnFieldNames(fieldList).sheet().doWrite(list);
    }

    /**
     * 异步导出CSV文件到本地
     *
     * @param requestBody 请求体
     * @param userId      用户id
     */
    @Async
    public void asyncExport(ExportRequestBody requestBody, Integer userId) {
        String fileName = String.format("%s-%d-%d", requestBody.getFileName(), userId, System.currentTimeMillis());
        // 插入导出任务
        exportTaskMapper.insertExportTask(userId, fileName);
        List<User> userList = userMapper.selectAll();
        exportToLocal(userList, requestBody);
        // 更新导出状态
        exportTaskMapper.updateExportStatus(userId, fileName);
    }
}
