package com.yanghui.study.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPool {

    private static class ThreadPoolHolder{
        static ExecutorService threadPool = Executors.newCachedThreadPool();
    }

    @Bean
    public static ExecutorService getThreadPool(){
        return ThreadPoolHolder.threadPool;
    }
}
