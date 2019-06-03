package com.yanghui.study.concurrency.rudiment;


import lombok.extern.slf4j.Slf4j;

/**
 * 一、final域重排序规则：
 *
 * 1）在构造函数内对一个final域的写入，与随后把这个被构造对象的引用赋值给一个引用
 * 变量，这两个操作之间不能重排序。
 * 2）初次读一个包含final域的对象的引用，与随后初次读这个final域，这两个操作之间不能
 * 重排序
 *
 * 规则 1 的实现包含以下两点：
 * JMM禁止编译器把final域的写重排序到构造函数之外。
 * 编译器会在final域的写之后，构造函数return之前，插入一个StoreStore屏障。这个屏障
 * 禁止处理器把final域的写重排序到构造函数之外。
 *
 *
 * 二、在构造函数内部，不能让这个被构造对象的引用为其他线程所见，也就是对
 * 象引用不能在构造函数中“逸出”。
 */
@Slf4j
public class FinalEscape {

    public final int i;
    public static FinalEscape obj;

    public FinalEscape(){
        i = 1;
        obj = this;
    }

    public static void writer(){
        obj = new FinalEscape();
    }

    public static void reader(){
        if(obj != null){
            if(obj.i != 1){
                log.info("构造函数final域初始化和被构造函数溢出操作发生重排序");
            }
        }
    }

}
