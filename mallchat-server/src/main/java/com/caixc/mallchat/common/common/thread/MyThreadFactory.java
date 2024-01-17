package com.caixc.mallchat.common.common.thread;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName MyThreadFactory.java
 * @Description 自定义处理  打印线程里面的日志
 * @createTime 2024年01月17日 17:20:00
 */
@Slf4j
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {
    private final ThreadFactory threadFactory;
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = threadFactory.newThread(r);
        thread.setUncaughtExceptionHandler(MyUncaughtExceptionHandler.getInstance());
        return thread;
    }
}
