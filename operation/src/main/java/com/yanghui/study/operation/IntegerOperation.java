package com.yanghui.study.operation;

/**
 * 整型操作
 */
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
}
