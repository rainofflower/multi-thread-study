package com.yanghui.study.thread.jdk;

import com.yanghui.study.thread.jdk.interrept.ThreadInterrupt;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ThreadTest {

    @Test
    public void testInterrupt() throws InterruptedException {
        ThreadInterrupt threadInterrupt = new ThreadInterrupt();
        threadInterrupt.threadParkAndInterrupt();
    }

    @Test
    public void testLockSupport() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread t1 = new Thread(()->{
            long start = System.currentTimeMillis();
//            System.out.println("线程1调用sleep(2000)...");
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("线程1退出sleep()...");
            System.out.println("线程1调用park()...");
            //LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            LockSupport.park();
            System.out.println("线程1停止休眠，休眠时间："+(System.currentTimeMillis() - start));
        });
        t1.start();
        LockSupport.parkUntil(System.currentTimeMillis()+3000);
        LockSupport.unpark(t1);
        System.out.println("主线程给了线程1 permit...");
        //t1.interrupt();
        t1.join();
        System.out.println("所有线程结束消耗时间："+(System.currentTimeMillis() - begin));
    }

    @Test
    public void test03() throws InterruptedException {
        ThreadInterrupt threadInterrupt = new ThreadInterrupt();
        threadInterrupt.waitAndInterrupt();
    }
}
