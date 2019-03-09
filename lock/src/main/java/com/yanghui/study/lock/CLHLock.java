package com.yanghui.study.lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * CLHLock
 *
 * 基于链表的可扩展、高性能、公平的自旋锁
 *
 * 申请线程只在本地变量上自旋，它不断轮询前驱的状态，如果发现前驱释放了锁就结束自旋
 */
public class CLHLock {

    public static class CLHNode{
        //默认是在等待锁
        private volatile boolean isBlock = true;
    }

    private volatile CLHNode tail;

    private static final AtomicReferenceFieldUpdater<CLHLock, CLHNode> UPDATE = AtomicReferenceFieldUpdater.newUpdater(CLHLock.class, CLHNode.class, "tail");

    public void lock(CLHNode currentThread){
        CLHNode predecessor = UPDATE.getAndSet(this, currentThread);
        if(predecessor != null){
            //已有线程占用了锁，进入自旋
            while(predecessor.isBlock);
        }
    }

    public void unlock(CLHNode currentThread){
        /**
         * 如果队列里只有当前线程，则释放对当前线程的引用（for GC）
         */
        if(!UPDATE.compareAndSet(this, currentThread, null)){
            //改变状态，让后续线程结束自旋
            currentThread.isBlock = false;
        }
    }
}
