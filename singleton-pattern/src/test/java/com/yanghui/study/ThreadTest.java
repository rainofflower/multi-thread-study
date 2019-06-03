package com.yanghui.study;

import com.yanghui.study.bean.Singleton;
import com.yanghui.study.bean.SynchronizedUse;
import com.yanghui.study.bean.VolatileUse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {ThreadPool.class})
@Slf4j
public class ThreadTest {

    //@Autowired
    private ExecutorService threadPool;

    @Test
    public void test05() throws InterruptedException, ExecutionException {
        VolatileUse volatileUse = new VolatileUse();
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<String> result = pool.submit(() ->{
            System.out.println("线程执行中...");
            volatileUse.doWork();
            return "线程处理任务完成";
        });
        for(int i = 1; i>=0; i--){
            Thread.sleep(1000);
            System.out.println(i);
        }
        volatileUse.shutDown();
        System.out.println(result.get());
    }

    @Test
    public void test04(){
        log.info(threadPool+"");
        log.info(Executors.newCachedThreadPool()+"");
        log.info((threadPool==Executors.newCachedThreadPool())+"");
    }

    @Test
    public void test03() throws InterruptedException {
        SynchronizedUse syncBean = new SynchronizedUse();
        ExecutorService pool = Executors.newCachedThreadPool();
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
                log.info("所有的线程都结束了！");
                log.info("计算结果："+syncBean.inc);
                break;
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void test02() throws InterruptedException {
        VolatileUse bean = new VolatileUse();
        ExecutorService pool = Executors.newCachedThreadPool();
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
                log.info("所有的线程都结束了！");
                log.info("计算结果："+bean.inc);
                break;
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void test01() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for(int i = 0; i<20; i++){
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    log.info(Singleton.getInstance()+"");
                }
            });
            //将线程放到池中执行；
            pool.execute(thread);
        }
        pool.shutdown();
        while(true){
            if(pool.isTerminated()){
                log.info("所有的线程都结束了！");
                break;
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void test0() throws InterruptedException {
        for(int i = 0; i<10; i++){
            new Thread(new Runnable(){
                @Override
                public void run() {
                    for(int j = 0;j<100;j++){
                        log.info(Executors.newCachedThreadPool()+"");
                    }
                }
            }).start();
        }
        Thread.sleep(5000);
    }

    @Test
    public void test() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newCachedThreadPool();
        int num = 3;
        int count = 0;
        for(int i = 0; i<num; i++){
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    log.info(Thread.currentThread().getName()+"执行。。。。");
                }
            });
            //将线程放到池中执行；
            Future<?> future = pool.submit(thread);
            if(null == future.get()){
                count++;
                if(count == num){
                    pool.shutdown();
                }
            }
        }
    }

}
