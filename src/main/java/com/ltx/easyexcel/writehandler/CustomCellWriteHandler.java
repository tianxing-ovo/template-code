package com.ltx.easyexcel.writehandler;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * 自定义CellWriteHandler
 *
 * @author tianxing
 */
public class CustomCellWriteHandler implements CellWriteHandler {

    /**
     * 处理null/空字符串
     * STRING: 字符串
     * BLANK: 空白(不包含任何数据)
     * _NONE: 类型未知/尚未初始化
     *
     * @param context 上下文
     */
    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        Cell cell = context.getCell();
        CellType cellType = cell.getCellType();
        if ((cellType == CellType.STRING && StringUtils.isBlank(cell.getStringCellValue()))
                || cellType == CellType.BLANK || cellType == CellType._NONE) {
            cell.setCellValue("--");
        }
    }
}
