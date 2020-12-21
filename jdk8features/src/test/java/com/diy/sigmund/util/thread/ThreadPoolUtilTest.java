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
    public void testNewDefaultThreadPool() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = ThreadPoolUtil.getInstance().newDefaultThreadPool(true, "testSome");
        List<Future> futureList = new ArrayList<>();
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        ThreadPoolUtil.getInstance().awaitRunnableTaskDone(futureList);
        executorService.shutdown();

        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newDefaultThreadPool(true, "testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitCallableTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService1.shutdown();
    }

    @Test
    public void testNewSingleThreadExecutor() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = ThreadPoolUtil.getInstance().newSingleThreadExecutor("testSome");
        List<Future> futureList = new ArrayList<>();
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        ThreadPoolUtil.getInstance().awaitRunnableTaskDone(futureList);
        executorService.shutdown();

        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newSingleThreadExecutor("testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitCallableTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService1.shutdown();
    }

    @Test
    public void testNewFixedThreadPool() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = ThreadPoolUtil.getInstance().newFixedThreadPool(4, "testSome");
        List<Future> futureList = new ArrayList<>();
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        futureList.add(executorService.submit(testRunnable(3)));
        ThreadPoolUtil.getInstance().awaitRunnableTaskDone(futureList);
        executorService.shutdown();

        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newFixedThreadPool(4, "testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitCallableTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService1.shutdown();
    }

    @Test
    public void testNewFixedThreadPoolByException() throws ExecutionException, InterruptedException {
        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newFixedThreadPool(4, "testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(() -> {
            int a = 1 / 0;
            return "success";
        }));
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitCallableTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService1.shutdown();
    }

    @Test
    public void testNewFixedThreadPoolAndSetUncaughtExceptionHandler() throws ExecutionException, InterruptedException {
        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newFixedThreadPool(4, "testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(() -> {
            int a = 1 / 0;
            return "success";
        }));
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitCallableTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService1.shutdown();
    }

    @Test
    public void testNewFixedThreadPoolAndShutdown() {
        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newFixedThreadPool(4, "testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(10)));
        futureList1.add(executorService1.submit(testCallable(10)));
        futureList1.add(executorService1.submit(testCallable(10)));
        // 前面的任务会执行完毕，后面的任务会执行拒绝策略
        executorService1.shutdown();
        // 线程中设计到了InterruptedException的会抛出该异常
        // executorService1.shutdownNow();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(() -> {
            int a = 1;
            TimeUnit.SECONDS.sleep(5);
            return "success";
        }));
        // shutdown()后短时间内没有跑下面方法
        // final List<String> list1 = ThreadPoolUtil.getInstance().awaitCallableTaskDone(futureList1);
        // list1.forEach(LOGGER::info);
    }

    public Runnable testRunnable(int timeout) {
        final Runnable runnable = () -> {
            final String name = Thread.currentThread().getName();
            final String groupName = Thread.currentThread().getThreadGroup().getName();
            try {
                TimeUnit.SECONDS.sleep(timeout);
                int a = 1 / 1;
                LOGGER.info("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName);
            } catch (Exception exception) {
                LOGGER.error("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName, exception);
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
                int a = 1 / 1;
                LOGGER.info("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName);
            } catch (Exception exception) {
                LOGGER.error("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName, exception);
            }
            return "success";
        };
        return callable;
    }

}