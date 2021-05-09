package com.diy.sigmund.util.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 【强制】线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式， 这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 * 
 * 说明：Executors 返回的线程池对象的弊端如下： 1） FixedThreadPool 和 SingleThreadPool： 允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
 * 创建一个单线程的线程池.这个线程池只有一个线程在工程,也就是相当于单线程串行执行所有任务.如果这个唯一的线程因为异常结束,那么会有一个新的线程来替代它.此线程池保证所有任务的执行顺序都会按照提交的顺序执行.
 *
 * 2） CachedThreadPool： 允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。
 * 
 * @author ylm-sigmund
 * @since 2020/10/10 21:13
 */
public class CreateThreadTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateThreadTest.class);

    @Test
    public void createThreadTest() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Executors.newFixedThreadPool(4);
        final int cpuNum = Runtime.getRuntime().availableProcessors();
        LOGGER.info("cpuNum={}", cpuNum);

        RejectedExecutionHandler customRejectedExecutionHandler = (Runnable runnable, ThreadPoolExecutor executor) -> {
            LOGGER.error(
                "customRejectedExecutionHandler:The thread pool is full and the task is discarded,ThreadPoolExecutor={}",
                executor.toString());
        };
        final LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>(1);
        final UserThreadFactory testOnly = new UserThreadFactory("testOnly");
        final ExecutorService customThreadPool =
            getCustomThreadPool(1, 1, 0L, TimeUnit.MILLISECONDS, runnables, testOnly, customRejectedExecutionHandler);
        List<Future<?>> futureList = new ArrayList<>();
        futureList.add(customThreadPool.submit(testRunnable(3)));
        futureList.add(customThreadPool.submit(testRunnable(3)));
        futureList.add(customThreadPool.submit(testRunnable(3)));

        for (Future<?> future : futureList) {
            future.get();
        }
        customThreadPool.shutdown();
        LOGGER.info("customThreadPool.isTerminated()={}", customThreadPool.isTerminated());
    }

    public ExecutorService getCustomThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        LOGGER.info(
            "Start to initialize the thread pool[corePoolSize={},maximumPoolSize={},keepAliveTime={},TimeUnit={}]...",
            corePoolSize, maximumPoolSize, keepAliveTime, unit);
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
            workQueue, threadFactory, handler);
        LOGGER.info("Initializing the thread pool=complete!");
        return executorService;
    }

    // ArrayBlockingQueue和LinkedBlockingQueue的区别：
    // 1. 队列中锁的实现不同
    //     ArrayBlockingQueue实现的队列中的锁是没有分离的，即生产和消费用的是同一个锁；
    //     LinkedBlockingQueue实现的队列中的锁是分离的，即生产用的是putLock，消费是takeLock
    // 2. 在生产或消费时操作不同
    //     ArrayBlockingQueue实现的队列中在生产和消费的时候，是直接将枚举对象插入或移除的；
    //     LinkedBlockingQueue实现的队列中在生产和消费的时候，需要把枚举对象转换为Node<E>进行插入或移除，会影响性能
    // 3. 队列大小初始化方式不同
    //     ArrayBlockingQueue实现的队列中必须指定队列的大小；
    //     LinkedBlockingQueue实现的队列中可以不指定队列的大小，但是默认是Integer.MAX_VALUE
    @Test
    public void testLinkedBlockingQueue() {
        final LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>(1);
        // final ArrayBlockingQueue<Runnable> runnables = new ArrayBlockingQueue<>(1);
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            runnables.add(testRunnable(3));
        }
        System.out.println("end");
    }

    public Runnable testRunnable(int timeout) {
        final Runnable runnable = () -> {
            final String name = Thread.currentThread().getName();
            final String groupName = Thread.currentThread().getThreadGroup().getName();
            try {
                TimeUnit.SECONDS.sleep(timeout);
                // int a = 1/0;
                LOGGER.info("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName);
            } catch (Exception e) {
                LOGGER.error("currentThread().getName()={},currentThread().getThreadGroup().getName()={}", name,
                    groupName);
            }
        };
        return runnable;
    }

    /**
     * 返回可用于Java虚拟机的处理器数量。 在虚拟机的特定调用期间，此值可能会更改。 因此，对可用处理器数量敏感的应用程序应该偶尔轮询此属性并适当地调整其资源使用情况。
     *
     * 返回值： 虚拟机可用的最大处理器数量； 永远不小于一个
     */
    @Test
    public void testCpuNum() {
        final int cpuNum = Runtime.getRuntime().availableProcessors();
        // cpuNum=8
        LOGGER.info("cpuNum={}", cpuNum);
    }

}
