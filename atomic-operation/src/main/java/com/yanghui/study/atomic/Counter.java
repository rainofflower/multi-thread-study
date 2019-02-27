package com.yanghui.study.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

    public AtomicInteger atomicI = new AtomicInteger(0);

    public int i = 0;

    /**
     * 使用CAS实现线程安全计数器
     */
    public void safeCount(){
        for (;;){
            int i = atomicI.get();
            //CAS:比较并交换，compare and swap/compare and set
            boolean suc = atomicI.compareAndSet(i, ++i);
            if(suc){
                break;
            }
            /*else{
                System.out.println(Thread.currentThread().getName()+"线程工作内存数据过时,此时值为："+i);
            }*/
        }
    }

    /**
     * 非线程安全计数器
     */
    public void count(){
        i++;
    }

}
