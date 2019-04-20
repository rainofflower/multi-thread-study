package com.yanghui.study.concurrency.rudiment;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class Person {

    //对象初始化值为 0
    private int age;

    //对象初始化值为 false
    public boolean flag;

    //对象初始化值为 0.0
    public float f;

    //对象初始化值为 null
    public String name;

    public BigDecimal bd;

    //对象初始化值为 0.0
    public double d;

    public Person(){
        System.out.println(name);
        log.info("initialize name");
        name = "flower";
        System.out.println(name.getClass().getName()+"@" + Integer.toHexString(name.hashCode()));
        name = "rain";
        System.out.println(name.getClass().getName()+"@" + Integer.toHexString(name.hashCode()));
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
