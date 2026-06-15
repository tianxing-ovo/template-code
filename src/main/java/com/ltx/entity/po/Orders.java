package com.ltx.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 *
 * @author tianxing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("orders")
public class Orders {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private String productName;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalAmount;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}