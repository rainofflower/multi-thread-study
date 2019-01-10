package com.yanghui.study.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private ExecutorService executorService;

    private static class ThreadPoolHolder{
        static ExecutorService threadPool = Executors.newCachedThreadPool();
    }

    public static ExecutorService getThreadPool(){
        return ThreadPoolHolder.threadPool;
    }
}
