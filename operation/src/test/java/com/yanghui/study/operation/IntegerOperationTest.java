package com.yanghui.study.operation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class IntegerOperationTest {

    @Test
    public void test1(){
        IntegerOperation operation = new IntegerOperation();
        log.info("操作1："+operation.m1(16));
        log.info("max capacity:"+IntegerOperation.MAP_MAXIMUM_CAPACITY);
        log.info("max integer :"+Integer.MAX_VALUE);
        log.info("(1 << 31) -1:"+IntegerOperation.MAX_INTEGER);
    }

}
