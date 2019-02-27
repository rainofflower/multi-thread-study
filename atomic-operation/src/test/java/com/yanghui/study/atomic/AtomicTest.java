package com.yanghui.study.atomic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicTest {
    
    @Test
    public void test1(){
        final Counter counter = new Counter();
        ComplexClass complexClass1 = new ComplexClass();
        ComplexClass complexClass2 = new ComplexClass();
        AtomicStampedReference<ComplexClass> stampedRef = new AtomicStampedReference<>(complexClass2, 0);
        AtomicOperation atomicOperation = new AtomicOperation();
        List<Thread> ts = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int j = 0; j<100; j++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i<1000; i++){
                        counter.count();
                        counter.safeCount();
                        atomicOperation.normalMethod(complexClass1);
                        atomicOperation.atomicMethod(stampedRef);
                    }
                }
            });
            ts.add(t);
        }
        for (Thread t: ts){
            t.start();
        }
        for (Thread t: ts){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(counter.i);
        System.out.println(counter.atomicI.get());
        System.out.println("complexClass1: a="+complexClass1.getA()+" b="+complexClass1.getB());
        System.out.println("stampedRef中的complexClass: a="+stampedRef.getReference().getA()+" b="+stampedRef.getReference().getB()+" stamp="+stampedRef.getStamp()+" complexClass是否是同一个："+(complexClass2==stampedRef.getReference()));
        System.out.println(System.currentTimeMillis()-start);
    }
}
