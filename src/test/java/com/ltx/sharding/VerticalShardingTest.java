package com.ltx.sharding;

import com.ltx.entity.po.Orders;
import com.ltx.entity.po.User;
import com.ltx.mapper.OrdersMapper;
import com.ltx.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ltx.enums.Role;
import com.ltx.enums.Sex;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 垂直分库测试
 *
 * @author tianxing
 */
@Slf4j
@ActiveProfiles("sharding")
@SpringBootTest(args = "--sharding.mode=vertical")
public class VerticalShardingTest {

    private final UserMapper userMapper;
    private final OrdersMapper ordersMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public VerticalShardingTest(UserMapper userMapper, OrdersMapper ordersMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.ordersMapper = ordersMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void testReadBoth() {
        // 路由到ds_user
        List<User> users = userMapper.selectList(null);
        users.forEach(user -> log.info("{}", user));
        // 路由到ds_order
        List<Orders> orders = ordersMapper.selectList(null);
        orders.forEach(order -> log.info("{}", order));
    }

    @Test
    public void testInsertBoth() {
        // 路由到ds_user
        User user = new User();
        user.setName("王五");
        user.setUsername("user");
        user.setAge(28);
        user.setSex(Sex.MAN);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setHobbies(List.of("篮球", "音乐"));
        user.setProvince("上海市");
        user.setCity("上海");
        user.setAddress("上海市浦东新区陆家嘴环路100号");
        user.setDescription("垂直分库测试用户");
        user.setRole(Role.USER);
        userMapper.insert(user);
        log.info("插入用户成功, id={}", user.getId());
        // 路由到ds_order
        Orders order = new Orders();
        order.setOrderNo("ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        order.setProductName("垂直分库测试商品");
        order.setPrice(new BigDecimal("199.00"));
        order.setQuantity(2);
        order.setTotalAmount(new BigDecimal("398.00"));
        order.setStatus(0);
        ordersMapper.insert(order);
        log.info("插入订单成功, id={}", order.getId());
        // 验证插入结果
        log.info("用户数: {}, 订单数: {}", userMapper.selectList(null).size(), ordersMapper.selectList(null).size());
    }
}
