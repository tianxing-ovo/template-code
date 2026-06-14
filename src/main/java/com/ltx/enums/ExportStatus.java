package com.ltx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 导出状态枚举
 *
 * @author tianxing
 */
@AllArgsConstructor
@Getter
public enum ExportStatus {

    PENDING(0, "排队中"),
    EXPORTING(1, "导出中"),
    SUCCESS(2, "成功"),
    FAIL(3, "失败");

    private final int value;
    private final String desc;

    /**
     * 判断状态是否为已完成
     *
     * @param value 状态值
     * @return 是否已完成
     */
    public static boolean isFinished(Integer value) {
        return value != null && (value == SUCCESS.value || value == FAIL.value);
    }

}
