package com.yanghui.study;

import com.yanghui.study.bean.Singleton;
import com.yanghui.study.bean.SynchronizedUse;
import com.yanghui.study.bean.VolatileUse;
import com.yanghui.study.thread.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

@Slf4j
public class ThreadTest {

    @Test
    public void test03() throws InterruptedException {
        SynchronizedUse syncBean = new SynchronizedUse();
        ExecutorService pool = ThreadPool.getThreadPool();
        for(int i = 0;i<10;i++){
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    for(int j = 0;j<1000;j++){
                        syncBean.increase();
                    }
                }
            });
            pool.execute(thread);
        }
        pool.shutdown();
        while(true){
            if(pool.isTerminated()){
                System.out.println("所有的线程都结束了！");
                System.out.println("计算结果："+syncBean.inc);
                break;
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void test02() throws InterruptedException {
        VolatileUse bean = new VolatileUse();
        ExecutorService pool = ThreadPool.getThreadPool();
        for(int i = 0;i<10;i++){
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    for(int j = 0;j<1000;j++){
                        bean.increase();
                    }
                }
            });
            pool.execute(thread);
        }
        pool.shutdown();
        while(true){
            if(pool.isTerminated()){
                System.out.println("所有的线程都结束了！");
                System.out.println("计算结果："+bean.inc);
                break;
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void test01(){
        ExecutorService pool = ThreadPool.getThreadPool();
        for(int i = 0; i<20; i++){
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    System.out.println(Singleton.getInstance());
                }
            });
            //将线程放到池中执行；
            pool.execute(thread);
        }
        pool.shutdown();
    }

    @Test
    public void test(){
        ExecutorService pool = ThreadPool.getThreadPool();
        for(int i = 0; i<20; i++){
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+"执行。。。。");
                }
            });
            //将线程放到池中执行；
            pool.execute(thread);
        }
        pool.shutdown();
    }

}
