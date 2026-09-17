package com.ltx.entity.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页查询实体
 *
 * @param pageNum   页码
 * @param pageSize  每页数量
 * @param sortField 排序字段
 * @param sortOrder 升序还是降序
 * @author tianxing
 */
public record PageQuery(Integer pageNum, Integer pageSize, String sortField, String sortOrder) {

    public PageQuery {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (StrUtil.isBlank(sortOrder)) {
            sortOrder = "asc";
        }
    }

    /**
     * 无参构造器
     */
    public PageQuery() {
        this(1, 10, null, "asc");
    }

    /**
     * 转换为分页对象
     *
     * @param items 排序字段
     * @return 分页对象
     */
    public Page<?> toPage(OrderItem... items) {
        Page<?> page = Page.of(pageNum, pageSize);
        if (StrUtil.isNotBlank(sortField)) {
            boolean asc = "asc".equalsIgnoreCase(sortOrder);
            page.addOrder(asc ? OrderItem.asc(sortField) : OrderItem.desc(sortField));
        } else if (items != null) {
            page.addOrder(items);
        }
        return page;
    }

    /**
     * 转换为分页对象
     *
     * @param column 默认排序字段
     * @param asc    是否升序
     * @return 分页对象
     */
    public Page<?> toPage(String column, boolean asc) {
        return toPage(new OrderItem().setColumn(column).setAsc(asc));
    }
}
