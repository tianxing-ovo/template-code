package com.ltx.easyexcel.converter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.ltx.enums.Sex;

/**
 * 性别枚举转换器
 *
 * @author tianxing
 */
public class SexConverter implements Converter<Sex> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Sex.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 转换为Java数据
     */
    @Override
    public Sex convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
            GlobalConfiguration globalConfiguration) {
        String value = cellData.getStringValue();
        if (StrUtil.isBlank(value)) {
            return null;
        }
        return Sex.getEnumByDesc(value.trim());
    }

    /**
     * 转换为Excel数据
     */
    @Override
    public WriteCellData<?> convertToExcelData(Sex sex, ExcelContentProperty contentProperty,
            GlobalConfiguration globalConfiguration) {
        int value = sex.getValue();
        return new WriteCellData<>(Sex.getDescByValue(value));
    }
}
