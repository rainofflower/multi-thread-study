package com.yanghui.study.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用AtomicReference实现单例模式
 */
public class SingletonByAtomic {

    private SingletonByAtomic(){}

    private static SingletonByAtomic instance;

    public static SingletonByAtomic getInstance(){
        for(;;){
            if(instance == null){
                if(atomicReference.compareAndSet(null, new SingletonByAtomic())){
                    instance = atomicReference.get();
                    return instance;
                }
            }
            else{
                return instance;
            }
        }
    }

    private static AtomicReference<SingletonByAtomic> atomicReference = new AtomicReference<>(null);
}
