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
}
