package com.yanghui.study.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 使用Unsafe类实现单例模式
 */
public class SingletonByUnsafe {

    private SingletonByUnsafe(){}

    private static volatile SingletonByUnsafe instance;

    /**
     *
     * @return
     */
    public static SingletonByUnsafe getInstance(){
       for(;;){
            if(instance == null){
                if(UNSAFE.compareAndSwapObject(SingletonByUnsafe.class, offset, null, new SingletonByUnsafe())){
                    return instance;
                }
            }
            else{
                return instance;
            }
       }
    }

    private static final Unsafe UNSAFE;

    private static final long offset;

    /**
     * 直接通过Unsafe.getUnsafe()获取Unsafe实例
     * 会抛出SecurityException异常，原因是
     * getUnsafe()方法上有@CallerSensitive注解
     * 故此处使用反射直接获取其theUnsafe属性
     */
    static{
        try {
            Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
            unsafe.setAccessible(true);
            UNSAFE = (Unsafe) unsafe.get(null);
            offset = UNSAFE.staticFieldOffset(SingletonByUnsafe.class.getDeclaredField("instance"));
        } catch (Exception ex) {
            System.out.println("发生异常。。。");
            throw new Error(ex);
        }
    }

}
