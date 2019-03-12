package com.yanghui.study.monitor;

public class MonitorRule {

    private Object obj = new Object();

    volatile String name;

    volatile boolean exit = false;

    public void waitWithoutMonitor() throws InterruptedException {
        //线程未获取到调用wait()/notify()方法的对象的监视器(monitor)锁就执行wait()/notify()方法
        //抛出IllegalMonitorStateException
        //MonitorRule.class.wait();
        obj.wait();
        //obj.notify();
    }

    public void waitWithMonitor(){
        boolean exit = false;
        long start = System.currentTimeMillis();
        //获取MonitorRule的class对象监视器(monitor)锁
        synchronized(MonitorRule.class){
            while(!exit){
                try {
                    //线程进入MonitorRule的class对象的等待集合(wait sets),这里会解MonitorRule class对象的监视器锁！这里会解锁！这里会解锁！！
                    MonitorRule.class.wait(2000);
                }catch (InterruptedException e) {
                    //do somethings...
                    //Thread.currentThread().interrupt();
                }
                //线程重新获取到MonitorRule的class对象的监视器锁,继续执行下面的代码
                System.out.println("退出等待...耗时："+(System.currentTimeMillis() -start));
                exit = true;
            }
        }
    }

    public void waitAndInterrupt() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj){
                    try {
                        obj.wait(6000);
                    } catch (InterruptedException e) {
                        System.out.println("thread1被中断，wait()方法抛出InterruptedException异常...");
                        return;
                    }
                    System.out.println("thread1正常退出...");
                }
            }
        });
        t1.start();
        Thread.sleep(3000);
        t1.interrupt();
    }

    public void waitAndNotify() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj){
                    try {
                        System.out.println("线程1等待唤醒...");
                        //无限等待，直到当前线程被中断或者obj对象调用notify()/notifyAll()
                        obj.wait();
                    } catch (InterruptedException e) {
                        System.out.println("线程1被中断，wait()方法抛出InterruptedException异常...");
                        return;
                    }
                    System.out.println("线程1正常退出...");
                    name = Thread.currentThread().getName();
                    exit = true;
                }
            }
        },"线程1");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj){
                    try {
                        System.out.println("线程2等待唤醒...");
                        obj.wait();
                    } catch (InterruptedException e) {
                        System.out.println("线程2被中断，wait()方法抛出InterruptedException异常...");
                        return;
                    }
                    System.out.println("线程2正常退出...");
                    name = Thread.currentThread().getName();
                    exit = true;
                }
            }
        },"线程2");
        t1.start();
        t2.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized(obj){
                    /**
                     * 通知obj对象中的等待集合(线程集合)，被选中的一个线程将从等待集合中移出，可以让被移出的线程恢复。
                     * 注意：
                     * 1、对于哪个线程会被选中而被移出，虚拟机没有提供任何保证
                     * 2、恢复之后的线程如果对obj对象进行加锁操作将不会成功，直到线程3完全释放锁之后
                     *
                     * -->记住:
                     * 被notify的线程在唤醒后是需要重新获取监视器锁的
                     */
                    obj.notify();
                    System.out.println("线程3执行了notify(),但是线程3依旧持有obj监视器锁...");
                    try {
                        System.out.println("线程3仍在执行...");
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {

                    }
                    System.out.println("线程3执行完成");
                }
                System.out.println("线程3释放了obj锁");
            }
        },"线程3").start();
        while(!exit);
        if("线程1".equals(name)){
            t2.interrupt();
        }else{
            t1.interrupt();
        }

    }
}
