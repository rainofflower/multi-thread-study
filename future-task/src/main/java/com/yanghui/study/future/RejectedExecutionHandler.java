package com.yanghui.study.future;

/**
 * Created by YangHui on 2019/11/24
 */
public interface RejectedExecutionHandler {

    void rejectedExecution(Runnable r, EventExecutor e);

}
