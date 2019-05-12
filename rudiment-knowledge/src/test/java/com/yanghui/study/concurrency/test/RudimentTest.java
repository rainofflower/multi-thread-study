package com.yanghui.study.concurrency.test;

import com.yanghui.study.concurrency.rudiment.*;
import com.yanghui.study.concurrency.rudiment.AbstractMap;
//import com.yanghui.study.concurrency.rudiment.HashMap;
//import com.yanghui.study.concurrency.rudiment.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

@Slf4j
public class RudimentTest {

    @Test
    public void test1() throws InterruptedException {
        ConcurrencyTest.concurrency();
        ConcurrencyTest.serial();
    }

    @Test
    public void test2() throws InterruptedException {
        DeadLockDemo.deadLock();
    }

    @Test
    public void test3() throws InterruptedException {
        SynchronizedTest.add1();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedTest.add1();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                SynchronizedTest.add2();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void test4() throws InterruptedException {
        SynchronizedTest s = new SynchronizedTest();
        SynchronizedTest.InnerClass innerClass1 = s.new InnerClass();
        SynchronizedTest.InnerClass innerClass2 = s.new InnerClass();
        SynchronizedTest.InnerClass innerClass3 = s.new InnerClass();
        synchronized (innerClass1){
            innerClass3.method();
        }
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (innerClass1){
                    innerClass1.method();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (innerClass1){
                    innerClass2.method();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void test5(){
        //ValueReference.basicDataTransfer();
        ValueReference.referenceTransfer();
    }

    @Test
    public void test6(){
        ValueReference.test();
    }

    @Test
    public void test7() throws Exception{
        /**
         * UnsatisfiedLinkError
         */
        //new MemoryModel().jni();
//       MemoryModel memoryModel = new MemoryModel();//.thread1();
//        log.info(memoryModel.getX()+"");
//        MemoryModel memoryModel = new MemoryModel(1);
//        log.info(memoryModel.f()+"");

        log.info(MemoryModel.map.get("name"));
        /**
         * 以下为使用反射修改static final属性值（底层使用 Unsafe 类实现）
         */
        Field z = MemoryModel.class.getDeclaredField("map");
//        z.setAccessible(true);
//
//        Field modifiers = Field.class.getDeclaredField("modifiers");
//        modifiers.setAccessible(true);
//        modifiers.setInt(z, z.getModifiers() & ~Modifier.FINAL);
        Map<String, String> newMap = new HashMap<>();
        newMap.put("name","sunshine");
//        z.set(null, newMap);
//        modifiers.setInt(z, z.getModifiers() &~Modifier.FINAL);
//        log.info(MemoryModel.map.get("name"));
//        int i = 3;
//        // ~符号表示取反，即二进制中值取反（0和1互换）
//        i = ~3;
//        log.info(i+"");

        /**
         * 直接使用 Unsafe 修改static final属性
         */
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        long offset = unsafe.staticFieldOffset(z);
        unsafe.putObject(MemoryModel.class, offset, newMap);
        log.info(MemoryModel.map.get("name"));
    }

    @Test
    public void test8() throws InterruptedException {
        Thread t1 = new Thread(()->{
            MemoryModel.writer();
        });
        Thread t2 = new Thread(() ->{
            MemoryModel.reader();
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    /**
     * 使用LockSupport工具类实现多线程同步（线程间精确同步）
     */
    @Test
    public void test9() {
        ExecutorService pool = Executors.newCachedThreadPool();
        Map<String,Thread> map = new ConcurrentHashMap<>();
        /**
         * 假设t1依赖于t3,t2依赖t3,t4依赖t1
         */
        Thread t1 = new Thread(()->{
            map.put("t1",Thread.currentThread());
            log.info("t1 启动...");
            LockSupport.park();
            log.info("t1 开始执行...");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            log.info("t1 执行完成...");
            LockSupport.unpark(map.get("t4"));
        });
        Thread t2 = new Thread(() ->{
            map.put("t2",Thread.currentThread());
            log.info("t2 启动...");
            LockSupport.park();
            log.info("t2 开始执行...");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            log.info("t2 执行完成...");
            //LockSupport.unpark(map.get("t3"));
        });
        Thread t3 = new Thread(() ->{
            map.put("t3",Thread.currentThread());
            log.info("t3 启动...");
            //LockSupport.park();
            log.info("t3 开始执行...");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            log.info("t3 执行完成...");
            LockSupport.unpark(map.get("t1"));
            LockSupport.unpark(map.get("t2"));
        });
        Thread t4 = new Thread(() ->{
            map.put("t4",Thread.currentThread());
            log.info("t4 启动...");
            LockSupport.park();
            log.info("t4 开始执行...");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            log.info("t4 执行完成...");
        });
        pool.submit(t4);
        pool.submit(t1);
        pool.submit(t2);
        //LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        pool.submit(t3);
        pool.shutdown();
        while(!pool.isTerminated()){
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        }
    }

    /**
     * 接口和抽象类无法实例化，但是依然可以使用 new 关键字
     */
    @Test
    public void test10(){
        com.yanghui.study.concurrency.rudiment.Map map = new com.yanghui.study.concurrency.rudiment.Map() {

            public Object put(Object key, Object value) {
                return null;
            }

            public Object get(Object key) {
                return null;
            }
        };
        log.info("new Map接口返回的引用调用size方法："+map.size());
        log.info("Map接口调用静态method方法："+com.yanghui.study.concurrency.rudiment.Map.method());

        com.yanghui.study.concurrency.rudiment.HashMap<String, String> hashMap = new com.yanghui.study.concurrency.rudiment.HashMap<>();
        log.info("HashMap实例调用Map接口中的default size方法："+hashMap.size());
        log.info("HashMap实例中获取Map接口中的capacity静态属性："+hashMap.capacity);

        AbstractMap<String, String> abstractMap = new AbstractMap<String, String>() {

            public String get(String key) {
                return null;
            }

            protected Entry<String, String> getEntry(String key) {
                return null;
            }
        };
        log.info("new AbstractMap返回的引用调用size方法："+abstractMap.size());
        log.info("AbstractMap调用静态staticMethod方法："+AbstractMap.staticMethod());
    }

    @Test
    public void test11(){
        Person person = new Person();
        log.info(person.f+"");
    }

    /**
     * HashMap源码解析
     */
    @Test
    public void test12(){
        final int MAXIMUM_CAPACITY = 1 << 30;
        //float double 强转为 int 会直接舍弃掉小数位
//        float a1 = 10.75f;
//        int a2 = (int) a1;
//        float b1 = 10.49f;
//        int b2 = (int) b1;
//        double c1 = 4.50d;
//        int c2 = (int) c1;

        int b = (1 << 31) - 1;
        boolean flag = Integer.MAX_VALUE == b;
        int cap = 255;
        int n = cap - 1;
        //右移 1 位再与移位前的值做异运算，结果就是最高位的 1 覆盖到第二高位（如果 n 的二进制数有两位的话，否则这步运算以及之后的几步都不会改变 n 的值），此时前两位都为 1
        n |= n >>> 1;
        //右移 2 位再与移位前的值做异运算，结果就是前两位的 1 覆盖到 第三、四高位，此时前四位都为 1
        n |= n >>> 2;
        //以此类推
        n |= n >>> 4;
        n |= n >>> 8;
        //到此步，即使给定的 n 的二进制有32位，右移16位再与移位前的值做异运算，结果也是32位都是 1 （也就是2^32次幂-1）
        n |= n >>> 16;
        //若 n大于0，进行加1操作，n变成2的幂
        int threshold = (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;

        Map<String, String> map = new HashMap<>(1);
//        Map<String, String> map = new HashMap<>();
        //测试用
        String s = null;
        //int num = 100000000;
        int num = 100;
        for(int i = 0; i<num;++i){
            String key = UUID.randomUUID().toString();
            if(i == 90){
                s = key;
            }
            map.put(key,i+"");
        }
        String value = map.get(s);
    }

    /**
     * ConcurrentHashMap源码解析
     */
    @Test
    public void test13(){
        int initialCapacity = (1 << 6) + (1 << 5) + 1;
        int a = initialCapacity + (initialCapacity >>> 1) + 1;
        int table,tab,nt = 2;
        //从右向左赋值
        table = tab = nt;
        Map<String, String> map = new ConcurrentHashMap<>();
        //测试用
        String s = null;
        //int num = 100000000;
        int num = 100;
        for(int i = 0; i<num;++i){
            String key = UUID.randomUUID().toString();
            if(i == 90){
                s = key;
            }
            map.put(key,i+"");
        }
        String value = map.get(s);
    }

    @Test
    public void test14() throws Exception{
        List<Person> list1 = new ArrayList<>();
        List<Person> list2 = new ArrayList<>();
        Person person;
        for(int i = 0; i<1000000; ++i){//1000000
            person = new Person(i, "niko"+i);
            list1.add(person);
            person = new Person(i+1, "niko"+i);
            list2.add(person);
        }
//        long start1 = System.currentTimeMillis();
//        for(Person p1 : list1){
//            for (Person p2 : list2){
//                if(p1.getName().equals(p2.getName())){
//                    p1.setF(p1.getAge()+p2.getAge());
//                    break;
//                }
//            }
//        }
//        log.info(System.currentTimeMillis() - start1 + "");
        log.info("stream");
        long start2 = System.currentTimeMillis();
        Map<String, Person> map = list2.stream().collect(Collectors.toMap(i -> i.getName(), i -> i, (oldValue, newValue) -> oldValue));//, (oldValue, newValue) -> oldValue
        for(Person p1 : list1){
            Person p2 = map.get(p1.getName()); //list1 和 list2匹配，关键 map 的 key
            if(p2 != null){
                p1.setF(p1.getAge()+p2.getAge());
            }
        }

        log.info(System.currentTimeMillis() - start2 + "");
    }

    @Test
    public void test15(){
        final List<Map<String, Object>> list1 = new ArrayList<>();
        List<Map<String, Object>> list2 = new ArrayList<>();
        Map<String, Object> temp;
        for(int i = 0; i<1000000; ++i){//1000000
            temp = new HashMap<>();
            temp.put("age", i);
            temp.put("name", "niko"+i);
            list1.add(temp);
            temp = new HashMap<>();
            temp.put("age", i+1);
            temp.put("name", "niko"+i);
            list2.add(temp);
        }
//        long start1 = System.currentTimeMillis();
//        for(Person p1 : list1){
//            for (Person p2 : list2){
//                if(p1.getName().equals(p2.getName())){
//                    p1.setF(p1.getAge()+p2.getAge());
//                    break;
//                }
//            }
//        }
//        log.info(System.currentTimeMillis() - start1 + "");
        log.info("stream");
        long start2 = System.currentTimeMillis();
        Map<String,Map<String, Object>> map = list2.stream().collect(Collectors.toMap(i -> i.get("name").toString(), i -> i, (oldValue, newValue) -> oldValue));//, (oldValue, newValue) -> oldValue
        for(Map<String, Object> p1 : list1){
            Map<String, Object> p2 = map.get(p1.get("name")); //list1 和 list2 匹配，关键 map 的 key
            if(p2 != null){
                p1.put("total", Integer.parseInt(p1.get("age").toString()) + Integer.parseInt(p2.get("age").toString()));
            }
        }
        log.info(System.currentTimeMillis() - start2 + "");
    }
}
