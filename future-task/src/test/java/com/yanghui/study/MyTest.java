package com.yanghui.study;

import com.yanghui.study.task.CountTask;
import com.yanghui.study.task.MyGeneric;
import com.yanghui.study.task.RunnableTask;
import com.yanghui.study.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class MyTest {
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.submit(new RunnableTask());
        Future<List> result = pool.submit(new Task<>());
        log.info(result.get()+"");
    }

    @Test
    public void test(){
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Future<Integer> result = forkJoinPool.submit(new CountTask(1, 100));
        try {
            log.info(result.get()+"");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        MyGeneric<String> generic = new MyGeneric();
        generic.setResult("哈哈");
        //log.info(generic.call());
        MyGeneric<Integer> generic1 = new MyGeneric<>();
        generic1.setResult(1);
        getReturn(generic);
        getReturn(generic1);
        generic.test2("a","g","h");
        generic.test3("c","d");
        MyGeneric<Float> generic2 = new MyGeneric<>();
        generic2.setResult(new Float(2));
        showKey(generic1);
        showKey(generic2);
        //showKey(generic);
        String s1 = "hah30";
        String s2 = "hah2";
        log.info("泛型类型限定："+generic.get(s1,s2));
    }

    @Test
    public void test3() throws ParseException {
        MyGeneric.test4(1);
        MyGeneric.test4("abcyz");
        MyGeneric.test4(null);
        MyGeneric.test5(3,4,7,"5");
        String dateStr = "2019-01-28";
        log.info(MyGeneric.getTheSameDayOfYear(dateStr, -23, String.class)+"");
    }



    public void getReturn(MyGeneric<?> s){
        log.info(s.call()+"");
    }

    public void showKey(MyGeneric<? extends Number> obj){
        log.info("泛型测试：key="+obj.getResult());
    }
}
