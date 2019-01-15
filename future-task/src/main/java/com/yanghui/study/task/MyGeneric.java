package com.yanghui.study.task;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class MyGeneric<T> implements Generic<T>{

    private T result;

    public MyGeneric(){

    }

    public MyGeneric(T t){
        this.result = t;
    }

    @Override
    public T call() {
        return result;
    }

    @Override
    public <E> E genericMethod1() {
        return null;
    }

    @Override
    public <K, V> K genericMethod2(K key) {
        return key;
    }

    @Override
    public <T extends Comparable<T>> T get(T t1, T t2) {
        if(t1.compareTo(t2)>=0){
            return t1;
        }
        return t2;
    }

    /**
     * 泛型的灵活运用
     * 获取指定日期 前/后 n 年的日期
     * @param date 指定的基准日期
     * @param n 前/后n年
     * @param clazz 需要返回的类型
     * @param <T>
     * @param <E>
     * @return
     * @throws ParseException
     */
    public static <T,E> E getTheSameDayOfYear(T date, int n, Class<E> clazz) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(date == null){
            date = (T) new Date();
        }
        if(date instanceof String){
            calendar.setTime(dateFormat.parse((String) date));
        }
        if(date instanceof Date){
            calendar.setTime((Date) date);
        }
        if(!(date instanceof String || date instanceof Date)){
            throw new RuntimeException("该类型无法转换");
        }
        calendar.add(Calendar.YEAR, n);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
        if(clazz == String.class){
            return (E) dateFormat.format(calendar.getTime());
        }
        if(clazz == Date.class){
            return (E) calendar.getTime();
        }
        throw new RuntimeException("暂不支持转换为该类型");
    }

    public static <E> void test5(E... t){
        for (E temp: t) {
            log.info(temp.toString());
        }
    }

    public static <T> void test4(T t){
        if(null == t){
            log.info("null");
        }
        if(t instanceof String){
            log.info("String类型:长度="+((String) t).length());
        }
        if(t instanceof Number){
            log.info("Number类型:值="+((Number) t).longValue());
        }
    }

    public void test3(T... t){
        for (T temp:t) {
            log.info(temp.toString());
        }
    }

    public void test2(String... s){
        int size = s.length;
        log.info("参数长度:"+size);
        StringBuffer stringBuffer = new StringBuffer();
        for (String temp:s) {
            stringBuffer.append(temp+"-");
        }
        log.info("参数拼接结果："+stringBuffer.deleteCharAt(stringBuffer.length()-1).toString());
    }

    public void test(T t){
        t.toString();
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
