package com.yanghui.study.task.outterPackage;

/**
 * @author YangHui
 */
public class FanDto<T> {
    T data;
    String name;

    public T getData(){
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
