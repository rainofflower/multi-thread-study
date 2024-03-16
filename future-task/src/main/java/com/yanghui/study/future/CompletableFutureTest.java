package com.yanghui.study.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * CompletableFuture的使用
 * CompletableFuture默认使用 ForkJoinPool 类里的线程池 static final ForkJoinPool common ，该线程池是static修饰的，属于类属性，
 * jvm加载该类的时候会初始化一个实例，并将类属性common指向该实例。一个jvm进程里CompletableFuture默认都使用该线程池实例
 *
 * ForkJoinPool里的ForkJoinWorkerThread会被设置为Daemon模式-》wt.setDaemon(true);见：
 * java.util.concurrent.ForkJoinPool#registerWorker(java.util.concurrent.ForkJoinWorkerThread)
 * 因此jvm进程可以不等默认的ForkJoinPool里的线程关闭就可以结束
 */
@Slf4j
public class CompletableFutureTest {

    @Test
    public void test(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                log.info("start exec task1");
                String s =  "task1";
                return s;
            }
        }).thenApplyAsync((param) -> {
            log.info("start exec task2");
            return param + "-task2";
        }).thenCombineAsync(CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                log.info("start exec task3");
                String s = "-task3";
                return s;
            }
        }), new BiFunction<String, String, String>() {
            @Override
            public String apply(String f1, String f2) {
                return f1 + f2 + "完成后回调";
            }
        });
        future.thenAcceptAsync((res)->{
            log.info(res);
        });
//        Thread thread = Thread.currentThread();
//        log.info(""+thread.isDaemon());
    }

}
