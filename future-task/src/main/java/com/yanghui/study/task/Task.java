package com.yanghui.study.task;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class Task implements Callable<String> {
    @Override
    public String call(){
        log.info("Callable任务被调度...");
        return "返回结果";
    }
}
