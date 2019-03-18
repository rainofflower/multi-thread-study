package com.yanghui.study.thread.jdk.method;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class CommonMethod {

    /**
     * join方法和sleep方法对interrupt的感知与处理
     * @throws InterruptedException
     */
    public void interruptAndJoinAndSleep() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("线程1：执行中");
            System.out.println("线程1: 准备休眠10秒");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
            if(Thread.currentThread().isInterrupted()){
                System.out.println("线程1： 被中断...");
            }
            else{
                System.out.println("线程1： 正常退出");
            }
        });
        t1.start();
        Thread t2 = new Thread(() ->{
            try {
                System.out.println("线程2：执行中");
//                System.out.println("线程2： 等待线程1 6秒");
//                t1.join(6000);
                System.out.println("线程2： 休眠6秒");
                Thread.sleep(6000);
                System.out.println("线程2： 正常退出");
            } catch (InterruptedException e) {
                /**
                 * join()方法抛出InterruptedException会重置中断状态
                 */
                //System.out.println("线程2： 执行t1.join()抛出InterruptedException异常。此时线程2中断状态为："+Thread.currentThread().isInterrupted());
                /**
                 * sleep()方法抛出InterruptedException会重置中断状态
                 */
                System.out.println("线程2： 执行Thread.sleep()抛出InterruptedException异常。此时线程2中断状态为："+Thread.currentThread().isInterrupted());
            }
        });
        t2.start();
        System.out.println("主线程 休眠3秒...");
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
        //t1.interrupt();
        /**
         * 调用join()方法的线程被中断会抛出InterruptedException异常
         */
        System.out.println("主线程 中断线程2");
        t2.interrupt();
        t1.join();
        t2.join();
    }

}
