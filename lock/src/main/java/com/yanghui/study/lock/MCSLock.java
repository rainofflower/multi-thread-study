package com.yanghui.study.lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * MCS锁
 *
 * 基于链表的可扩展、高性能、公平的自旋锁
 *
 * 申请线程只在本地变量上自旋，
 * 直接前驱负责通知其结束自旋，
 * 从而极大地减少了不必要的处理器缓存同步的次数，降低了总线和内存的开销
 */
public class MCSLock {

    public static class MCSNode{
        volatile MCSNode next;
        //默认在等待锁
        volatile boolean isBlock = true;
    }

    //指向最后一个申请锁的MCSNode
    volatile MCSNode queue;

    private static final AtomicReferenceFieldUpdater<MCSLock, MCSNode> UPDATE = AtomicReferenceFieldUpdater.newUpdater(MCSLock.class, MCSNode.class, "queue");

    public void lock(MCSNode currentThread){
        //获取前驱，并将queue指向当前申请锁的MSCNode
        MCSNode predecessor = UPDATE.getAndSet(this, currentThread);
        if(predecessor != null){
            //设置前驱的后继
            predecessor.next = currentThread;
            while(currentThread.isBlock);
        }/*else{
            currentThread.isBlock = false;
        }*/
    }

    public void unlock(MCSNode currentThread){
        /*if(currentThread.isBlock){
            return;
        }*/
        if(currentThread.next == null){
            //检查是否有线程排在自己后面
            if(UPDATE.compareAndSet(this, currentThread, null)){
                //CAS成功说明确实没有线程排在自己后面
                return;
            }else{
                //CAS失败说明突然有线程排在自己后面，可能还不知道是哪个线程，以下为等待后续者
                //此处之所以要忙等，是因为后面的线程执行到了lock()方法的获取前驱，设置queue处，却还没有执行到设置前驱的后继处
                //unlock()方法后面还有设置当前Node后继的isBlock为false,所以必须等当前Node与后继关联完成之后再退出while循环
                while(currentThread.next == null);
            }
        }
        currentThread.next.isBlock = false;
        currentThread.next = null;
    }
}
