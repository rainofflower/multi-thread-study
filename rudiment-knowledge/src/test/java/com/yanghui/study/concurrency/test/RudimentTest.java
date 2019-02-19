package com.yanghui.study.concurrency.test;

import com.yanghui.study.concurrency.rudiment.ConcurrencyTest;
import com.yanghui.study.concurrency.rudiment.DeadLockDemo;
import org.junit.Test;

public class RudimentTest {

    @Test
    public void test1() throws InterruptedException {
        ConcurrencyTest.concurrency();
        ConcurrencyTest.serial();
    }

    @Test
    public void test2() throws InterruptedException {
        DeadLockDemo.deadLock();
    }
}
