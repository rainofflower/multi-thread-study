package com.yanghui.study.task;

import lombok.Data;

/**
 * @author YangHui
 */
@Data
public class User {
    String name;
    Integer age;

    public User(){}

    public User(String name, int age){
        this.name = name;
        this.age = age;
    }

}
