package com.yanghui.study.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * CountDownLatch
 * latch 门栓、栅栏
 * AQS共享模式(shared mode)的使用
 *
 * 可将一个比较大的任务拆分到多个线程内执行，等所有任务执行完成，再往下做其他事
 *
 * 源码解析：
 * 对于 CountDownLatch，我们仅仅需要关心两个方法，一个是 countDown() 方法，另一个是 await() 方法。
 *
 * countDown() 方法每次调用都会将 AQS里的state 减 1，直到 state 的值为 0；
 * 而 await() 是一个阻塞方法，所有调用了 await 方法的线程阻塞在 AQS 的阻塞队列中，等待条件满足（state == 0），将线程从队列中一个个唤醒过来。
 */
@Slf4j
public class CountDownLatchExample {

    public void runDemo() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(8);

        log.info("运动员 在起跑线等待...");
        ExecutorService pool = Executors.newFixedThreadPool(8);
        for (int i = 1; i<=8; i++) {
            pool.execute(new WorkerRunnable(startSignal, doneSignal, i));
        }
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
        log.info("预备 3 2 1 Run");
        startSignal.countDown();
        log.info("观众 加油 加油...");
        doneSignal.await();
        log.info("比赛结束 进入颁奖仪式...");
    }

    class WorkerRunnable implements Runnable{

        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;
        private int i;

        WorkerRunnable(CountDownLatch startSignal, CountDownLatch doneSignal, int i){
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
            this.i = i;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                doWork(i);
                doneSignal.countDown();
            } catch (InterruptedException e) {
                //
            }
        }

        public void doWork(int i){
            log.info(Thread.currentThread().getName()+"运动员 跑起来了");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(i));
            log.info(Thread.currentThread().getName()+"运动员 冲过终点线");
        }
    }
}
