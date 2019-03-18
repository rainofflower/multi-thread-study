package com.yanghui.study.thread.jdk;

import com.yanghui.study.thread.jdk.interrept.ThreadInterrupt;
import com.yanghui.study.thread.jdk.method.CommonMethod;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ThreadTest {

    @Test
    public void testInterrupt() throws InterruptedException {
        ThreadInterrupt threadInterrupt = new ThreadInterrupt();
        threadInterrupt.threadParkAndInterrupt();
    }

    /**
     * 该例子可以发现线程被中断相当于对该线程调用了LockSupport.unpark(thread),
     * 不管被中断线程是在中断前调用LockSupport.park()还是之后，
     * 只要被中断过，该线程就有 permit.
     * 所以当线程被中断后再调用LockSupport.park(),线程不会被挂起，
     * 即使此时线程中断状态已被重置为false !!!
     * @throws InterruptedException
     */
    @Test
    public void testLockSupport() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread t1 = new Thread(()->{
//            synchronized (Object.class){
//                try {
//                    System.out.println("线程1 中断状态："+Thread.currentThread().isInterrupted());
//                    System.out.println("线程1 wait...");
//                    Object.class.wait();
//                } catch (InterruptedException e) {
//                    //
//                }
//            }
            System.out.println("线程1 等待被中断...");
            while(!Thread.currentThread().isInterrupted()){

            }
            //wait返回之后中断状态会被重置
            System.out.println("线程1 中断状态："+Thread.currentThread().isInterrupted());
            //再次重置中断状态
            Thread.interrupted();
            System.out.println("线程1 中断状态："+Thread.currentThread().isInterrupted());
            System.out.println("线程1 调用park()...");
            //LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            long start = System.currentTimeMillis();
            LockSupport.park();
            System.out.println("线程1 停止休眠，休眠时间："+(System.currentTimeMillis() - start));
        });
        t1.start();
        LockSupport.parkUntil(System.currentTimeMillis()+3000);
        //System.out.println("主线程给了线程1 permit...");
        System.out.println("主线程 中断线程1...");
        t1.interrupt();
        t1.join();
        System.out.println("所有线程结束消耗时间："+(System.currentTimeMillis() - begin));
    }

    @Test
    public void test03() throws InterruptedException {
        ThreadInterrupt threadInterrupt = new ThreadInterrupt();
        threadInterrupt.waitAndInterrupt();
    }

    @Test
    public void test04() throws InterruptedException {
        CommonMethod commonMethod = new CommonMethod();
        commonMethod.interruptAndJoinAndSleep();
    }
}
