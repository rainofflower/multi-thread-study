package com.yanghui.study.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicOperation {

    /**
     * 原子操作方法（循环CAS操作）
     * 此处使用标记类解决AtomicReference类中存在的ABA问题
     * @param stampedRef AtomicStampedReference
     */
    public void atomicMethod(AtomicStampedReference<ComplexClass> stampedRef){
        for(;;){
            ComplexClass expectedRef = stampedRef.getReference();
            int expectedStamp = stampedRef.getStamp();
            ComplexClass newRef = new ComplexClass();
            newRef.setA(expectedRef.getA()+1);
            newRef.setB(expectedRef.getB()-1);
            boolean success = stampedRef.compareAndSet(expectedRef, newRef, expectedStamp, expectedStamp + 1);
            if(success){
                break;
            }
        }
    }

    /**
     * 普通方法
     * @param obj 变化的对象
     */
    public void normalMethod(ComplexClass obj){
        obj.setA(obj.getA()+1);
        obj.setB(obj.getB()-1);
    }
}
