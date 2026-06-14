package com.ltx.common.easyexcel.converter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.ltx.enums.Role;

/**
 * 角色枚举转换器
 *
 * @author tianxing
 */
public class RoleConverter implements Converter<Role> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Role.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 转换为Java数据
     */
    @Override
    public Role convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 获取角色值
        String value = cellData.getStringValue();
        if (StrUtil.isBlank(value)) {
            return null;
        }
        return Role.getEnumByDesc(value.trim());
    }

    /**
     * 转换为Excel数据
     */
    @Override
    public WriteCellData<?> convertToExcelData(Role role, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String value = role.getValue();
        return new WriteCellData<>(Role.getDescByValue(value));
    }
}
