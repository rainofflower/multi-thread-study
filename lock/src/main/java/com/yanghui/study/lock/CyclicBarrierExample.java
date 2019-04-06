package com.yanghui.study.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * CyclicBarrier 可重复使用的栅栏
 *
 * 源码解析：
 * CyclicBarrier 的源码实现和 CountDownLatch 大相径庭，
 * CountDownLatch 基于 AQS 的共享模式的使用，而 CyclicBarrier 基于 Condition 来实现。
 *
 * 线程调用 await方法将count减 1，如果count不为 0，调用condition.await()将当前线程封装成AQS里的Node进入条件队列，
 * 当count为 0 时，调用condition.signalAll()唤醒所有在栅栏上等待的线程，并更新 count 的值（还原），
 * 同时重新生成“新一代”generation = new Generation()
 */
@Slf4j
public class CyclicBarrierExample {

    public void runDemo(){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Posthandler());
        ExecutorService pool = Executors.newCachedThreadPool();
        for(int i = 0; i<3; i++){
            pool.execute(new Thread(()->{
                log.info(Thread.currentThread().getName()+"线程 执行中");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    //
                }
                log.info(Thread.currentThread().getName()+"线程 通过第一道栅栏继续工作...");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
                log.info(Thread.currentThread().getName()+"线程 再次被栅栏挡住");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                log.info(Thread.currentThread().getName()+"线程 通过第二道栅栏继续工作...");
            }));
        }
        log.info("主线程 执行中");
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        log.info("主线程 通过第一道栅栏继续工作...");
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
        log.info("主线程 再次被栅栏挡住");
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        log.info("主线程 通过第二道栅栏继续工作...");
    }

    class Posthandler implements Runnable{
        @Override
        public void run() {
            log.info("后置处理工作");
        }
    }
}
