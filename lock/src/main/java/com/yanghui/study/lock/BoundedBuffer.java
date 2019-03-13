package com.yanghui.study.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Condition的使用示例</p>
 * 摘自Doug Lea在 {@link java.util.concurrent.locks.Condition} 接口注释中的代码
 */
class BoundedBuffer {
    final Lock lock = new ReentrantLock();
    //condition依赖于lock产生
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
  
    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    /**
     * 生产
     * @param x
     * @throws InterruptedException
     */
    public void put(Object x) throws InterruptedException {
       lock.lock();
       try {
           while (count == items.length)
               //队列已满，等待队列not full才能继续生产
               notFull.await();
           items[putptr] = x;
           if (++putptr == items.length) putptr = 0;
           ++count;
           //生产成功，队列已经not empty了，发个通知出去
           notEmpty.signal();
       } finally {
           lock.unlock();
       }
    }

    /**
     * 消费
     * @return
     * @throws InterruptedException
     */
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                //队列为空，等待队列not empty,才能继续消费
                notEmpty.await();
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0;
            --count;
            //被我消费掉一个，队列not full了，发个通知出去
            notFull.signal();
            return x;
       } finally {
            lock.unlock();
       }
    }
}