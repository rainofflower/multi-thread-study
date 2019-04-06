package com.yanghui.study.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 可重入锁
 * AQS独占模式(exclusive mode)的使用
 * 1、排他锁
 * 2、条件使用
 *
 * 源码关键词：
 * sync queue 同步队列
 * condition queue 条件队列
 * transfer 转移（条件队列转移到同步队列）
 * 关键属性
 * state
 * Node.waitStatus
 * ...
 */
@Slf4j
public class ReentrantLockExample {

    int a = 0;
    final ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void writer(){
        lock.lock();
        try{
            a++;
        }finally {
            lock.unlock();
        }
    }

    public int reader(){
        lock.lock();
        try{
            int i = a;
            return i;
        }finally {
            lock.unlock();
        }
    }

    public void take() {
        lock.lock();
        try{
            log.info("消费线程 进入等待...");
            condition.await();
            log.info("消费线程 条件满足了，继续执行");
        } catch (InterruptedException e) {
            //
        } finally {
            lock.unlock();
        }
    }

    public void put(){
        lock.lock();
        try{
            log.info("生产线程 开始生产");
            condition.signal();
            log.info("生产线程 完成生产");
        }finally {
            lock.unlock();
        }
    }
}
