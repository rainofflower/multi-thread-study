package com.yanghui.study.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 排队自旋锁
 * 实现原理类似于现实中银行柜台的排队叫号
 *
 * 解决SpinLock公平性问题
 * 缺点：
 * 多处理器系统上，每个进程/线程占用的处理器都在读写同一个变量serviceNum ，
 * 每次读写操作都必须在多个处理器缓存之间进行缓存同步，这会导致繁重的系统总线和内存的流量，大大降低系统整体的性能
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
