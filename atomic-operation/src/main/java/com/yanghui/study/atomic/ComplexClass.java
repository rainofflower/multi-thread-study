package com.yanghui.study.atomic;

public class ComplexClass {

    //使用64位的long型变量，基于JSR-133内存模型测试考虑
    private long a = 0;

    private long b = 0;

    public long getA() {
        return a;
    }

    public void setA(long a) {
        this.a = a;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }
}
