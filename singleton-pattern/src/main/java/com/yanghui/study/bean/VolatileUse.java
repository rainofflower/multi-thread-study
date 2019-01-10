package com.yanghui.study.bean;

public class VolatileUse {
    public volatile int inc = 0;

    public void increase() {
        inc++;
    }
}
