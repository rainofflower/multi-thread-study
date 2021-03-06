package com.yanghui.study.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁的实现
 *
 * 自旋锁是指当一个线程尝试获取某个锁时，
 * 如果该锁已被其他线程占用，就一直循环检测锁是否被释放，而不是进入线程挂起或睡眠状态
 *
 * 适用于锁保护的临界区很小的情况
 * 缺点：
 * 保证各个CPU的缓存（L1、L2、L3、跨CPU Socket、主存）的数据一致性，通讯开销很大，在多处理器系统上更严重；
 * 没法保证公平性，不保证等待进程/线程按照FIFO（first input first output）顺序获得锁。
 */
public class SpinLock {

    private AtomicReference<Thread> owner = new AtomicReference<>();

    public void lock(){
        Thread currentThread = Thread.currentThread();
        //自旋将当前线程设置为锁拥有者
        while(!owner.compareAndSet(null, currentThread));
    }

    public void unlock(){
        Thread currentThread = Thread.currentThread();
        //锁拥有者释放锁
        owner.compareAndSet(currentThread, null);
    }
}
