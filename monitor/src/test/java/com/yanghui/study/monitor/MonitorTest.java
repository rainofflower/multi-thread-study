package com.yanghui.study.monitor;

import org.junit.Test;

public class MonitorTest {

    private MonitorRule monitorRule = new MonitorRule();

    @Test
    public void test01(){
        try {
            monitorRule.waitWithoutMonitor();
        } catch (InterruptedException e) {
            System.out.println("线程处于中断状态...");
        }
    }

    @Test
    public void test02() throws InterruptedException {
        monitorRule.waitWithMonitor();
    }

    @Test
    public void test03() throws InterruptedException {
        monitorRule.waitAndInterrupt();
    }

    @Test
    public void test04() throws InterruptedException {
        monitorRule.waitAndNotify();
    }

    @Test
    public void test05() throws InterruptedException {
        monitorRule.notifyAndInterrupt();
    }

    @Test
    public void test06() throws InterruptedException {
        monitorRule.objectMonitorLock1();
    }

    @Test
    public void test07() throws InterruptedException {
        monitorRule.objectMonitorLock2();
    }

    @Test
    public void test08() throws InterruptedException {
        monitorRule.objectMonitorLock3();
    }
}
