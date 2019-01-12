package com.yanghui.study;

import com.yanghui.study.task.RunnableTask;
import com.yanghui.study.util.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

@Slf4j
public class MyTest {
    @Test
    public void hello(){
        ExecutorService pool = ThreadPool.threadPool();
        pool.submit(new RunnableTask());
        pool.submit(new Task());
    }

    class Task implements Runnable{

        @Override
        public void run() {
            log.info("Task被调度....");
        }
    }
}
