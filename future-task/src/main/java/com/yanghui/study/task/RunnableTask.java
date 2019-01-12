package com.yanghui.study.task;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public class RunnableTask implements Runnable{
    @Override
    public void run() {
        //System.out.println("任务被调度...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //log.info("任务被调度...");
    }
}
