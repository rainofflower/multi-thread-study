package com.yanghui.study.concurrency.rudiment;

import lombok.extern.slf4j.Slf4j;

/**
 * 在Java里面参数传递都是按值传递,没有按引用传递！
 */
@Slf4j
public class ValueReference {

    /**
     * 基本数据类型传递
     */
    public static void basicDataTransfer(){
        int num = 5;
        log.info(num+"");
        changeValue(num);
        log.info(num+"");
    }

    /**
     * 基本数据类型修改
     * @param x 基本数据类型
     */
    public static void changeValue(int x){
        /**
         * num作为参数传递给changeValue()方法时，是将内存空间中num所指向的那个存储单元中存放的值，
         * 即"5",传送给了changeValue()方法中的x变量，而这个x变量也在内存空间中分配了一个存储单元，
         * 这个时候，就把num的值5传送给了这个存储单元中。
         * 此后，在changeValue()方法中对x的一切操作都是针对x所指向的这个存储单元，与num所指向的那个存储单元没有关系了！
         */
        x = 4;
    }

    /**
     * 对象传递（引用传递）
     */
    public static void referenceTransfer(){
        /**
         * new 创建一个对象Person，实际分配了两个对象：新创建的Person类的实体对象，和指向该对象的引用变量person
         * 【注意：在java中，新创建的实体对象在堆内存中开辟空间，而引用变量在栈内存中开辟空间】
         */
        Person person = new Person();
        person.setAge(20);
        log.info(person.getAge()+"");
        log.info("person地址："+person);
        changeReference(person);
        log.info(person.getAge()+"");
        log.info("person地址："+person);
    }

    /**
     * 修改对象属性值
     * @param p 对象引用
     */
    public static void changeReference(Person p){
        /**
         *
         * 传递引用的地址值，在栈中会为引用变量 p 分配一个新的存储单元用于存储指向堆中对象的地址，
         * 这个新的存储单元（引用变量）与实参（也是一个引用变量）的存储单元完全独立不相干，
         * 要说有什么共同点，那就是两者刚开始的时候存储的内容相同，都是同一个对象的地址，指向同一个对象，
         * 如果后面两者存储的内容（也就是对象的地址值）不变，那么两者使用该值对对象进行修改，被修改的对象都是同一个，
         * 但是如果两者存储的内容发生变化，那么两者对引用地址指向的对象的修改就是在修改不同的对象了
         *
         * 所以如果下方执行 p = new Person()，那么 p 存储的内容将会修改成新对象的地址
         * 否则，p 中存储的内容依旧是传递过来的对象地址值（实参的值）
         */
        //p = new Person();
        log.info("p地址："+p);
        p.setAge(18);
    }

    public static void test(){
        Person q = new Person();
        Person p = q;

        Person r1 = p;
        int r2 = r1.getAge();
        log.info(p.getAge()+"");
        Person r6 = p;
        r6.setAge(3);
        Person r3 = q;
        int r4 = r3.getAge();
        log.info(p.getAge()+"");
        int r5 = r2;
        log.info(p.getAge()+"");
    }
}
