package com.yanghui.study.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class LockTest {

    @Test
    public void test01() throws InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        Counter counter = new Counter();
        Lock reentrantLock = new ReentrantLock();
        SpinLock spinLock = new SpinLock();
        TicketLock ticketLock = new TicketLock();
        MCSLock mcsLock = new MCSLock();
        CLHLock clhLock = new CLHLock();
        long start = System.currentTimeMillis();
        for (int i = 0; i<100; i++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    reentrantLock.lock();
                    //spinLock.lock();
                    //ticketLock.lock();
                    //MCSLock.MCSNode mcsNode = new MCSLock.MCSNode();
                    //mcsLock.lock(mcsNode);
                    //CLHLock.CLHNode clhNode = new CLHLock.CLHNode();
                    //clhLock.lock(clhNode);
                    //synchronized (counter){
                        try{
                            for (int j = 0; j<10000; j++){
                                counter.increment();
                                //counter.synIncrement();
                            }
                        }finally {
                            reentrantLock.unlock();
                            //spinLock.unlock();
                            //ticketLock.unlock();
                            //mcsLock.unlock(mcsNode);
                            //clhLock.unlock(clhNode);
                        }
                    //}
                }
            });
            threadList.add(t);
        }
        for (Thread t:threadList) {
            t.start();
        }
        for (Thread t: threadList) {
            t.join();
        }
        System.out.println("计算时长："+ (System.currentTimeMillis() - start) +
                "\n计算结果为："+counter.a);
    }

    @Test
    public void test02(){
        ExecutorService pool = Executors.newCachedThreadPool();
        ReentrantLockExample example = new ReentrantLockExample();
        pool.submit(new Thread(()-> log.info("读取到值： "+example.reader())));
        pool.submit(new Thread(()-> example.writer()));
        pool.submit(new Thread(()-> example.take()));
        pool.submit(new Thread(()-> example.writer()));
        pool.submit(new Thread(()-> log.info("读取到值： "+example.reader())));
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
        pool.submit(new Thread(()-> example.writer()));
        pool.submit(new Thread(()-> example.put()));
        pool.submit(new Thread(()-> log.info("读取到值： "+example.reader())));
        pool.shutdown();
        while(!pool.isTerminated()){
            Thread.yield();
        }
    }

    @Test
    public void test03() throws InterruptedException {
        CountDownLatchExample countDownLatchExample = new CountDownLatchExample();
        countDownLatchExample.runDemo();
    }

    @Test
    public void test04(){
        CyclicBarrierExample cyclicBarrierExample = new CyclicBarrierExample();
        cyclicBarrierExample.runDemo();
    }

    @Test
    public void test05(){
        SemaphoreExample semaphoreExample = new SemaphoreExample();
        semaphoreExample.runDemo();
    }
}
