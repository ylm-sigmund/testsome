package com.diy.sigmund.util.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ylm-sigmund
 * @since 2020/12/16 23:34
 */
public class ThreadPoolUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolUtilTest.class);

    @Test
    public void testSome() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = ThreadPoolUtil.getInstance().newDefaultThreadPool(true, "testSome");
        List<Future> futureList = new ArrayList<>();
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        ThreadPoolUtil.getInstance().awaitTaskDoneNoReturn(futureList);
        executorService.shutdown();

        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newDefaultThreadPool(true, "testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService1.shutdown();
    }

    @Test
    public void testSome1() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = ThreadPoolUtil.getInstance().newSingleThreadExecutor("testSome");
        List<Future> futureList = new ArrayList<>();
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        ThreadPoolUtil.getInstance().awaitTaskDoneNoReturn(futureList);
        executorService.shutdown();

        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newSingleThreadExecutor("testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService1.shutdown();
    }

    @Test
    public void testSome2() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = ThreadPoolUtil.getInstance().newFixedThreadPool(4, "testSome");
        List<Future> futureList = new ArrayList<>();
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        ThreadPoolUtil.getInstance().awaitTaskDoneNoReturn(futureList);
        executorService.shutdown();

        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newFixedThreadPool(4, "testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService1.shutdown();
    }

    public Runnable testRunnable(int timeout) {
        final Runnable runnable = () -> {
            final String name = Thread.currentThread().getName();
            final String groupName = Thread.currentThread().getThreadGroup().getName();
            try {
                TimeUnit.SECONDS.sleep(timeout);
                int a = 1 / 0;
                LOGGER.info("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName);
            } catch (Exception e) {
                LOGGER.error("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName);
            }
        };
        return runnable;
    }

    public Callable testCallable(int timeout) {
        final Callable callable = () -> {
            final String name = Thread.currentThread().getName();
            final String groupName = Thread.currentThread().getThreadGroup().getName();
            try {
                TimeUnit.SECONDS.sleep(timeout);
                int a = 1 / 0;
                LOGGER.info("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName);
            } catch (Exception e) {
                LOGGER.error("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName);
            }
            return "success";
        };
        return callable;
    }

}