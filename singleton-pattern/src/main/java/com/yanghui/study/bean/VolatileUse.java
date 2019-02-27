package com.yanghui.study.bean;

public class VolatileUse {
    public volatile int inc = 0;

    public void increase() {
        inc++;
    }

    private volatile int count = 0;

    /**
     * 利用volatile的可见性保证线程修改本地内存中的共享变量立即刷新到主内存，其它线程从主内存中读取到最新的值
     */
    public volatile boolean shutDownRequested = false;

    public void shutDown(){
        shutDownRequested = true;
    }

    public void doWork() throws InterruptedException {
        System.out.println("任务执行中...");
        while (!shutDownRequested){
            //Thread.sleep(10);
            //do somethings
        }
        System.out.println("退出任务");
    }

    /**
     * 该方法未使用synchronized
     * 实现开销较低的读－写锁
     * 关键点：count变量为volatile变量，允许多个线程的读操作
     * @return
     */
    public int getCount(){
        return count;
    }

    public synchronized int increment(){
        return count++;
    }
}
