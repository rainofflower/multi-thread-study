package com.yanghui.study.concurrency.rudiment;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 在Java里面参数传递都是按值传递,没有按引用传递！
 */
@Slf4j
public class ValueReference {

    /**
     * 基本数据类型传递
     */
    public static void basicDataTransfer(){
        int num = 5;
        log.info(num+"");
        changeValue(num);
        log.info(num+"");
    }

    /**
     * 基本数据类型修改
     * @param x 基本数据类型
     */
    public static void changeValue(int x){
        /**
         * num作为参数传递给changeValue()方法时，是将内存空间中num所指向的那个存储单元中存放的值，
         * 即"5",传送给了changeValue()方法中的x变量，而这个x变量也在内存空间中分配了一个存储单元，
         * 这个时候，就把num的值5传送给了这个存储单元中。
         * 此后，在changeValue()方法中对x的一切操作都是针对x所指向的这个存储单元，与num所指向的那个存储单元没有关系了！
         */
        x = 4;
    }

    /**
     * 对象传递（引用传递）
     */
    public static void referenceTransfer(){
        /**
         * new 创建一个对象Person，实际分配了两个对象：新创建的Person类的实体对象，和指向该对象的引用变量person
         * 【注意：在java中，新创建的实体对象在堆内存中开辟空间，而引用变量在栈内存中开辟空间】
         */
        Person person = new Person();
        person.setAge(20);
        log.info(person.getAge()+"");
        log.info("person地址："+person);
        changeReference(person);
        log.info(person.getAge()+"");
        log.info("person地址："+person);
    }

    /**
     * 修改对象属性值
     * @param p 对象引用
     */
    public static void changeReference(Person p){
        /**
         *
         * 传递引用的地址值，在栈中会为引用变量 p 分配一个新的存储单元用于存储指向堆中对象的地址，
         * 这个新的存储单元（引用变量）与实参（也是一个引用变量）的存储单元完全独立不相干，
         * 要说有什么共同点，那就是两者刚开始的时候存储的内容相同，都是同一个对象的地址，指向同一个对象，
         * 如果后面两者存储的内容（也就是对象的地址值）不变，那么两者使用该值对对象进行修改，被修改的对象都是同一个，
         * 但是如果两者存储的内容发生变化，那么两者对引用地址指向的对象的修改就是在修改不同的对象了
         *
         * 所以如果下方执行 p = new Person()，那么 p 存储的内容将会修改成新对象的地址
         * 否则，p 中存储的内容依旧是传递过来的对象地址值（实参的值）
         *
         * 实参：调用方法时传递的参数
         * 形参：方法声明中的参数
         */
        //p = new Person();
        log.info("p地址："+p);
        p.setAge(18);
    }

    public static void test(){
        Person q = new Person();
        Person p = q;

        Person r1 = p;
        int r2 = r1.getAge();
        log.info(p.getAge()+"");
        Person r6 = p;
        r6.setAge(3);
        Person r3 = q;
        int r4 = r3.getAge();
        log.info(p.getAge()+"");
        int r5 = r2;
        log.info(p.getAge()+"");
    }

    /**
     * 传递对象到其它方法中进行交换是不会成功的（和Integer内的缓存无关）
     * 可以参照changeReference(Person p)方法里的解析
     */
    @Test
    public void swapIntegerTest(){
        //栈中有两个引用变量a和b，堆中默认已经分配了-128到127一共256个对象，a和b指向堆中对应的对象
        Integer a = 1;
        Integer b = 2;
        /**
         * 调用swapInteger()方法，会将实参的值传递到方法形参的变量中，实参a -> 形参a；实参b -> 形参b
         * 注意，调用方法后，方法里会为形参在栈中分配新的空间，而不是共用实参的栈空间，也就是说形参和实参互不影响，
         * 传递过去后，方法形参指向和实参相同的堆空间，相同的对象，
         * 如果在方法中修改了形参指向的对象，那也就只是形参指向的对象发生了变化，实参仍然指向原来的对象。
         */
        swapInteger(a,b);
        log.info("a={}, b={}",a,b);
    }

