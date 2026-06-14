package com.ltx.entity.drools;

import lombok.Data;

/**
 * 订单类
 *
 * @author tianxing
 */
@Data
public class Order {
    // 金额
    private Integer amount;
    // 积分
    private Integer score;
}
