package com.ltx.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;


/**
 * RabbitMQ配置
 *
 * @author tianxing
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 使用JSON序列化机制进行消息转换
     *
     * @return 消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 直接交换机(Direct Exchange): 将消息路由到与消息的routing key完全匹配的队列
     * 扇出交换机(Fanout Exchange): 将消息广播到与该交换机绑定的所有队列
     * 主题交换机(Topic Exchange): 允许队列通过通配符匹配的方式接收消息 -> *匹配一个单词 -> #匹配零个或多个单词
     *
     * @return 交换机
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.exchange", true, false);
    }

    /**
     * name: 队列的名称
     * durable: 队列是否是持久化的
     * exclusive: 队列是否是独占的,当设置为true时,只有创建队列的连接可以访问该队列
     * autoDelete: 队列在没有活动消费者时是否自动删除
     * arguments: 额外的配置选项
     *
     * @return 队列
     */
    @Bean
    public Queue queue() {
        return new Queue("queue", true, false, false);
    }


    /**
     * 延迟队列
     *
     * @return 延迟队列
     */
    @Bean
    public Queue delayQueue() {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topic.exchange");
        arguments.put("x-dead-letter-routing-key", "delay.queue");
        // 消息过期时间: 2分钟
        arguments.put("x-message-ttl", 120000);
        return new Queue("delay.queue", true, false, false, arguments);
    }


    /**
     * 交换机与队列绑定
     *
     * @return 绑定
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("queue.#");
    }

    /**
     * 交换机与延迟队列绑定
     *
     * @return 绑定
     */
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(topicExchange()).with("delay.queue.#");
    }
}
