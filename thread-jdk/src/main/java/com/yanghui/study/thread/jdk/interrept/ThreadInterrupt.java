package com.yanghui.study.thread.jdk.interrept;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>关于线程中断：</p>
 * java中的线程中断不是类似 linux 里面的命令 kill -9 pid，不是说我们中断某个线程，这个线程就停止运行了 <br>
 * 中断代表线程状态，每个线程都关联了一个中断状态，是一个 true 或 false 的 boolean 值，初始值为 false <br>
 *
 * <br>关于中断状态，我们需要重点关注 Thread 类中的以下几个方法：
 * <pre>
 * 1、Thread 类中的实例方法，持有线程实例引用即可检测线程中断状态
 * public boolean isInterrupted() {}
 *
 * 2、Thread 中的静态方法，检测调用这个方法的线程是否已经中断
 * 注意：这个方法返回中断状态的同时，会将此线程的中断状态重置为 false
 * 所以，如果我们连续调用两次这个方法的话，第二次的返回值肯定就是 false 了
 * public static boolean interrupted() {}
 *
 * 3、Thread 类中的实例方法，用于设置一个线程的中断状态为 true
 * public void interrupt() {}
 *
 * 分析前两个方法的关系：
 * 在说明这两个方法之前先看Thread类的一个私有方法
 * private native boolean isInterrupted(boolean ClearInterrupted);
 * 该方法属于private方法，由底层实现，该方法上的注释：
 * Tests if some Thread has been interrupted. The interrupted state is reset or not based on the value of ClearInterrupted that is passed.
 * 即：测试线程是否被中断。中断状态是否要重置基于传递过来的ClearInterrupted参数值
 *
 * public boolean isInterrupted() {
 *      //该实例方法就只是调用private native boolean isInterrupted(boolean ClearInterrupted);方法
 *      //传入参数false,表示不重置线程的中断状态
 *      return isInterrupted(false);
 * }
 *
 * public static boolean interrupted() {
 *      //该静态方法获取到当前线程实例，然后同样的调用private native boolean isInterrupted(boolean ClearInterrupted);方法
 *      //与上面第一个方法不同之处在于传入的参数为true,表示重置线程的中断状态
 *     return currentThread().isInterrupted(true);
 * }
 *
 * </pre>
 *
 * 我们说中断一个线程，其实就是设置了线程的 interruption status 为 true，
 * 至于说被中断的线程怎么处理这个状态，那是那个线程自己的事。如以下代码：
 * <pre>
 * while (!Thread.interrupted()) {
 *    doWork();
 *    System.out.println("我做完一件事了，准备做下一件，如果没有其他线程中断我的话");
 * }
 * </pre>
 * <p>当然，中断除了是线程状态外，还有其他含义，否则也不需要专门搞一个这个概念出来了</p>
 *
 * 如果线程处于以下三种情况，那么当线程被中断的时候，能自动感知到：
 * <pre>
 * 1、来自 Object 类的 wait()、wait(long)、wait(long, int)，
 * 来自 Thread 类的 join()、join(long)、join(long, int)、sleep(long)、sleep(long, int)
 * 这几个方法的相同之处是，方法上都有: throws InterruptedException
 * 如果线程阻塞在这些方法上（我们知道，这些方法会让当前线程阻塞），
 * 这个时候如果其他线程对这个线程进行了中断，那么这个线程会从这些方法中立即返回，抛出 InterruptedException 异常，同时重置中断状态为 false。
 *
 * 2、实现了 InterruptibleChannel 接口的类中的一些 I/O 阻塞操作，如 DatagramChannel 中的 connect 方法和 receive 方法等
 * 如果线程阻塞在这里，中断线程会导致这些方法抛出 ClosedByInterruptException 并重置中断状态。
 *
 * 3、Selector 中的 select 方法
 * 一旦中断，方法立即返回
 *</pre>
 * 对于以上 3 种情况是最特殊的，因为他们能自动感知到中断（这里说自动，当然也是基于底层实现），<br>
 * 并且在做出相应的操作后都会重置中断状态为 false。<br>
 * 还有一种能自动感知中断的方法--LockSupport.park()方法，如果线程阻塞在 LockSupport.park(Object obj) 方法，<br>
 * 也叫挂起，这个时候的中断也会导致线程唤醒，但是唤醒后不会重置中断状态，所以唤醒后去检测中断状态将是 true。
 *
 */
