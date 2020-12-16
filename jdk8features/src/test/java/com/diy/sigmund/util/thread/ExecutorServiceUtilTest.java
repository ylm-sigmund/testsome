package com.diy.sigmund.util.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * @author ylm-sigmund
 * @since 2020/12/16 23:34
 */
public class ExecutorServiceUtilTest {

    @Test
    public void testSome() throws ExecutionException, InterruptedException {
        final ExecutorService executorService =
            new ExecutorServiceUtil().newFixedThreadPool(ExecutorServiceUtil.CPU_INTENSIVE, "testSome", 7);
        final Future<?> submit = executorService.submit(() -> System.out.println(222));
        submit.get();
        executorService.shutdown();
    }

}