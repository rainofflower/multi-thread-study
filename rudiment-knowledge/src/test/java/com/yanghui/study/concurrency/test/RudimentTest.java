package com.yanghui.study.concurrency.test;

import com.yanghui.study.concurrency.rudiment.ConcurrencyTest;
import com.yanghui.study.concurrency.rudiment.DeadLockDemo;
import com.yanghui.study.concurrency.rudiment.SynchronizedTest;
import org.junit.Test;

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
}
