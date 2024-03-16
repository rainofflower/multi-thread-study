package com.yanghui.study.atomic;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class CASTest {

    static class OptimisticLockingPlus{
        private static final Unsafe UNSAFE;

        private static final long offset;

        //此处加不加 volatile 执行结果都一样，都能保证+1的原子性。failCount结果也差不多
        //但是不加volatile无法保证其它线程立即可见
        private int value = 0;

        private static AtomicInteger failCount = new AtomicInteger(0);

        public void casPlus(){
            int oldValue;
            int i = 0;
            do{
                oldValue = value;
                if(i++ > 0){
//                    failCount.incrementAndGet();
                }
            }while (!UNSAFE.compareAndSwapInt(this, offset, oldValue, oldValue + 1));
        }

        public void print(){
            System.out.println("最终结果："+value);
            System.out.println("失败次数："+failCount.get());
            System.out.println("value偏移量："+offset);
        }

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
                offset = UNSAFE.objectFieldOffset(OptimisticLockingPlus.class.getDeclaredField("value"));
            } catch (Exception ex) {
                System.out.println("发生异常。。。");
                throw new Error(ex);
            }
        }
    }

    @Test
    public void test() throws InterruptedException {
        OptimisticLockingPlus plus = new OptimisticLockingPlus();
//        CountDownLatch countDownLatch = new CountDownLatch(10);
        for(int i = 0; i<10; i++){
            new Thread(()->{
                for(int j = 0; j < 1000; j++){
                    plus.casPlus();
                }
//                countDownLatch.countDown();
                plus.print();
            }).start();
        }
//        countDownLatch.await();
        plus.print();
        /**
         * 最终结果：10000
         * 失败次数：461
         * value偏移量：12
         *
         * 最终结果：10000
         * 失败次数：15659
         *
         * 最终结果：10000
         * 失败次数：654
         *
         * 最终结果：10000
         * 失败次数：21289
         */
    }

}
