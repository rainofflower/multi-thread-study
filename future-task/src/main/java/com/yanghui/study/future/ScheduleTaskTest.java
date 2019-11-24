package com.yanghui.study.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by YangHui on 2019/11/24
 */
@Slf4j
public class ScheduleTaskTest {

    /**
     * ScheduledThreadPoolExecutor每次添加任务都会将任务列表按时间先后顺序重新排序，
     * 线程池里的每个线程都尝试获取第一个任务（最先执行的任务），获取到了就去执行，
     * 而获取任务的时候会根据任务添加进队列时设置的延迟时间等待。
     *
     * 注意：构造方法里指定的核心线程数将会是线程池中能创建的最大数量的线程数。具体见delayedExecute()方法中的
     * ensurePrestart()方法。
     * 如果所有线程都处于繁忙状态，新提交的任务将无法保证按时执行！
     */
    @Test
    public void test(){
        ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1); //2
        long start1 = System.currentTimeMillis();
        scheduledPool.schedule(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    log.info("任务1开始执行...已过去：{} s",(System.currentTimeMillis() - start1)/1000.0);
                    long s = System.currentTimeMillis();
                    LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(8));
                    log.info("执行任务1成功,耗时：{} s",(System.currentTimeMillis() - s)/1000.0);
                    return "执行任务1成功";
                }
            },
        2,
            TimeUnit.SECONDS);

        long start2 = System.currentTimeMillis();
        scheduledPool.schedule(new Runnable() {
                                   @Override
                                   public void run(){
                                       log.info("执行任务2成功,已过去：{} s",(System.currentTimeMillis() - start2)/1000.0);
                                   }
                               },
                5,
                TimeUnit.SECONDS);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
