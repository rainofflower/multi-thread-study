package com.yanghui.study.concurrency.rudiment;

import java.util.concurrent.CountDownLatch;

/**
 * 重排序示例代码
 * cpu i7 8750h表示无法表现出重排序的代码执行顺序 ￣へ￣
 */
public class Rearrange {

    private static int x = 0, y = 0;
    private static int a = 0, b =0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for(;;) {
            i++;
            x = 0; y = 0;
            a = 0; b = 0;
            CountDownLatch latch = new CountDownLatch(1);

            Thread one = new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                }
                a = 1;
                x = b;
            });

            Thread other = new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                }
                b = 1;
                y = a;
            });
            one.start();other.start();
            latch.countDown();
            one.join();other.join();

            String result = "第" + i + "次 (" + x + "," + y + "）";
            if(x == 0 && y == 0) {
                //发生重排序
                System.err.println(result);
                break;
            } else {
                System.out.println(result);
            }
        }
    }
}