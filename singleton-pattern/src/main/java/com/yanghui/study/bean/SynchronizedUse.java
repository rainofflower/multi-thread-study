package com.yanghui.study.bean;

public class SynchronizedUse {
    public int inc = 0;

    public synchronized void increase(){
        inc++;
    }
}
