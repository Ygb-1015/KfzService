package com.order.main.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RetryUtils {

    public static <T> T executeWithRetry(Callable<T> task, int maxRetries, long initialDelay) {
        int retryCount = 0;
        long delay = initialDelay;

        while (retryCount <= maxRetries) {
            try {
                return task.call();
            } catch (Exception e) {
                retryCount++;
                if (retryCount > maxRetries) {
                    log.error("达到最大重试次数 {}，操作失败", maxRetries, e);
                    throw new RuntimeException("操作失败，已达最大重试次数", e);
                }

                log.warn("第 {} 次重试失败，{}ms后重试。异常: {}", retryCount, delay, e.getMessage());

                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("重试被中断", ie);
                }

                // 指数退避
                delay *= 2;
            }
        }

        throw new RuntimeException("未知错误");
    }
}
