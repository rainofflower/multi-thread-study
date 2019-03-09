package com.yanghui.study.lock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LockTest {

    @Test
    public void test01() throws InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        TicketLock ticketLock = new TicketLock();
        SpinLock spinLock = new SpinLock();
        Counter counter = new Counter();
        long start = System.currentTimeMillis();
        for (int i = 0; i<100; i++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //spinLock.lock();
                    ticketLock.lock();
                    try{
                        for (int j = 0; j<1000; j++){
                            counter.increment();
                            //counter.synIncrement();
                        }
                    }finally {
                        //spinLock.unlock();
                        ticketLock.unlock();
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