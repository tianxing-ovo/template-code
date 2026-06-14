package com.ltx.entity.drools;

import lombok.Data;

import java.util.List;

/**
 * 客户类
 *
 * @author tianxing
 */
@Data
public class Customer {
    private String name;
    // 订单列表
    private List<Order> orderList;
}
