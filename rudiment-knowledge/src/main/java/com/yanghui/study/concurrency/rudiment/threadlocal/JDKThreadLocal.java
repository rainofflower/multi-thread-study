package com.yanghui.study.concurrency.rudiment.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ${DESCRIPTION}
 *
 * @author yanghui
 * @date 2020-09-29 18:03
 **/
public class JDKThreadLocal {

    public static void main(String... args) throws InterruptedException {
        ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();
        Map<String, String> map = new HashMap<>();
        map.put("a","0000");
        threadLocal.set(map);
        System.out.println("线程："+Thread.currentThread().getName()+"："+threadLocal.get());
        Thread thread = new Thread(()->{
            ThreadLocal<Map<String, String>> threadLocal1 = new ThreadLocal<>();
            Map<String, String> map1 = new HashMap<>();
            map1.put("a","1111");
            map1.put("b","2222");
            threadLocal1.set(map1);
            System.out.println("线程："+Thread.currentThread().getName()+"："+threadLocal1.get());
        });
        thread.start();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        pool.execute(()->{
            ThreadLocal<Map<String, String>> threadLocal1 = new ThreadLocal<>();
            threadLocal1.set(map);
            System.out.println("线程："+Thread.currentThread().getName()+"："+threadLocal1.get());
        });
        pool.execute(()->{
            ThreadLocal<Map<String, String>> threadLocal1 = new ThreadLocal<>();
            Map<String, String> map1 = new HashMap<>();
            map1.put("a","cccc");
            map1.put("b","dddd");
            threadLocal1.set(map1);
            System.out.println("线程："+Thread.currentThread().getName()+"："+threadLocal1.get());
        });
        thread.join();
        pool.shutdown();
        while(!pool.isShutdown()){
            Thread.yield();
        }
    }
}
