package com.yanghui.study.concurrency.rudiment;

/**
 * 字分裂 word tearing
 * 实现 Java 虚拟机需要考虑的一件事情是，每个对象属性以及数组元素之间是独立的，更新一个属性或元素不能影响其他属性或元素的读取与更新。
 * 尤其是，两个线程在分别更新 byte 数组相邻的元素时，不能互相影响与干扰，且不需要同步来保证连续一致性。
 * 一些处理器不提供写入单个字节的能力
 *
 * 以下程序用于测试是否存在字分裂
 * 摘自 The Java® Language Specification（java se 8）
 */
public class WordTearing extends Thread {
    static final int LENGTH = 8;
    static final int ITERS = 1000000;
    static byte[] counts = new byte[LENGTH];
    static Thread[] threads = new Thread[LENGTH];

    final int id;

    WordTearing(int i) {
        id = i;
    }

    public void run() {
        byte v = 0;
        for (int i = 0; i < ITERS; i++) {
            byte v2 = counts[id];
            if (v != v2) {
                System.err.println("Word-Tearing found: " +
                        "counts[" + id + "] = " + v2 +
                        ", should be " + v);
                return;
            }
            v++;
            counts[id] = v;
        }
        System.out.println("done");
    }

    public static void main(String[] args) {
        for (int i = 0; i < LENGTH; ++i)
            (threads[i] = new WordTearing(i)).start();
    }
}