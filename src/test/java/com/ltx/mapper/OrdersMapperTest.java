package com.ltx.mapper;

import com.ltx.entity.po.Orders;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 订单Mapper测试
 *
 * @author tianxing
 */
@Slf4j
@SpringBootTest
public class OrdersMapperTest {

    private final OrdersMapper ordersMapper;

    @Autowired
    public OrdersMapperTest(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    @Test
    public void testInsert() {
        // 路由到ds-master
        doInsert();
    }

    @Test
    public void testRead() {
        // 路由到ds-slave1和ds-slave2
        doRead();
    }

    @Test
    @Transactional
    public void testTransaction() {
        // 事务内所有操作都路由到ds-master
        doInsert();
        doRead();
        // 默认会执行回滚
        log.info("执行回滚");
    }

    private void doInsert() {
        Orders order = new Orders();
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        order.setOrderNo("ORD" + timeStr);
        order.setProductName("读写分离测试商品");
        order.setPrice(new BigDecimal("99.99"));
        order.setQuantity(1);
        order.setTotalAmount(new BigDecimal("99.99"));
        order.setStatus(0);
        int result = ordersMapper.insert(order);
        log.info("插入结果: {}, 订单ID: {}", result, order.getId());
    }

    private void doRead() {
        List<Orders> list = ordersMapper.selectList(null);
        log.info("查询到订单: {}", list);
    }
}