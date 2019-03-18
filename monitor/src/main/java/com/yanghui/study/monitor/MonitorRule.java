package com.yanghui.study.monitor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

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
        Thread t1 = new Thread(() -> {
            synchronized (obj){
                System.out.println("线程1 获取到监视器锁");
                try {
                    obj.wait();
                    System.out.println("线程1 恢复啦。我为什么这么久才恢复，因为虽然线程2早就调用了notify方法，但线程2未立即释放锁，所以我还要获取锁成功之后才能继续执行。");
                } catch (InterruptedException e) {
                    System.out.println("线程1 wait方法抛出了InterruptedException异常，即使是异常，我也是要获取到监视器锁了才会抛出");
                }
            }
        });
        t1.start();
        new Thread(() -> {
            synchronized (obj){
                System.out.println("线程2 拿到了监视器锁。为什么呢，因为线程1 在调用 wait 方法的时候会自动释放锁");
                System.out.println("线程2 设置线程1 中断");
                t1.interrupt();
                System.out.println("线程2 执行完了 中断，先休息3秒再说。");
                try {
                    Thread.sleep(3000);
                    System.out.println("线程2 休息完啦。注意了，调sleep方法和wait方法不一样，不会释放监视器锁");
                } catch (InterruptedException e) {

                }
                System.out.println("线程2 休息够了，结束操作");
            }
        },"线程2").start();
        t1.join();
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
                        System.out.println("线程1 恢复啦。我为什么这么久才恢复，因为虽然线程3早就调用了notify方法，但线程3未立即释放锁，所以我还要获取锁成功之后才能继续执行。");
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
                        System.out.println("线程2 恢复啦。我为什么这么久才恢复，因为虽然线程3早就调用了notify方法，但线程3未立即释放锁，所以我还要获取锁成功之后才能继续执行。");
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
                     * 2、恢复之后的线程如果对obj对象进行加锁操作将不会成功，直到线程3释放锁(执行完synchronized(obj)代码块)
                     *
                     * -->记住:
                     * 被notify的线程需要重新获取对象的监视器锁才能继续执行
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

    /**
     * 该方法常见结果是线程2正常返回，然后线程1被中断抛出InterruptedException异常
     * 也有可能线程1是正常返回的，虽然发生了中断，它的中断状态也确实是true，
     * 但是它没有抛出InterruptedException异常，此时线程2将得不到唤醒，一直 wait
     * @throws InterruptedException
     */
    public void notifyAndInterrupt() throws InterruptedException {
        Thread t1 = new Thread(() ->{
            synchronized (obj){
                System.out.println("线程1 获取到监视器锁");
                try {
                    obj.wait();
                    System.out.println("线程1 正常返回,此时线程1中断状态："+Thread.currentThread().isInterrupted());
                } catch (InterruptedException e) {
                    System.out.println("线程1 被中断，wait方法抛出InterruptedException异常,此时线程1中断状态："+Thread.currentThread().isInterrupted());
                }
            }
        },"线程1");
        t1.start();

        Thread t2 = new Thread(() ->{
            synchronized (obj){
                System.out.println("线程2 获取到监视器锁");
                try {
                    obj.wait();
                    System.out.println("线程2 正常返回,此时线程2中断状态："+Thread.currentThread().isInterrupted());
                } catch (InterruptedException e) {
                    System.out.println("线程2 被中断，wait方法抛出InterruptedException异常,此时线程2中断状态："+Thread.currentThread().isInterrupted());
                }
            }
        },"线程2");
        t2.start();
        //确保线程3在线程1/2之后启动
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));

        Thread t3 = new Thread(()->{
            synchronized (obj){
                System.out.println("线程3 获取到监视器锁");
                System.out.println("线程3 设置线程1中断");
                t1.interrupt();
                //volatile写，禁止中断和notify代码重排序
                exit = true;
                System.out.println("线程3 调用notify");
                obj.notify();
                System.out.println("线程3 休息3秒...");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
                System.out.println("线程3 休息完了，退出同步代码块");
            }
        },"线程3");
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }

    /**
     * LockSupport.park()方法、Thread.sleep()方法和Thread的join()方法都不会释放监视器锁(不同于Object的wait方法)
     * @throws InterruptedException
     */
    public void objectMonitorLock1() throws InterruptedException {
        Thread t2 = new Thread(() ->{
            synchronized(obj){
                System.out.println("线程2 获取到obj监视器锁");
                System.out.println("线程2 调用LockSupport.parkNanos()休眠2秒");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
                System.out.println("线程2 正常返回");
            }
        },"线程2");
        Thread t1 = new Thread(() ->{
            synchronized (obj){
                System.out.println("线程1 获取到obj监视器锁");
                System.out.println("线程1 调用LockSupport.parkNanos()休眠3秒");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
                try {
                    //System.out.println("线程1 调用Thread.sleep()休眠3秒");
                    //Thread.sleep(3000);
                    System.out.println("线程1 调用t2.join(3000)等待线程2 3秒");
                    t2.join(3000);
                } catch (InterruptedException e) {
                    //
                }
                System.out.println("线程1 正常返回");
            }
        },"线程1");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    /**
     * 该方法发生死锁，说明Object的wait()方法只会释放调用wait方法的对象锁，而不会同时释放嵌套同步块中的其他对象锁
     * 每个对象不仅关联一个监视器（monitor），还关联一个等待集合（线程集合 wait set），wait方法只会释放相应的对象锁
     * （ps：notify方法只会移除相应的对象的等待集合中的一个线程）
     * @throws InterruptedException
     */
    public void objectMonitorLock2() throws InterruptedException {
        Thread t1 = new Thread(() ->{
            synchronized (obj){
                System.out.println("线程1 获取到obj监视器锁");
                synchronized (MonitorRule.class){
                    System.out.println("线程1 获取到MonitorRule class对象监视器锁");
                    System.out.println("线程1 obj.wait()释放obj监视器锁进入休眠");
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("线程1 正常返回");
        },"线程1");
        t1.start();
        Thread t2 = new Thread(() ->{
            System.out.println("线程2 调用LockSupport.parkNanos()休眠2秒");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            synchronized(MonitorRule.class){
                System.out.println("线程2 获取到MonitorRule class对象监视器锁");
                synchronized (obj){
                    System.out.println("线程2 获取到obj监视器锁");
//                    System.out.println("线程2 调用LockSupport.parkNanos()休眠2秒");
//                    LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
                    obj.notify();
                }
            }
            System.out.println("线程2 正常返回");
        },"线程2");
        t2.start();
        t1.join();
        t2.join();
    }

    /**
     * 1、同步的static方法等同于synchronized(当前类的class对象)代码块
     * 2、同步的实例方法等同于synchronized(当前实例)代码块
     * 3、以上，显然，在同一个类中，静态同步方法之间构成同步，实例同步方法之间也构成同步，实例同步方法与静态同步方法之间不构成同步
     * @throws InterruptedException
     */
    public void objectMonitorLock3() throws InterruptedException {
        Thread t1 = new Thread(() ->{
            //syncStaticMethod1();
            syncInstanceMethod1();
        });
        Thread t2 = new Thread(() ->{
            synchronized (MonitorRule.class){
                System.out.println("线程2 执行中...");
                System.out.println("线程2 调用LockSupport.parkNanos()休眠3秒");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
                System.out.println("线程2 执行完成");
            }
        });
        Thread t3 = new Thread(() ->{
            //syncStaticMethod2();
            syncInstanceMethod2();
        });
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }

    /**
     * 静态同步方法（带wait方法）
     */
    public static synchronized void syncStaticMethod1(){
        System.out.println("syncStaticMethod1 方法执行中...");
        System.out.println("syncStaticMethod1 方法调用LockSupport.parkNanos()休眠3秒");
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
        //Thread.currentThread().yield();
        try {
            System.out.println("syncStaticMethod1 方法执行MonitorRule.class.wait()释放监视器锁进入休眠");
            /**
             * MonitorRule.class.wait()不会抛出IllegalMonitorStateException，因为synchronized方法等同于synchronized代码块
             * 当前方法为static方法，synchronized关键字相当于synchronized(当前类的class对象)，即synchronized(MonitorRule.class)
             */
            MonitorRule.class.wait();
        } catch (InterruptedException e) {
            System.out.println("syncStaticMethod1 方法执行wait时线程发生中断");
        }
        System.out.println("syncStaticMethod1 方法执行完成");
    }

    /**
     * 静态同步方法
     */
    public static synchronized void syncStaticMethod2(){
        System.out.println("syncStaticMethod2 方法执行中...");
        System.out.println("syncStaticMethod2 方法调用LockSupport.parkNanos()休眠3秒");
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
        MonitorRule.class.notify();
        System.out.println("syncStaticMethod2 方法执行完成");
    }

    /**
     * 实例同步方法（带wait方法）
     */
    public synchronized void syncInstanceMethod1(){
        System.out.println("syncInstanceMethod1 方法执行中...");
        System.out.println("syncInstanceMethod1 方法调用LockSupport.parkNanos()休眠3秒");
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
        try {
            System.out.println("syncInstanceMethod1 方法执行this.wait()释放监视器锁进入休眠");
            /**
             * this.wait()不会抛出IllegalMonitorStateException，因为synchronized方法等同于synchronized代码块
             * 当前方法为实例方法，synchronized关键字相当于synchronized(当前实例)，即synchronized(this)
             */
            this.wait();
        } catch (InterruptedException e) {
            System.out.println("syncStaticMethod1 方法执行wait时线程发生中断");
        }
        System.out.println("syncInstanceMethod1 方法执行完成");
    }

    /**
     * 实例同步方法
     */
    public synchronized void syncInstanceMethod2(){
        System.out.println("syncInstanceMethod2 方法执行中...");
        System.out.println("syncInstanceMethod2 方法调用LockSupport.parkNanos()休眠3秒");
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
        this.notify();
        System.out.println("syncInstanceMethod2 方法执行完成");
    }
}
