package com.yanghui.study.task;

import java.util.concurrent.*;

/**
 * Fork/Join框架的使用
 *
 */
public class CountTask extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 10;

    private int start;

    private int end;

    public CountTask(int start, int end){
        this.start = start;
        this.end = end;
    }

    protected Integer compute() {
        int sum = 0;
        if(end - start <= THRESHOLD){
            for(int i = start;i <= end;i++){
                sum += i;
            }
        }else{
            int middle = (end + start)/2;
            CountTask leftTask = new CountTask(start, middle);
            CountTask rightTask = new CountTask(middle + 1, end);
            leftTask.fork();
            rightTask.fork();
            Integer r1 = leftTask.join();
            Integer r2 = rightTask.join();
            sum = r1 + r2;
        }
        return sum;
    }
}
