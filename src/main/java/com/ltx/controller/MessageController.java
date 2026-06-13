package com.ltx.controller;

import com.ltx.entity.Result;
import com.ltx.mq.rabbitmq.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息控制器
 *
 * @author tianxing
 */
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageSender messageSender;

    /**
     * 发送消息
     *
     * @return 通用响应对象
     */
    @PostMapping("/messages")
    public Result sendMessage() {
        messageSender.sendMessage("topic.exchange", "queue", "Hello RabbitMQ");
        return Result.success();
    }
}
