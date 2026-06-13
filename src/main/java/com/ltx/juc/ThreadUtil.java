package com.ltx.juc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author tianxing
 */
@Component
@RequiredArgsConstructor
public class ThreadUtil {

    private final ThreadPoolExecutor threadPoolExecutor;

    /**
     * 无返回值
     *
     * @param runnable {@link Runnable}
     */
    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    /**
     * 有返回值
     *
     * @param callable {@link Callable}
     * @return 计算结果
     */
    @SneakyThrows
    public <T> T submit(Callable<T> callable) {
        return threadPoolExecutor.submit(callable).get();
    }
}
