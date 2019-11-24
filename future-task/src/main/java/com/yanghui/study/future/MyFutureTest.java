package com.yanghui.study.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by YangHui on 2019/11/24
 */
@Slf4j
public class MyFutureTest {

    @Test
    public void test(){
        ExecutorService fixPool = Executors.newFixedThreadPool(2);
        ExecutorService listeningPool = new EventExecutor(5,
                10,
                0,
                TimeUnit.SECONDS,
                new SynchronousQueue<>()
                );
        DefaultPromise promise = new DefaultPromise(fixPool) {
            @Override
            public void run() {
                try {
                    log.info("处理任务1中...");
                    Thread.sleep(5000);
                    log.info("1处理完成");
//                    throw new RuntimeException("发生未知错误");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        promise.addListener(new Listener<Future<Void>>() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if(future.isSuccess()){
                    log.info("任务1结束，结果：" + future.get());
                }else{
                    log.info("1发生错误；error:{}",future.getFailure());
                }
            }
        });
        listeningPool.execute(promise);
        DefaultPromise promise2 = new DefaultPromise(fixPool) {
            @Override
            public void run() {
                try {
                    log.info("处理任务2中...");
                    Thread.sleep(8000);
                    log.info("2处理完成");
//                    throw new RuntimeException("发生未知错误");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        listeningPool.execute(promise2);
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
        promise.addListener(new Listener<Future<Void>>() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if(future.isSuccess()){
                    log.info("任务1结束，啦啦啦啦啦啦");
                }else{
                    log.info("1发生错误；error:{}",future.getFailure());
                }
            }
        });

        promise2.addListener(new Listener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if(future.isSuccess()){
                    log.info("任务2执行成功");
                    log.info("休息5s ...");
                    LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
                    log.info("休息好了...");
                }
            }
        });
        try {
            while(!promise.isDone() || !promise2.isDone()){
                Thread.sleep(1000);
            }
            log.info("任务全部执行完成，等待回调全部执行...");
            fixPool.shutdown();
            while(!fixPool.isTerminated()){
                Thread.yield();
            }
            log.info("回调全部执行完");
        } catch (InterruptedException e) {
            //
        }
    }

}
