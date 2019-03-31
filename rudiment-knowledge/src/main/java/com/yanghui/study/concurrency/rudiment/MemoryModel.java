package com.yanghui.study.concurrency.rudiment;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
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
    public static final Map<String,String> map;

    static{
        map = new HashMap<>();
        map.put("name","yanghui");
    }

    static MemoryModel m;

    public MemoryModel(int value){
        x = value;
    }

    public int f(){
        return d(this, this);
    }

    /**
     * 在一个线程内，允许 JVM 实现对于 final 属性的读操作与构造方法之外的对于这个 final 属性的修改进行重排序
     * 在方法 d 中，编译器允许对 x 的读操作和方法 g 进行重排序，
     * 这样的话，new MemoryModel().f()可能会返回 -1, 0, 或 1
     */
    int d(MemoryModel a1, MemoryModel a2){
        int i = a1.x;
        g(a1);
        int j = a2.x;
        return j - i;
    }

    static void g(MemoryModel a) {
        //利用反射将a.x的值加 1
        try{
            Field x = MemoryModel.class.getDeclaredField("x");
            x.setAccessible(true);
            x.set(a,a.x+1);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public MemoryModel(){
        //log.info(x+"");
        x = 3;
        log.info(x+"");
        //下一行代码编译不通过，构造方法里只能给final属性写入一次值（指通过 = 号赋值）
        //x = 4;
        try {
            //通过反射修改final字段的值
            Field x = MemoryModel.class.getDeclaredField("x");
            x.setAccessible(true);
            x.set(this,4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(x+"");
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

    public int getX(){
        return x;
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
