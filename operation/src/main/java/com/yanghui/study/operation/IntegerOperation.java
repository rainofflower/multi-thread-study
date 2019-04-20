package com.yanghui.study.operation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 整型操作
 *
 * 运算符先后顺序
 * 括号>单目>算数运算符>移位>比较>按位>逻辑>三目>赋值
 *
 * 括号：（）,[]
 * 单目运算符（一元运算符，即一个变量）：+，-，++，--，！，~
 * 算数运算符：+，-，*，/，%
 * 移位运算符：<< ， >>  ， >>>
 * 关系运算符：>,<,>=,<=,==,!=
 * 位运算符：&，|，^，注意 ~ （取反运算符）也属于位运算符，但其也属于一元运算符，运算顺序归到前面的单目运算符
 * 逻辑运算符：&&，||
 * 条件运算（三目运算符）：表达式1？表达式2：表达式3;
 * 赋值运算符：=,+=,-+,*=,/=,<<= 等等
 *
 * 反码表示法规定：正数的反码与其原码相同；负数的反码是对其原码逐位取反，但符号位除外。
 * 补码表示法规定：正数的补码与其原码相同；负数的补码是在其反码的末位加1。
 *
 * 取反运算规律：~a = -(a+1)
 */
@Slf4j
public class IntegerOperation {

    public static final int MAP_MAXIMUM_CAPACITY = 1 << 30;

    public static final int MAP_DEFAULT_INITIAL_CAPACITY = 1 << 4;

    public static final int MAX_INTEGER = (1 << 31) -1;

    public int m1(int cap){
        int n = cap - 1;
        //n |= n >>> 1;
        n >>>= 1;
        return n;
        //return (n < 0) ? 1 : (n >= MAP_MAXIMUM_CAPACITY) ? MAP_MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * 与、位与运算（&）
     * 如果相对应位都是1，则该位结果是1，否则是0
     */
    @Test
    public void m2(){
        //5     101
        //4     100
        int a = 5 & 4;
        //结果
        //a=4   100
        log.info(a+"");
    }

    /**
     * 或、位或运算（|）
     * 如果相对应位有一个是1，则该位结果是1，否则是0
     */
    @Test
    public void m3(){
        //5     101
        //4     100
        int a = 5 | 4;
        //结果
        //a=5   101
        log.info(a+"");
    }

    /**
     * 异或、位异或（^）
     * 如果相对应位只有一个是1，则该位结果是1，否则是0
     */
    @Test
    public void m4(){
        //5     101
        //4     100
        int a = 5 ^ 4;
        //结果
        //a=1   001
        log.info(a+"");

//        int a = 2;
//        int b = 3;
//        log.info("a = "+a+", b = "+b);
////        int c = a;
////        a = b;
////        b = c;
//        a = a ^ b;
//        b = a ^ b;
//        a = a ^ b;
//        log.info("a = "+a+", b = "+b);
    }

    /**
     * 包装类
     */
    @Test
    public void m5(){
        Integer a = 2;
        Integer b = 3;
        log.info("a = "+a+", b = "+b);
        swap(a,b);
//        Integer c = a;
//        a = b;
//        b = c;
        log.info("a = "+a+", b = "+b);
    }

    /**
     *  交换失败，Integer不会创建一个类，调用该方法并非传递地址值
     */
    public static void swap(Integer a, Integer b){
        Integer c = a;
        a = b;
        b =c;
    }

    @Test
    public void test1(){
        //int a = 10 >> 2 ^ 1 >> 1;
        int a = 1 << 2 + 1 * 2 ^ 3;
        log.info(a+"");
    }

    /**
     * ++i 和 i++ 的区别（--i 和 i--）
     */
    @Test
    public void test2(){
        int i = 0;
        //先自增再参与运算，故a = 1
        //int a = ++i;
        //先参与运算，再自增，故a = 0
        int a = i++;
        //不管++放在前面还是后面，i都做了一次自增操作（i = 1）
        log.info("a = "+a+", i = "+i);

        int b = 0;
        //b = b++;
        b = ++b;
        log.info("b = "+b);
    }

    @Test
    public void test3(){
        int a = 1;
        //++a最先运算
        //int i = 3 + 4 * ++a;
        int i = ~3 + 1 * ~-4;
        //先计算~（取反运算），~3 = -4，~-4 = 3，然后计算* ，1 * 3 = 3，最后计算+，-4 + 3 = -1
        //结果为 -1
        log.info(i+"");
    }
}
