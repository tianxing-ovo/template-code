package com.ltx.sharding;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ltx.entity.po.Message;
import com.ltx.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 水平分片测试
 *
 * @author tianxing
 */
@Slf4j
@SpringBootTest(args = "--sharding.mode=horizontal")
public class HorizontalShardingTest {

    private final MessageMapper messageMapper;

    @Autowired
    public HorizontalShardingTest(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Test
    public void testReadAll() {
        // 路由到所有表
        List<Message> messages = messageMapper.selectList(null);
        messages.forEach(message -> log.info("{}", message));
    }

    @Test
    public void testInsert() {
        // 路由到所有表
        int deletedCount = messageMapper.delete(Wrappers.lambdaQuery());
        log.info("已清空 {} 条消息", deletedCount);
        List<Message> messages = List.of(
                new Message("订单发货通知", "订单202606180001已发货"),
                new Message("系统维护公告", "6月20日02点到04点维护"),
                new Message("账户安全提醒", "深圳新设备登录"),
                new Message("优惠券到账", "满100减20已到账"),
                new Message("工单回复", "导出问题已处理"),
                new Message("欢迎注册", "欢迎注册template-code")
        );
        // id % 4 决定路由到哪个表
        messages.forEach(message -> {
            messageMapper.insert(message);
            log.info("插入成功, id={}", message.getId());
        });
    }
}
