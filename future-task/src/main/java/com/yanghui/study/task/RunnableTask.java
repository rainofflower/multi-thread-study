package com.yanghui.study.task;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunnableTask implements Runnable{
    @Override
    public void run() {
        log.info("Runnable任务被调度...");
    }
}
