package com.yanghui.study.concurrency.rudiment;

/**
 *  接口使用细则
 *  接口中的所有属性和方法都必须是public的（public可省略不写，接口隐式指定）
 */
public interface Map<K,V> {

    /**
     * 接口中的属性必须是static final的(这两修饰符可省略,其实public也省略了)
     * 类可以被实例化，实例化的时候创建的对象里面的属性就会被赋初始值。比如String 是 null int是0，double是0.0。但是接口呢？
     * 接口不能被实例化，所以接口里面的变量的无法像类一样被初始化，只能指定为final属性，并赋予初始值。所以接口里面的值必须是常量final而且一定是static,
     * 接口中的属性对所有实现类只有一份（static）
     */
    int capacity = 2;

    /**
     *  接口中的非default、static方法一定是 public abstract 方法
     *  public和 abstract可省略
     */
    V put(K key, V value);

    V get(K key);

    /**
     * jdk1.8之后接口中的方法可以提供默认实现，
     * 并且对于有默认（default）实现的方法，实现该接口的类无需再次实现该方法，
     * 当然也可以选择重写默认实现，【这一特性使得接口可以像抽象类一样使用】
     * jdk中的 Map、List、Comparator等接口就有许多default方法
     *
     * 默认方法还可以直接调用接口中的静态方法，直接使用接口中的属性(也是static的)
     *
     * 在接口中新添加default方法将不会破坏现有代码(无需在所有实现该接口的实现类中加上新加的方法的实现)
     */
    default int size(){
        //return capacity;
        return method();
    }

    /**
     * 接口内可声明静态方法,必须提供具体实现
     */
    static int method(){
        return capacity;
    }

    /**
     *  接口内部接口,内部方法一定是static的，不过static关键字可以省略
     */
    interface Entry<K,V>{

        K getKey();

        V getValue();

        V setValue(V value);

        int hash();
    }

     class InterClass{

    }
}
