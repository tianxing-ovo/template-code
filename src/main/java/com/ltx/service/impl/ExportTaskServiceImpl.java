package com.ltx.service.impl;

import com.ltx.mapper.ExportTaskMapper;
import com.ltx.service.ExportTaskService;
import com.ltx.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author tianxing
 */
@Service
@RequiredArgsConstructor
public class ExportTaskServiceImpl implements ExportTaskService {

    private final ExportTaskMapper exportTaskMapper;

    @Override
    public List<LinkedHashMap<String, Object>> queryExportTaskList() {
        Integer id = UserContext.get().getId();
        return exportTaskMapper.queryExportTask(id);
    }
}
