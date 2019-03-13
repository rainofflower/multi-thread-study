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
            System.out.println("线程1进入休眠...");
            long start = System.currentTimeMillis();
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
            //LockSupport.park();
            System.out.println("线程1停止休眠，休眠时间："+(System.currentTimeMillis() - start));
        });
        t1.start();
        LockSupport.parkUntil(System.currentTimeMillis()+3000);
        //LockSupport.unpark(t1);
        t1.interrupt();
        t1.join();
        System.out.println("所有线程结束消耗时间："+(System.currentTimeMillis() - begin));
    }

    @Test
    public void test03() throws InterruptedException {
        ThreadInterrupt threadInterrupt = new ThreadInterrupt();
        threadInterrupt.waitAndInterrupt();
    }
}
