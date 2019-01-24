package com.yanghui.study.task;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class MyGeneric<T> implements Generic<T>{
    
    public static final String DATE_FMT_YYYY_MM_DD = "yyyy-MM-dd";
    
    public static final String DATE_FMT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS= "yyyy-MM-dd HH:mm:ss.SSS";

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
    
    /**
     * 获取指定一个日期的天环比日期
     * @param date 指定的基准日期(支持String和java.util.Date)
     * @param clazz 需要返回的类型(支持String.class和java.util.Date.class)
     * @param <T>
     * @param <E>
     * @return	clazz类型日期
     * @throws RuntimeException
     */
	@SuppressWarnings("unchecked")
	public static <T,E> E getDayRingRatioDate(T date, Class<E> clazz) {
		Calendar calendar = Calendar.getInstance(); 
		SimpleDateFormat dateFormat = null;
        if(date == null){
            return null;
        }
        if(date instanceof String){
        	String format;
        	int length = ((String) date).length();
        	if (length == 10) {
                format = DATE_FMT_YYYY_MM_DD;
            } else if (length == DATE_FMT_YYYY_MM_DD_HH_MM_SS.length()) {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS;
            } else {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS;
            }
        	dateFormat = new SimpleDateFormat(format);
            try {
				calendar.setTime(dateFormat.parse((String) date));
			} catch (ParseException e) {
				throw new RuntimeException("该日期参数无法转化为日期："+date);
			}
        }
        if(date instanceof Date){
            calendar.setTime((Date) date);
            dateFormat = new SimpleDateFormat(DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS);
        }
        if(!(date instanceof String || date instanceof Date)){
            throw new RuntimeException("该类型无法转换");
        }
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) - 1);
        if(clazz == String.class){
            return (E) dateFormat.format(calendar.getTime());
        }
        if(clazz == Date.class){
            return (E) calendar.getTime();
        }
        throw new RuntimeException("暂不支持转换为该类型："+clazz);
	}
	
	/**
     * <p>获取指定一个日期的月环比日期</p>
     * 
     * 注：如果传入的date参数处于一个月的最后一天，<br>
     *	不管是yyyy-MM-dd、yyyy-MM-dd HH:mm:ss、yyyy-MM-dd HH:mm:ss.SSS格式字符串<br>
     *	还是java.util.Date对象，都会返回上一个月的最后一天<br>
     *	否则，返回上一个月的相同一天(见下面的例子3)，即对月末具有吸引力>_< <br>
     *	eg.
     * <pre>
     * 1、DateUtils.getMonthRingRatioDate("2019-02-28",String.class) => 返回 "2019-01-31"字符串
     * 2、DateUtils.getMonthRingRatioDate("2019-04-30 23:59:59",Date.class) => 返回 java.util.Date类型的3月 31日23:59:59
     * 3、DateUtils.getMonthRingRatioDate(2019年1月24日零时的Date对象,String.class) => 返回 "2018-12-24 00:00:00.000"字符串
     * </pre>
     * @param date 指定的基准日期(支持String和java.util.Date)
     * @param clazz 需要返回的类型(支持String.class和java.util.Date.class)
     * @param <T>
     * @param <E>
     * @return	clazz类型日期
     * @throws RuntimeException
     */
	@SuppressWarnings("unchecked")
	public static <T,E> E getMonthRingRatioDate(T date, Class<E> clazz) {
		Calendar calendar = Calendar.getInstance(); 
		boolean endDateFlag = false;
		SimpleDateFormat dateFormat = null;
        if(date == null){
            return null;
        }
        if(date instanceof String){
        	String format;
        	int length = ((String) date).length();
        	if (length == 10) {
                format = DATE_FMT_YYYY_MM_DD;
            } else if (length == DATE_FMT_YYYY_MM_DD_HH_MM_SS.length()) {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS;
            } else {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS;
            }
        	dateFormat = new SimpleDateFormat(format);
            try {
				calendar.setTime(dateFormat.parse((String) date));
			} catch (ParseException e) {
				throw new RuntimeException("该日期参数无法转化为日期："+date);
			}
            if(date.equals(getEndDateOfMonth(date, 0, String.class).substring(0, length))) {
            	endDateFlag = true;
            }
        }
        if(date instanceof Date){
            calendar.setTime((Date) date);
            dateFormat = new SimpleDateFormat(DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS);
            if(((Date) date).compareTo(getEndDateOfMonth(date, 0, Date.class))==0) {
            	endDateFlag = true;
            }
        }
        if(!(date instanceof String || date instanceof Date)){
            throw new RuntimeException("该类型无法转换");
        }
        calendar.add(Calendar.MONTH, -1);
        if(endDateFlag) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        else {
        	calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        }
        if(clazz == String.class){
            return (E) dateFormat.format(calendar.getTime());
        }
        if(clazz == Date.class){
            return (E) calendar.getTime();
        }
        throw new RuntimeException("暂不支持转换为该类型："+clazz);
	}
	
	/**
     * 获取指定一个日期的同比日期
     * @param date 指定的基准日期(支持String和java.util.Date)
     * @param clazz 需要返回的类型(支持String.class和java.util.Date.class)
     * @param <T>
     * @param <E>
     * @return	clazz类型日期
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
	public static <T,E> E getYearOnYearDate(T date, Class<E> clazz) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = null;
        if(date == null){
            return null;
        }
        if(date instanceof String){
        	String format;
        	int length = ((String) date).length();
        	if (length == 10) {
                format = DATE_FMT_YYYY_MM_DD;
            } else if (length == DATE_FMT_YYYY_MM_DD_HH_MM_SS.length()) {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS;
            } else {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS;
            }
        	dateFormat = new SimpleDateFormat(format);
            try {
				calendar.setTime(dateFormat.parse((String) date));
			} catch (ParseException e) {
				throw new RuntimeException("该日期参数无法转化为日期："+date);
			}
        }
        if(date instanceof Date){
            calendar.setTime((Date) date);
            dateFormat = new SimpleDateFormat(DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS);
        }
        if(!(date instanceof String || date instanceof Date)){
            throw new RuntimeException("该类型无法转换");
        }
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
        if(clazz == String.class){
            return (E) dateFormat.format(calendar.getTime());
        }
        if(clazz == Date.class){
            return (E) calendar.getTime();
        }
        throw new RuntimeException("暂不支持转换为该类型："+clazz);
    }
    
    /**
     * 获取某一个日期往前/往后 n个月第一天零毫秒的日期
     * @param date 指定的基准日期(支持String和java.util.Date)
     * @param clazz 需要返回的类型(支持String.class和java.util.Date.class)
     * @param n 往前/往后多少个月
     * @param <T>
     * @param <E>
     * @return	clazz类型日期
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
	public static <T,E> E getBeginDateOfMonth(T date, int n, Class<E> clazz) {
        Calendar calendar = Calendar.getInstance();
        if(date == null){
            return null;
        }
        if(date instanceof String){
        	String format;
        	int length = ((String) date).length();
        	if (length == 10) {
                format = DATE_FMT_YYYY_MM_DD;
            } else if (length == DATE_FMT_YYYY_MM_DD_HH_MM_SS.length()) {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS;
            } else {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS;
            }
        	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            try {
				calendar.setTime(dateFormat.parse((String) date));
			} catch (ParseException e) {
				throw new RuntimeException("该日期参数无法转化为日期："+date);
			}
        }
        if(date instanceof Date){
            calendar.setTime((Date) date);
        }
        if(!(date instanceof String || date instanceof Date)){
            throw new RuntimeException("该类型无法转换");
        }
        calendar.add(Calendar.MONTH, n);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(clazz == String.class){
            return (E) new SimpleDateFormat(DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS).format(calendar.getTime());
        }
        if(clazz == Date.class){
            return (E) calendar.getTime();
        }
        throw new RuntimeException("暂不支持转换为该类型："+clazz);
    }
    
    /**
     * 获取某一个日期往前/往后 n个月最后一天最后一毫秒的日期
     * @param date 指定的基准日期(支持String和java.util.Date)
     * @param clazz 需要返回的类型(支持String.class和java.util.Date.class)
     * @param n 往前/往后多少个月
     * @param <T>
     * @param <E>
     * @return	clazz类型日期
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
	public static <T,E> E getEndDateOfMonth(T date, int n, Class<E> clazz) {
        Calendar calendar = Calendar.getInstance();
        if(date == null){
            return null;
        }
        if(date instanceof String){
        	String format;
        	int length = ((String) date).length();
        	if (length == 10) {
                format = DATE_FMT_YYYY_MM_DD;
            } else if (length == DATE_FMT_YYYY_MM_DD_HH_MM_SS.length()) {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS;
            } else {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS;
            }
        	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            try {
				calendar.setTime(dateFormat.parse((String) date));
			} catch (ParseException e) {
				throw new RuntimeException("该日期参数无法转化为日期："+date);
			}
        }
        if(date instanceof Date){
            calendar.setTime((Date) date);
        }
        if(!(date instanceof String || date instanceof Date)){
            throw new RuntimeException("该类型无法转换");
        }
        calendar.add(Calendar.MONTH, n);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        if(clazz == String.class){
            return (E) new SimpleDateFormat(DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS).format(calendar.getTime());
        }
        if(clazz == Date.class){
            return (E) calendar.getTime();
        }
        throw new RuntimeException("暂不支持转换为该类型："+clazz);
    }
	
    /**
     * 获取某一个日期往前/往后 n年第一天零毫秒的日期
     * @param date 指定的基准日期(支持String和java.util.Date)
     * @param clazz 需要返回的类型(支持String.class和java.util.Date.class)
     * @param n 往前/往后多少年
     * @param <T>
     * @param <E>
     * @return	clazz类型日期
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
	public static <T,E> E getBeginDateOfYear(T date, int n, Class<E> clazz) {
        Calendar calendar = Calendar.getInstance();
        if(date == null){
            return null;
        }
        if(date instanceof String){
        	String format;
        	int length = ((String) date).length();
        	if (length == 10) {
                format = DATE_FMT_YYYY_MM_DD;
            } else if (length == DATE_FMT_YYYY_MM_DD_HH_MM_SS.length()) {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS;
            } else {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS;
            }
        	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            try {
				calendar.setTime(dateFormat.parse((String) date));
			} catch (ParseException e) {
				throw new RuntimeException("该日期参数无法转化为日期："+date);
			}
        }
        if(date instanceof Date){
            calendar.setTime((Date) date);
        }
        if(!(date instanceof String || date instanceof Date)){
            throw new RuntimeException("该类型无法转换");
        }
        calendar.add(Calendar.YEAR, n);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(clazz == String.class){
            return (E) new SimpleDateFormat(DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS).format(calendar.getTime());
        }
        if(clazz == Date.class){
            return (E) calendar.getTime();
        }
        throw new RuntimeException("暂不支持转换为该类型："+clazz);
    }
    
    /**
     * 获取某一个日期往前/往后 n年最后一天最后一毫秒的日期
     * @param date 指定的基准日期(支持String和java.util.Date)
     * @param clazz 需要返回的类型(支持String.class和java.util.Date.class)
     * @param n 往前/往后多少年
     * @param <T>
     * @param <E>
     * @return clazz类型日期
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
	public static <T,E> E getEndDateOfYear(T date, int n, Class<E> clazz) {
        Calendar calendar = Calendar.getInstance();
        if(date == null){
            return null;
        }
        if(date instanceof String){
        	String format;
        	int length = ((String) date).length();
        	if (length == 10) {
                format = DATE_FMT_YYYY_MM_DD;
            } else if (length == DATE_FMT_YYYY_MM_DD_HH_MM_SS.length()) {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS;
            } else {
                format = DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS;
            }
        	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            try {
				calendar.setTime(dateFormat.parse((String) date));
			} catch (ParseException e) {
				throw new RuntimeException("该日期参数无法转化为日期："+date);
			}
        }
        if(date instanceof Date){
            calendar.setTime((Date) date);
        }
        if(!(date instanceof String || date instanceof Date)){
            throw new RuntimeException("该类型无法转换");
        }
        calendar.add(Calendar.YEAR, n+1);
        calendar.set(Calendar.DAY_OF_YEAR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        if(clazz == String.class){
            return (E) new SimpleDateFormat(DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS).format(calendar.getTime());
        }
        if(clazz == Date.class){
            return (E) calendar.getTime();
        }
        throw new RuntimeException("暂不支持转换为该类型："+clazz);
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