public class ThreadInterrupt {

    private static final Unsafe UNSAFE;

    private Object obj = new Object();

    public void threadParkAndInterrupt() throws InterruptedException {
        //Thread mainThread = Thread.currentThread();
        Thread t1 = new Thread(() ->{
            System.out.println("线程1执行中...此时线程1中断状态：" + Thread.currentThread().isInterrupted());
            System.out.println("==>线程1被挂起");
            //LockSupport.unpark(mainThread);
            //注意：LockSupport.park()挂起的线程被中断后也会导致线程唤醒，但是唤醒后不会重置中断状态
            LockSupport.park();
            //这里使用Thread.interrupted()重置线程中断状态为false,
            //如果改成Thread.currentThread().isInterrupted(),那么中断状态不变，
            //若前面挂起的线程是由线程中断唤醒的，那么后面线程挂起(LockSupport.park())操作将会无效
            System.out.println("==>线程1被唤醒,此时线程1中断状态：" + Thread.currentThread().isInterrupted());
            System.out.println("==>线程1再次被挂起...");
            LockSupport.park();
            System.out.println("==>线程1再次被唤醒，此时线程1中断状态：" + Thread.currentThread().isInterrupted());
        },"线程1");
        t1.start();
        //LockSupport.park();
        System.out.println("主线程执行中...");
        System.out.println("主线程休眠5秒...");
        LockSupport.parkUntil(System.currentTimeMillis()+5000);
        System.out.println("主线程中断线程1...");
        t1.interrupt();
        System.out.println("主线程再次休眠5秒...");
        LockSupport.parkUntil(System.currentTimeMillis()+5000);
        System.out.println("主线程尝试唤醒线程1,等待线程1响应...");
        LockSupport.unpark(t1);
        t1.join();
    }

   public void waitAndInterrupt() throws InterruptedException {
       Thread t1 = new Thread(() ->{
           System.out.println("线程1执行中...此时线程1中断状态：" + Thread.currentThread().isInterrupted());
           synchronized(obj) {
               try {
                   System.out.println("==>线程1调用wait()释放锁进入阻塞状态...");
                   obj.wait();
               } catch (InterruptedException e) {
                   //
               }
               System.out.println("==>线程1被唤醒,此时线程1中断状态：" + Thread.currentThread().isInterrupted());
           }
           //设置内存屏障，防止前后代码重排序
           UNSAFE.fullFence();
           System.out.println("==>线程1执行LockSupport.park()再次等待...");
           LockSupport.park();
           System.out.println("==>线程1再次被唤醒，此时线程1中断状态：" + Thread.currentThread().isInterrupted());
//           System.out.println("==>线程1执行LockSupport.park()再次等待...");
//           LockSupport.park();
//           System.out.println("==>线程1再次被唤醒，此时线程1中断状态：" + Thread.currentThread().isInterrupted());
       },"线程1");
       t1.start();
       System.out.println("主线程执行中...");
       System.out.println("主线程休眠3秒...");
       LockSupport.parkUntil(System.currentTimeMillis()+3000);
       System.out.println("主线程中断线程1...");
       t1.interrupt();
//       System.out.println("主线程再次休眠3秒...");
//       LockSupport.parkUntil(System.currentTimeMillis()+3000);
////       synchronized (obj){
////           System.out.println("主线程执行obj.notifyAll()唤醒obj对象的等待线程...");
////           obj.notifyAll();
////       }
//       LockSupport.unpark(t1);
//       System.out.println("主线程给了线程1 permit...");
       t1.join();
   }



    static{
        try {
            Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
            unsafe.setAccessible(true);
            UNSAFE = (Unsafe) unsafe.get(null);
        } catch (Exception ex) {
            System.out.println("发生异常。。。");
            throw new Error(ex);
        }
    }
}
