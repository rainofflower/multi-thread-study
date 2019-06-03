package com.yanghui.study.concurrency.rudiment.util;

public interface ThreadPool<T extends Runnable>{

    void execute(T job);

    void shutdown();

    void addWorkers(int num);

    void removeWorkers(int num);

    int getJobSize();
}
