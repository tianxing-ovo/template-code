package com.ltx.common.easyexcel.converter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.Collections;
import java.util.List;

/**
 * List转换器
 *
 * @author tianxing
 */
public class ListConverter implements Converter<List<String>> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return List.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 转换为Java数据
     */
    @Override
    public List<String> convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String value = cellData.getStringValue();
        if (StrUtil.isBlank(value)) {
            return Collections.emptyList();
        }
        return StrUtil.split(value.trim(), ',');
    }

    /**
     * 转换为Excel数据
     */
    @Override
    public WriteCellData<?> convertToExcelData(List<String> list, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (list == null || list.isEmpty()) {
            return new WriteCellData<>("--");
        } else {
            return new WriteCellData<>(String.join(",", list));
        }
    }
}

