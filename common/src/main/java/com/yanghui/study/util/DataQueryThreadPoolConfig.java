package com.yanghui.study.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class DataQueryThreadPoolConfig {

    private int cpus = Runtime.getRuntime().availableProcessors();

    private int corePoolSize = cpus;

    private int maxPoolSize = cpus << 1;

    private int queueCapacity = 20;

    private long keepAliveSeconds = 30l;

    @Bean
    public ThreadPoolExecutor dataQueryThreadPool(){
        return new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity));
    }
}
