package com.yanghui.study.lock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    @Test
    public void test01() throws InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        Counter counter = new Counter();
        ReentrantLock reentrantLock = new ReentrantLock();
        SpinLock spinLock = new SpinLock();
        TicketLock ticketLock = new TicketLock();
        MCSLock mcsLock = new MCSLock();
        CLHLock clhLock = new CLHLock();
        long start = System.currentTimeMillis();
        for (int i = 0; i<100; i++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //reentrantLock.lock();
                    //spinLock.lock();
                    //ticketLock.lock();
                    //MCSLock.MCSNode mcsNode = new MCSLock.MCSNode();
                    //mcsLock.lock(mcsNode);
                    CLHLock.CLHNode clhNode = new CLHLock.CLHNode();
                    clhLock.lock(clhNode);
                    try{
                        for (int j = 0; j<10000; j++){
                            counter.increment();
                            //counter.synIncrement();
                        }
                    }finally {
                        //reentrantLock.unlock();
                        //spinLock.unlock();
                        //ticketLock.unlock();
                        //mcsLock.unlock(mcsNode);
                        clhLock.unlock(clhNode);
                    }
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
}
