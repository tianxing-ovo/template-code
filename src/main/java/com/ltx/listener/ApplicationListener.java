package com.ltx.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 应用程序监听器
 *
 * @author tianxing
 */
@Slf4j
@Component
public class ApplicationListener {

    /**
     * ApplicationContext被初始化或刷新时触发
     *
     * @param contextRefreshedEvent 上下文刷新事件
     */
    @EventListener(ContextRefreshedEvent.class)
    public void handleContextRefreshEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("{}", contextRefreshedEvent);
        log.info("Application context refreshed!");
    }
}
