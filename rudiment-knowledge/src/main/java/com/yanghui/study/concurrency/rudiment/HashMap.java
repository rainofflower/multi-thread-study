package com.yanghui.study.concurrency.rudiment;

/**
 *  接口实现类使用细则
 */
public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {

    /**
     *  接口中未给出默认实现的方法在实现类中必须提供实现
     */
    public V put(K key, V value) {
        return value;
    }

    /**
     *  抽象类中未给出默认实现的方法在实现类中必须提供实现
     */
    public Map.Entry<K, V> getEntry(K key) {
        return null;
    }

    public V get(Object key) {
        return null;
    }

    /**
     *  default方法只能在接口声明
     */
//    default void defaultM(){
//       //
//    }

    /**
     *  实现类可自由选择是否重写接口里的 default 方法
     */
//    public int size(){
//       return capacity;
//    }

    static class Node<K,V> implements Map.Entry<K,V>{

        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next){
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            this.value = value;
            return value;
        }

        public int hash() {
            return 0;
        }
    }
}
