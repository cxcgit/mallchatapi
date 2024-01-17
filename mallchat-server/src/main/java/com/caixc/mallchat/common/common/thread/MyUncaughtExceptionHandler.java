package com.caixc.mallchat.common.common.thread;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Getter
    private static final MyUncaughtExceptionHandler instance = new MyUncaughtExceptionHandler();

    private MyUncaughtExceptionHandler() {
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread {} ", t.getName(), e);
    }

}
