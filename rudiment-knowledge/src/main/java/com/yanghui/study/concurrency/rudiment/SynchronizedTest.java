package com.yanghui.study.concurrency.rudiment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SynchronizedTest {

    private static final int count = 100000;
    private static String currentMethod = "";

    public synchronized static void add1(){
        String name = Thread.currentThread().getName();
        currentMethod = name;
        log.info(name+" 线程执行中...");
        for(int i = 0; i<count;i++){
            if(!currentMethod.equals(name)){
                log.info(SynchronizedTest.class+"实例未同步！");
            }
        }
    }

    public synchronized static void add2(){
        String name = Thread.currentThread().getName();
        currentMethod = name;
        log.info(name+" 线程执行中...");
        for(int i = 0; i<count;i++){
            if(!currentMethod.equals(name)){
                log.info(SynchronizedTest.class+"实例未同步！");
            }
        }
    }

    public class InnerClass{
        private final int a = 10;
        private String currentName = "";

        public synchronized void method(){
            String name = Thread.currentThread().getName();
            currentMethod = name;
            log.info(name+" 线程执行中...");
            for(int i = 0; i<a;i++){
                if(!currentMethod.equals(name)){
                    log.info("方法未同步！");
                }
            }
        }
    }
}
