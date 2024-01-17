package com.caixc.mallchat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName TheardTest.java
 * @Description TODO
 * @createTime 2024年01月03日 16:58:00
 */
public class ThreadTest {

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    public static class MyThread extends Thread {

        private Integer i;

        @Override
        public void run() {
            while (i<10){
                System.out.println("thread"+i);
                i++;
            }
        }
    }

    public static class RunThread implements Runnable{

        @Override
        public void run() {
            System.out.println("我是实现Runnable的线程");
        }
    }

    public static class CallableThread implements Callable<String>{

        @Override
        public String call() throws Exception {
            return "我是实现Callable的线程";
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        int i = 0;
//        Thread thread1 = new MyThread(i);
//        thread1.start();
//
//        int j = 0;
//        Thread thread2 = new MyThread(j);
//        thread2.start();
//
//        RunThread runThread = new RunThread();
//        new Thread(runThread).start();
//        new Thread(runThread).start();
//
//        CallableThread callableThread = new CallableThread();
//        FutureTask<String> futureTask = new FutureTask<>(callableThread);
//        // 托管给线程处理
//        new Thread(futureTask).start();
//        // 托管给线程池
//        Executors.newCachedThreadPool().submit(futureTask);
//        try {
//            String string = futureTask.get();
//            System.out.println(string);
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }

        ExecutorService service = Executors.newFixedThreadPool(3);
        final CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("子线程" + Thread.currentThread().getName() + "开始执行");
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("子线程"+Thread.currentThread().getName()+"执行完成");
                        latch.countDown();//当前线程调用此方法，则计数减一
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            service.execute(runnable);
        }

        try {
            System.out.println("主线程"+Thread.currentThread().getName()+"等待子线程执行完成...");
            latch.await();//阻塞当前线程，直到计数器的值为0
            System.out.println("主线程"+Thread.currentThread().getName()+"开始执行...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
