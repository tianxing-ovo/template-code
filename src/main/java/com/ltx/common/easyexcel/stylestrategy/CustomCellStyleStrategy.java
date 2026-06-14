package com.ltx.common.easyexcel.stylestrategy;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.Collections;

/**
 * 自定义单元格样式
 *
 * @author tianxing
 */
public class CustomCellStyleStrategy extends HorizontalCellStyleStrategy {

    public CustomCellStyleStrategy() {
        // 设置表头水平垂直居中对齐
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        this.setHeadWriteCellStyle(headWriteCellStyle);
        // 设置表体水平垂直居中对齐
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        this.setContentWriteCellStyleList(Collections.singletonList(contentWriteCellStyle));
    }
}
