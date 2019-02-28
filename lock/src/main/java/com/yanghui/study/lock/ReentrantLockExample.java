package com.yanghui.study.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {

    int a = 0;
    ReentrantLock lock = new ReentrantLock();

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
}
