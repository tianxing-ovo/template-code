package com.ltx.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 导出文件请求体
 *
 * @author tianxing
 */
@Data
public class ExportRequestBody {
    /**
     * 文件名称
     */
    @JsonProperty("file_name")
    private String fileName;

    /**
     * 导出字段列表
     */
    @JsonProperty("field_list")
    private List<String> fieldList;
}
