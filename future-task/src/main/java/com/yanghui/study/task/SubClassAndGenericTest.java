package com.yanghui.study.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yanghui.study.task.outterPackage.FanDemo;
import com.yanghui.study.task.outterPackage.FanDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 关于泛型和匿名内部类已经fastjson序列化的细节
 * @author YangHui
 */
@Slf4j
public class SubClassAndGenericTest {

    @Test
    public void test0(){
        List<User> users = new ArrayList<>();
        users.add(new User("name1",1));
        users.add(new User("name2",2));
        users.add(new User("name3",3));
        String jsonString = JSON.toJSONString(users);
        List<User> list = JSON.parseObject(jsonString, new TypeReference<List<User>>(){}.getType());
        for(User user : list){
            log.info(user.toString());
        }
    }

    @Test
    public void test() {
        // 转换成List<String>
        String listJsonData1 = "[\"张三\",\"李四\",\"王五\"]";
        List<String> list1 = FastJsonUtil.convertToBean(listJsonData1, new Type[]{String.class}, List.class);
        System.out.println(list1);
    }

    /**
     * 非匿名子类，泛型不同，class相等（泛型擦除）
     */
    @Test
    public void test3() {
        List<String> list1 = new ArrayList<String>();
        List<Integer> list2 = new ArrayList<Integer>();
        System.out.println(list1.getClass() == list2.getClass());
    }

    /**
     * 匿名子类，泛型不同class不等（泛型不擦除的一种场景）
     *
     * 泛型不会被擦除的情形之一】如果一个类实例（list1）是声明的泛型类（ArrayList<String>）的子类时，那么这个类实例（list1）的泛型不会被擦除
     */
    @Test
    public void test4() {
        List<String> list1 = new ArrayList<String>(){}; //new后面加了个 {} 符号
        List<Integer> list2 = new ArrayList<Integer>(){};
        System.out.println(list1.getClass() == list2.getClass());
        log.info(list1.getClass()+"");
        log.info(list2.getClass()+"");

        // 判断list1和list2是否为List的子类或子接口（都为true）
        System.out.println(List.class.isAssignableFrom(list1.getClass()));
        System.out.println(List.class.isAssignableFrom(list2.getClass()));

        // 获取父类声明的类型（java.util.ArrayList<java.lang.String>和java.util.ArrayList<java.lang.Integer>）
        System.out.println(list1.getClass().getGenericSuperclass());
        System.out.println(list2.getClass().getGenericSuperclass());

        // 获取声明的类型参数（class java.lang.String和class java.lang.Integer）
        ParameterizedType type1 = (ParameterizedType) list1.getClass().getGenericSuperclass();
        ParameterizedType type2 = (ParameterizedType) list2.getClass().getGenericSuperclass();
        System.out.println(type1.getActualTypeArguments()[0]);
        System.out.println(type2.getActualTypeArguments()[0]);

    }

    /**
     * 匿名子类的运用
     * ThreadLocal的初始化
     * 其它运用 比如直接new接口，抽象类
     * 又比如 new TypeReference(){};
     * 由于 TypeReference的无参构造方法是protected的，外部（非同一个包或者这个类里面）无法使用(new TypeReference())，不能用它实例化对象，
     * 此时有一种可行的方案就是用匿名子类 -> new TypeReference(){}，在后面加一个 {} 即可
     */
    @Test
    public void test5(){
        ThreadLocal<String> t1 = new ThreadLocal<String>() {
            @Override
            protected String initialValue() {
                return "lalala";
            }
        };
        String s = t1.get();
        log.info(s);
    }


    /**
     * FanDemo构造方法是protected
     * 使用匿名内部类实例化一个实例
     */
    @Test
    public void test6(){
        FanDemo<String> fanDemo = new FanDemo<String>(){};
    }

    /**
     * 多层次泛型的获取
     */
    @Test
    public void test7(){
        Class<?> aClass = new FanDemo<FanDto<String>>() {}.getClass();
        ParameterizedType genericSuperclass = (ParameterizedType)aClass.getGenericSuperclass();
        System.out.println(genericSuperclass);
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        for(Type type : actualTypeArguments){
            ParameterizedType parameterizedType = (ParameterizedType)type;
            System.out.println(parameterizedType.getTypeName());
            Type[] actualTypeArguments1 = parameterizedType.getActualTypeArguments();
            for(Type type1 : actualTypeArguments1){
                System.out.println(type1.getTypeName());
            }
        }
    }

    /**
     * 获取泛型对于字段
     */
    @Test
    public void test8(){
        Class<?> aClass = new FanDto<String>().getClass();
        TypeVariable[] typeParameters = aClass.getTypeParameters();
        for(TypeVariable typeVariable : typeParameters){
            System.out.println(typeVariable);
        }
        Field[] fields = aClass.getDeclaredFields();
        for(Field field : fields){
            System.out.println("field: "+field.getName()+" | 泛型类："+field.getGenericType());
        }
    }

    /**
     * 【泛型不会被擦除的情形之二】
     * （以testGetActualTypeArguments单元测试为例）
     * 如果该泛型类（Map<String, Integer>）
     * 作为另外一个类（ParameterizedTypeTest）的属性，
     * 那么该属性的泛型（String, Integer）不会被擦除
     */
    @Test
    public void testGetActualTypeArguments() throws NoSuchFieldException {
        Field fieldMap = ParameterizedTypeTest.class.getDeclaredField("map");
        Type typeMap = fieldMap.getGenericType();
        ParameterizedType parameterizedTypeMap = (ParameterizedType) typeMap;
        // 获取泛型中的实际类型（class java.lang.String，class java.lang.Integer）
        Type[] types = parameterizedTypeMap.getActualTypeArguments();
        System.out.println(types[0]);
        System.out.println(types[1]);
    }



    class ParameterizedTypeTest<T> {
        private Map<String, Integer> map = null;
    }

}
