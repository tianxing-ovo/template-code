package com.ltx.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ltx.entity.po.User;
import com.ltx.enums.Role;
import com.ltx.enums.Sex;
import com.ltx.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 如果有多个CommandLineRunner接口实现类 -> 使用@Order指定执行顺序
 *
 * @author tianxing
 */
@Component
@Order(1)
@ConditionalOnExpression("!'horizontal'.equals('${sharding.mode:rw}')")
@RequiredArgsConstructor
@Slf4j
public class StartupRunner implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("开始检查初始数据...");
        // 检查数据库中是否存在用户
        Long count = userMapper.selectCount(new LambdaQueryWrapper<>());
        if (count == 0) {
            log.info("正在自动创建默认管理员用户...");
            User admin = new User();
            admin.setName("系统管理员");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole(Role.ADMIN);
            admin.setSex(Sex.MAN);
            admin.setAge(25);
            admin.setProvince("广东省");
            admin.setCity("深圳市");
            admin.setAddress("南山区科兴科学园");
            admin.setDescription("系统初始化的默认超级管理员账号");
            admin.setHobbies(Collections.singletonList("阅读"));
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);
            admin.setEnabled(true);
            userMapper.insert(admin);
            log.info("【用户名 = admin, 密码 = 123456】");
        } else {
            log.info("数据库已存在用户数据");
        }
    }
}
