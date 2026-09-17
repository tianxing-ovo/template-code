package com.ltx.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 文件导出请求参数
 *
 * @param fieldList 字段列表
 * @author tianxing
 */
public record ExportRequest(@JsonProperty("field_list") List<String> fieldList) {
}
