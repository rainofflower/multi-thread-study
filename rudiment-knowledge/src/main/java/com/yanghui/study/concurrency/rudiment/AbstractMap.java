package com.yanghui.study.concurrency.rudiment;

/**
 *  抽象类（abstract）使用细则
 */
public abstract class AbstractMap<K,V> implements Map<K,V> {

    /**
     * 位运算符： ^ (亦或运算), & (与运算) , << (左移运算) , >> (右移运算) , >>> (无符号右移运算)
     */
    static final int n = 15 >> 2;

    int size = 3 << 2;

    /**
     *  abstract类实现接口时，可不实现接口中无默认实现的方法
     *  如 Map中的 get 方法，在本抽象类中未给出实现
     */
    public V put(K key,V value){
        return value;
    }

    /**
     * 抽象类中可以声明abstract方法
     * 抽象类中的abstract方法可以是public的也可以是protected的
     */
    protected abstract Map.Entry<K,V> getEntry(K key);

    /**
     *  可重写 接口中的默认实现
     */
    public int size() {
        return size;
    }

    /**
     * 类方法不能使用非static属性
     * 可以使用接口中的属性，因为接口中的属性是static的
     */
    public static int staticMethod(){
        //return size; //类方法不能使用非static域的属性
        //return capacity;  //类方法可以使用接口中的属性
        return n;
    }
}
