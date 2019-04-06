package com.yanghui.study.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Semaphore 信号量
 * AQS共享模式(shared mode)的使用
 * 类似于一个资源池（可以类比线程池），每个线程需要调用 acquire() 方法获取资源，
 * 然后才能执行，执行完后，需要 release 资源，让给其他的线程用
 *
 * 源码解析：
 * 创建 Semaphore 实例的时候，需要一个参数 permits，设置给 AQS 的 state ，
 * 然后每个线程调用 acquire 的时候，CAS 执行 state = state - 1，release 的时候执行 state = state + 1，
 * 当然，acquire 的时候，如果 state = 0，说明没有资源了，需要等待其他线程 release
 */
@Slf4j
public class SemaphoreExample {

    public void runDemo(){
        Semaphore semaphore = new Semaphore(10);
        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(new Thread(()->{
            semaphore.acquireUninterruptibly(5);
            log.info(Thread.currentThread().getName()+"线程 获取到 5个permit");
            try{
                log.info("剩余 "+semaphore.availablePermits()+" 个permit");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
            }finally {
                log.info(Thread.currentThread().getName()+"线程 释放 5个permit");
                semaphore.release(5);
            }
        }));
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        semaphore.acquireUninterruptibly(6);
        log.info(Thread.currentThread().getName()+"线程 获取到 6个permit");
        log.info("剩余 "+semaphore.availablePermits()+" 个permit");
        pool.execute(new Thread(()->{
            log.info(Thread.currentThread().getName()+"线程 尝试获取 5个permit");
            semaphore.acquireUninterruptibly(5);
            log.info(Thread.currentThread().getName()+"线程 获取到 5个permit");
            try{
                log.info("剩余 "+semaphore.availablePermits()+" 个permit");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
            }finally {
                log.info(Thread.currentThread().getName()+"线程 释放 5个permit");
                semaphore.release(5);
            }
        }));
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
        semaphore.release(1);
        log.info(Thread.currentThread().getName()+"线程 释放了 1个permit");
        pool.shutdown();
        while (!pool.isTerminated()){
            Thread.yield();
        }
    }
}
