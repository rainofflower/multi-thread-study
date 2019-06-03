package com.yanghui.study.concurrency.rudiment;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 设置线程优先级在很多情况下没什么用
 * 程序正确性不能依赖线程的优先级高低
 *
 * 线程优先级不能作为程序正确性的依赖，因为操作系统可以完全不用理会Java
 * 线程对于优先级的设定。
 *
 * 以下程序不同优先级线程被调度的次数极为相近
 */
@Slf4j
public class Priority {
    private static volatile boolean notStart = true;
    private static volatile boolean notEnd = true;

    public static void main(String... args) throws InterruptedException {
        ArrayList<Job> jobs = new ArrayList<>();
        for(int i = 0; i<10; i++){
            int priority = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job job = new Job(priority);
            jobs.add(job);
            Thread thread = new Thread(job, "Thread:"+i);
            thread.setPriority(priority);
            thread.start();
        }
        notStart = false;
        TimeUnit.SECONDS.sleep(10);
        notEnd = false;
        for(Job job : jobs){
            log.info("Job priority: "+ job.priority + ",Count: "+job.jobCount);
        }
    }
    static class Job implements Runnable{
        private int priority;
        private long jobCount;

        public Job(int priority){
            this.priority = priority;
        }

        public void run() {
            while(notStart){
                Thread.yield();
            }
            while(notEnd){
                Thread.yield();
                jobCount++;
            }
        }
    }
}
