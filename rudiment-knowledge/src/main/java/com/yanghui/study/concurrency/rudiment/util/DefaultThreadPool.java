package com.yanghui.study.concurrency.rudiment.util;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池的简单实现
 */
@Slf4j
public class DefaultThreadPool<T extends Runnable> implements ThreadPool<T> {

    private static final int MAX_WORKER_NUMBERS = 10;

    private static final int MIN_WORKER_NUMBERS = 1;

    private static final int DEFAULT_WORKER_NUMBERS = 5;

    //工作线程list，保证追加和移除线程的同步执行
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());

    //任务list
    private final LinkedList<T> jobs = new LinkedList<>();

    //任务list操作锁
    private final ReentrantLock lock = new ReentrantLock();

    //任务list非空条件
    private Condition notEmpty =  lock.newCondition();

    //生成线程编号
    private AtomicLong threadNum = new AtomicLong();

    public DefaultThreadPool(){
        initWorkers(DEFAULT_WORKER_NUMBERS);
    }

    public DefaultThreadPool(int num){
        int workNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : num < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : num;
        initWorkers(workNum);
    }

    private void initWorkers(int num){
        if(num < 1){
            throw new IllegalArgumentException("工作线程数不能小于1！");
        }
        for(int i = 0; i < num; i++){
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }

    /**
     * 提交任务
     * @param job
     */
    public void execute(T job) {
        if(job == null){
            throw new IllegalArgumentException("任务不能为null");
        }
        lock.lock();
        try{
            jobs.addLast(job);
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 终止线程池所有线程
     */
    public void shutdown() {
        for(Worker worker : workers){
            worker.shutdown();
        }
    }

    public void addWorkers(int num) {
        if(num < 1){
            throw new IllegalArgumentException("新增线程数不能小于1");
        }
        int workSize = workers.size();
        // 限制新增的Worker数量不能超过最大值
        if (num + workSize <= MAX_WORKER_NUMBERS) {
            initWorkers(num);
        }
        else if(num + workSize > MAX_WORKER_NUMBERS && workSize < MAX_WORKER_NUMBERS){
            initWorkers(MAX_WORKER_NUMBERS - workSize);
        }
        else{
            throw new IllegalArgumentException("工作线程数已达到最大值");
        }
    }

    public void removeWorkers(int num) {
        int workSize = workers.size();
        if(num > workSize){
            throw new IllegalArgumentException("工作线程数不足"+num+"个");
        }
        int count = 0;
        while (count < num) {
            Worker worker = workers.get(count);
            if (workers.remove(worker)) {
                worker.shutdown();
                count++;
            }
        }
    }

    public int getJobSize() {
        lock.lock();
        try{
            return jobs.size();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 工作线程
     */
    private final class Worker implements Runnable{

        private volatile boolean running = true;

        public void run() {
            T job;
            while(running){
                lock.lock();
                try{
                    /**
                     * 当前线程收到通知从条件队列转移到同步队列之后需要再次获取锁，
                     * 如果收到通知后的第一次获取锁失败，后续再次获取锁后 jobs 就可能为空了，
                     * 所以在获取链表中的任务之前需要再次检查 任务 是否为空
                     */
                    while(jobs.isEmpty()){
                        notEmpty.await();
                    }
                    job = jobs.removeFirst();
                }catch (InterruptedException e){
                    //响应线程中断，直接返回
                    log.info(Thread.currentThread().getName()+"线程被中断而结束");
                    return;
                }finally {
                    lock.unlock();
                }
                try{
                    job.run();
                }catch (Throwable e){
                    //忽略任务执行中的异常
                }
            }
        }

        public void shutdown(){
            running = false;
        }
    }
}
