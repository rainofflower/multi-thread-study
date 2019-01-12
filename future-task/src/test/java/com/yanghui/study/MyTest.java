package com.yanghui.study;

import com.yanghui.study.task.RunnableTask;
import com.yanghui.study.task.Task;
import com.yanghui.study.util.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
public class MyTest {
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        ExecutorService pool = ThreadPool.threadPool();
        pool.submit(new RunnableTask());
        Future<String> result = pool.submit(new Task());
        log.info(result.get());
    }
}
