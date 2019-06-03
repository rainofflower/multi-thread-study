package com.yanghui.study.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Value("${thread.pool.coreSize}")
    private int corePoolSize;

    @Value("${thread.pool.maxSize}")
    private int maxPoolSize;

    @Value("${thread.pool.capacity}")
    private int queueCapacity;

    @Value("${thread.pool.aliveSeconds}")
    private int keepAliveSeconds;

    @Bean
    public ThreadPoolTaskExecutor threadPool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
