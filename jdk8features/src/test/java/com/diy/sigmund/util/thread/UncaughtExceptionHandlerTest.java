package com.diy.sigmund.util.thread;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ylm-sigmund
 * @since 2020/12/21 18:22
 */
public class UncaughtExceptionHandlerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UncaughtExceptionHandlerTest.class);

    private static class OneThread extends Thread {
        public OneThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            int a = 1 / 0;
            // throw new RuntimeException("this is a customRuntimeException");
        }
    }

    public static void main(String[] args) {
        final OneThread thread = new OneThread("UncaughtExceptionHandlerTest");
        // From UserThreadFactory uncaughtException name=UncaughtExceptionHandlerTest,Throwable={}
        // java.lang.ArithmeticException: / by zero
        // at
        // com.diy.sigmund.util.thread.UncaughtExceptionHandlerTest$OneThread.run(UncaughtExceptionHandlerTest.java:21)
        thread.setUncaughtExceptionHandler((th, throwable) -> LOGGER
            .error("From UserThreadFactory uncaughtException name={},Throwable={}", th.getName(), throwable));
        thread.start();
    }

    /**
     * 单元测试无法达到main方法的效果，无法测试方法setUncaughtExceptionHandler
     */
    @Test
    public void test1() {
        final OneThread thread = new OneThread("UncaughtExceptionHandlerTest");
        thread.setUncaughtExceptionHandler((th, throwable) -> LOGGER
            .error("From UserThreadFactory uncaughtException name={},Throwable={}", th.getName(), throwable));
        thread.start();
    }
}
