package com.yanghui.study.task.subPackage;

import com.yanghui.study.task.outterPackage.FanDemo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 关于泛型和匿名内部类已经fastjson序列化的细节
 * @author YangHui
 */
@Slf4j
public class SubClassAndGenericTest {

    /**
     * FanDemo构造方法是protected，并且不再同一个包，无法直接使用
     * 使用匿名内部类实例化一个实例
     */
    @Test
    public void test6(){
        FanDemo<String> fanDemo = new FanDemo<String>(){};
    }


}
