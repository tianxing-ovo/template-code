package com.ltx;

import org.apache.catalina.util.ServerInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Indexed;

import jakarta.annotation.PostConstruct;

/**
 * proxyTargetClass = true -> 使用cglib代理(继承)
 * proxyTargetClass = false -> 使用jdk代理(实现接口)
 * exposeProxy = true -> 公开代理 -> 使用AopContext.currentProxy()获取代理对象
 *
 * @author tianxing
 */
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication
@EnableRedisHttpSession
@EnableAsync
@EnableRabbit
@MapperScan("com.ltx.mapper")
@Indexed
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void printTomcatVersion() {
        System.out.println("Tomcat Version: " + ServerInfo.getServerInfo());
    }
}
