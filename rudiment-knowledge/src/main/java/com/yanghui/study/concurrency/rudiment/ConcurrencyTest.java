package com.yanghui.study.concurrency.rudiment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcurrencyTest {
    private static final long count = 100000000l;

    /**
     * 并发执行
     * @throws InterruptedException
     */
    public static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int a = 0;
                for (long i = 0;i < count; i++){
                    a += 5;
                }
            }
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++){
            b--;
        }
        long time = System.currentTimeMillis() - start;
        thread.join();
        log.info("Concurrency :"+time+"ms,b="+b);
    }

    /**
     * 串行执行
     */
    public static void serial(){
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0;i < count; i++){
            a += 5;
        }
        int b = 0;
        for (long i = 0; i < count; i++){
            b--;
        }
        long time = System.currentTimeMillis() - start;
        log.info("Serial :"+time+"ms,b="+b+",a="+a);
    }
}
