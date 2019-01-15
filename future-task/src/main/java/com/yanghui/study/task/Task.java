package com.yanghui.study.task;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class Task<T> implements Callable<T> {

    @Override
    public T call(){
        log.info("Callable任务被调度...");
        String result = "执行结果哟~~~";
        return (T) result;
    }
}
