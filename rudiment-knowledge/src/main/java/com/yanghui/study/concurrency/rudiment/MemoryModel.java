package com.yanghui.study.concurrency.rudiment;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * java内存模型
 * Java 内存模型定义了一系列规则，这些规则定义了对共享内存的写操作对于读操作的可见性
 * 内存模型描述了程序执行时的可能的表现行为。只要执行的结果是满足 java 内存模型的所有规则，那么虚拟机对于具体的实现可以自由发挥
 * 关键词：
 * data race; 数据竞争
 * reorder; 重排序
 * rearrange; 重排序
 * synchronized; 同步
 * instruction; 指令
 * isolation; 隔离
 * shared memory; 共享内存
 * heap memory; 堆内存
 * action 操作
 * intra-thread semantics 线程内语义
 * inter-thread action 线程间操作
 * external actions 外部操作
 * thread divergence actions 线程分歧操作
 * volatile read,volatile write,lock,unlock
 * Sequential consistency 顺序一致性
 * visibility 可见性； immediately visible 立即可见
 * atomic 原子的
 * Happens-before order(强调可见性问题)
 */
@Slf4j
public class MemoryModel {

    final int x;
    int y;

    static MemoryModel m;

    public MemoryModel(){
        x = 3;
        y = 4;
    }

    public static void writer(){
        m = new MemoryModel();
    }

    public static void reader(){
        if(m != null){
            log.info(m.x+"");    //程序一定能看到 3
            log.info(m.y+"");    //也许会看到 0
        }
    }


    /**
     * native方法未给出外部实现会抛出java.lang.UnsatisfiedLinkError异常
     */
    public native void jni();/* {
        assert foo == 0; //我们假设外部操作执行的是这个。
    } */

    int foo = 0;

    /**
     *  method()方法中jni()是外部操作，不会和 "foo = 42;" 这条语句进行重排序。
     */
    void method() {
        jni(); // 外部操作
        foo = 42;
    }

    public void thread1() {
        while (true){} // 线程分歧操作
        //foo = 42; 假设该段代码不报错，多线程分别调用thread1()和thread2()
    }

    public void thread2() {
        assert foo == 0; // 这里永远不会失败
    }
}
