package com.yanghui.study.lock;

public class Counter {
    public int a;

    public int s;

    public void increment(){
        a++;
    }

    public synchronized void synIncrement(){
        s++;
    }
}
