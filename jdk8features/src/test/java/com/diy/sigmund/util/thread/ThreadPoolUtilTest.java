package com.diy.sigmund.util.thread;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
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

    /**
     * 线程中的Runnable或Callable，若不catch异常，则异常不会往外抛出
     */
    @Test
    public void testNewFixedThreadPoolAndIsTerminated() {
        final ExecutorService executorService1 = ThreadPoolUtil.getInstance().newFixedThreadPool(4, "testSome");
        List<Future<String>> futureList1 = new ArrayList<>();
        futureList1.add(executorService1.submit(testCallable(3)));
        futureList1.add(executorService1.submit(() -> {
            try {
                int a = 1 / 0;
            } catch (Exception exception) {
                LOGGER.error("ThreadPoolUtil.awaitTaskDone future.get() error,currentThread().getName()={}",
                    Thread.currentThread().getName(), exception);
            }
            return "success";
        }));
        // final List<String> list1 = ThreadPoolUtil.getInstance().awaitCallableTaskDone(futureList1);
        // list1.forEach(LOGGER::info);
        executorService1.shutdown();
        while (!executorService1.isTerminated()) {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException", e);
            }
        }
        LOGGER.info("end...");
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

    /**
     * 当创建了5个核心线程，10个最大线程，队列数量为10的线程池
     *
     * 任务数为5，直接创建5个线程，任务不会进入队列
     *
     * 任务数为10，直接创建5个线程，第6个任务开始进入队列
     * 
     * 任务数为16，直接创建5个线程，第6-15个任务开始进入队列，第16个任务创建第6个线程
     *
     * 任务数为21，直接创建5个线程，第6-15个任务进入队列，第16-20个任务创建线程，第21个任务执行拒绝策略
     *
     * [ERROR] 2021-03-15 19:02:06,054
     * method:com.diy.sigmund.util.thread.ThreadPoolUtil.lambda$customeRejectedExecutionHandler$0(ThreadPoolUtil.java:239)
     * customRejectedExecutionHandler:The thread pool is full and the task is
     * discarded,ThreadPoolExecutor=java.util.concurrent.ThreadPoolExecutor@35851384[Running, pool size = 10, active
     * threads = 10, queued tasks = 10, completed tasks = 0]
     * 
     * 总结来说：核心线程满了，接下来进队列，队列也满了，创建新线程，直到达到最大线程数，之后再超出，会进入拒绝rejectedExecution
     */
    @Test
    public void testSomeCountThreadCreate() {
        final ThreadPoolExecutor executorService =
            (ThreadPoolExecutor)ThreadPoolUtil.getInstance().newTestFixedThreadPool(5, 10, "testSomeCountThreadCreate");
        List<Future<String>> futureList1 = new ArrayList<>();

        for (int i = 1; i <= 21; i++) {
            futureList1.add(executorService.submit(testCallable(4)));
            LOGGER.info("当前进行的第{}个任务", i);
            System.out.println("当前线程数" + executorService.getPoolSize());
            System.out.println("返回线程的核心数量" + executorService.getCorePoolSize());
            System.out.println("返回允许的最大线程数" + executorService.getMaximumPoolSize());
            System.out.println("正在主动执行任务的线程的大概数量" + executorService.getActiveCount());
            System.out.println("已完成执行的任务的大概总数" + executorService.getCompletedTaskCount());
            System.out.println("曾经同时存在的最大线程数" + executorService.getLargestPoolSize());
            System.out.println("返回计划执行的任务总数" + executorService.getTaskCount());
            final BlockingQueue<Runnable> queue = executorService.getQueue();
            int a = 1;
            // getThreadPoolWorkers(executorService);
        }
        final List<String> list1 = ThreadPoolUtil.getInstance().awaitCallableTaskDone(futureList1);
        list1.forEach(LOGGER::info);
        executorService.shutdown();
    }

    /**
     * @throws Exception
     * @Description: 获取ThreadPoolExecutor中私有HashSet集合对象workers的数据
     */
    public static void getThreadPoolWorkers(ThreadPoolExecutor cutExecutor) {
        // 1.获取私有对象workers
        Object resultValue = getPrivateClass(cutExecutor, "workers");
        HashSet workers = (HashSet)resultValue;
        // 2.遍历workers集合
        Iterator iterator = workers.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            // 3.获取workers的私有属性thread对象
            Object workerValue = getPrivateClass(obj, "thread");
            Thread workerThread = (Thread)workerValue;
            System.out.println("线程名:" + workerThread.getName());
        }
    }

    /**
     * @Description: 获取类的某个私有属性
     */
    public static <T> Object getPrivateClass(T t, String param) {
        Object workerValue = null;
        Class workerCla = t.getClass();
        Field[] workerFields = workerCla.getDeclaredFields();
        for (Field workerField : workerFields) {
            // 私有属性必须设置访问权限
            workerField.setAccessible(true);
            // 获取私有对象的属性名称
            String workerName = workerField.getName();
            if (param.equals(workerName)) {
                try {
                    // 获取私有对象的属性
                    workerValue = workerField.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return workerValue;
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