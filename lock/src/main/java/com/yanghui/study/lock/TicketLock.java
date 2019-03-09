package com.yanghui.study.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 排队自旋锁
 */
public class TicketLock {

    private AtomicInteger ticket = new AtomicInteger();

    private AtomicInteger service = new AtomicInteger();

    public void lock(){
        int ticketNum = ticket.getAndIncrement();
        while(ticketNum != service.get());
    }

    public void unlock(){
        service.getAndIncrement();
    }
}
