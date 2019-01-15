package com.yanghui.study.task;

public interface Generic<T> {

    T call();

    <E> E genericMethod1();

    <K,V> K genericMethod2(K key);

    <T extends Comparable<T>> T get(T t1,T t2);
}
