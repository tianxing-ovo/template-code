package com.ltx.entity.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 分页结果
 *
 * @param total 总记录数
 * @param pages 总页数
 * @param list  当前页的数据列表
 * @author tianxing
 */
public record PageDTO<T>(Long total, Long pages, List<T> list) {

    public static <T> PageDTO<T> of(Page<?> page, Class<T> targetType) {
        List<?> records = page.getRecords();
        List<T> list = CollUtil.isEmpty(records) ? Collections.emptyList() : BeanUtil.copyToList(records, targetType);
        return new PageDTO<>(page.getTotal(), page.getPages(), list);
    }

    public static <PO, VO> PageDTO<VO> of(Page<PO> page, Function<PO, VO> function) {
        List<PO> records = page.getRecords();
        List<VO> list = CollUtil.isEmpty(records) ? Collections.emptyList() : records.stream().map(function).toList();
        return new PageDTO<>(page.getTotal(), page.getPages(), list);
    }
}