    void swapInteger(Integer a, Integer b){
        log.info("a={}, b={}",a,b);
        Integer c = a;
        a = b;
        b = c;
        log.info("a={}, b={}",a,b);
    }

    /**
     * 同SwapInteger
     */
    @Test
    public void swapReferenceTest(){
        Person person1 = new Person();
        Person person2 = new Person();
        log.info("person1:{}，person2:{}",person1,person2);
        swapReference(person1,person2);
        log.info("person1:{}，person2:{}",person1,person2);
    }

    public void swapReference(Person p1, Person p2){
        log.info("p1:{}，p2:{}",p1,p2);
        Person temp = p1;
        p1 = p2;
        p2 = temp;
        log.info("p1:{}，p2:{}",p1,p2);
    }


    /**
     * boxing (autoboxing) 自动装箱
     * unboxing 自动拆箱
     */
    @Test
    public void integerBoxing(){
        //使用等号创建Integer对象，代码编译结果为Integer a = Integer.valueOf(1); -> 自动装箱
        Integer a = 1;
        Integer b = 128;
        //使用Integer对象给int赋值，代码编译结果为 int c = a.intValue()； -> 自动拆箱
        int c = a;
        //使用等号判断Integer与基本数据类型int是否相等，代码编译结果为 a.intValue() == 1; -> 自动拆箱
        log.info(""+(a.intValue() == 1));
        log.info(""+(a == 1));                  //true
        log.info(""+(a==new Integer(1)));//false
        log.info(""+(a==Integer.valueOf(1)));   //true
        log.info(""+(a.equals(1)));             //true
        log.info("-------------------");
        log.info(""+(b == 128));                //true
        log.info(""+(b==new Integer(128)));//false
        log.info(""+(b.equals(128)));             //true
    }

    /**
     * 使用valueOf或者自动装箱操作的到的对象是对象池中的对象，手动new的对象则每次不一样。
     * 见Boolean.valueOf()源码
     */
    @Test
    public void booleanBoxing(){
        Boolean a_B = new Boolean(true);
        Boolean b_B = new Boolean(true);
        Boolean c_B = true;
        boolean d_b = true; //和d_b做 == 运算时，会自动将对象拆箱做等值运算
        Boolean e_B = Boolean.valueOf(true);
        System.out.println(a_B == b_B); //false
        System.out.println(a_B == c_B); //false
        System.out.println(d_b == c_B); //true
        System.out.println(a_B == d_b); //true
        System.out.println(c_B == e_B); //true
    }

    /**
     * Double、Float每次装箱都返回不同的对象实例
     */
    @Test
    public void doubleFloatBoxing(){
        Float a = 1.0f;
        Float b = 1.0f;
        System.out.println(a == b); //false
    }

    /**
     * 非浮点数，字符
     * Integer、Short、Byte、Character、Long这几个类的valueOf方法的实现是类似的。
     * 它们在运行时分为两种情况，一种是有对应的等值对象存放于对象池中，一种是没有。
     * Integer/Short/Long 有等值对象的范围都是 [-128 ,127]
     * Character 有等值对象的范围 是 x < 128
     *
     * 自动装箱或者调用valueOf()时
     * 1、对象池中有对应等值对象
     * 此时将会返回此对象池中的等值对象。
     *
     * 2、没有对应等值对象
     * 此时会返回一个新的对象，所以每次返回的对象都是不相等的。
     *
     * --> null拆箱问题
     *
     * Float f = null;
     *  //编译无错，运行时因调用f.intValue()是发现对象f是null，会报错
     * float ff = f;
     * System.out.println(ff);
     *
     * --> 拆箱/装箱规律
     * 包装类型和基础数据类型做算术运算/等值运算时，包装类型会自动拆箱
     * 调用equals方法时，会将基础数据类型做自动装箱
     *
     */
}
