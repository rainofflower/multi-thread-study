package com.yanghui.study.concurrency.test;

import com.yanghui.study.concurrency.rudiment.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class RudimentTest {

    @Test
    public void test1() throws InterruptedException {
        ConcurrencyTest.concurrency();
        ConcurrencyTest.serial();
    }

    @Test
    public void test2() throws InterruptedException {
        DeadLockDemo.deadLock();
    }

    @Test
    public void test3() throws InterruptedException {
        SynchronizedTest.add1();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedTest.add1();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedTest.add2();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void test4() throws InterruptedException {
        SynchronizedTest s = new SynchronizedTest();
        SynchronizedTest.InnerClass innerClass1 = s.new InnerClass();
        SynchronizedTest.InnerClass innerClass2 = s.new InnerClass();
        SynchronizedTest.InnerClass innerClass3 = s.new InnerClass();
        synchronized (innerClass1){
            innerClass3.method();
        }
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (innerClass1){
                    innerClass1.method();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (innerClass1){
                    innerClass2.method();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void test5(){
        //ValueReference.basicDataTransfer();
        ValueReference.referenceTransfer();
    }

    @Test
    public void test6(){
        ValueReference.test();
    }

    @Test
    public void test7(){
        /**
         * UnsatisfiedLinkError
         */
        //new MemoryModel().jni();
        new MemoryModel().thread1();
    }

    @Test
    public void test8() throws InterruptedException {
        Thread t1 = new Thread(()->{
            MemoryModel.writer();
        });
        Thread t2 = new Thread(() ->{
            MemoryModel.reader();
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    /**
     * 使用LockSupport工具类实现多线程同步（线程间精确同步）
     */
    @Test
    public void test9() {
        ExecutorService pool = Executors.newCachedThreadPool();
        Map<String,Thread> map = new ConcurrentHashMap<>();
        /**
         * 假设t1依赖于t3,t2依赖t3,t4依赖t1
         */
        Thread t1 = new Thread(()->{
            map.put("t1",Thread.currentThread());
            log.info("t1 启动...");
            LockSupport.park();
            log.info("t1 开始执行...");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            log.info("t1 执行完成...");
            LockSupport.unpark(map.get("t4"));
        });
        Thread t2 = new Thread(() ->{
            map.put("t2",Thread.currentThread());
            log.info("t2 启动...");
            LockSupport.park();
            log.info("t2 开始执行...");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            log.info("t2 执行完成...");
            //LockSupport.unpark(map.get("t3"));
        });
        Thread t3 = new Thread(() ->{
            map.put("t3",Thread.currentThread());
            log.info("t3 启动...");
            //LockSupport.park();
            log.info("t3 开始执行...");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            log.info("t3 执行完成...");
            LockSupport.unpark(map.get("t1"));
            LockSupport.unpark(map.get("t2"));
        });
        Thread t4 = new Thread(() ->{
            map.put("t4",Thread.currentThread());
            log.info("t4 启动...");
            LockSupport.park();
            log.info("t4 开始执行...");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            log.info("t4 执行完成...");
        });
        pool.submit(t4);
        pool.submit(t1);
        pool.submit(t2);
        //LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        pool.submit(t3);
        pool.shutdown();
        while(!pool.isTerminated()){
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        }
    }
}
