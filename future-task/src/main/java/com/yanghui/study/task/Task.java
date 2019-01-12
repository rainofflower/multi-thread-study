package com.yanghui.study.task;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class Task implements Callable<String> {
    @Override
    public String call() throws Exception {
        log.info("任务被调度...");
        return "result";
    }
}