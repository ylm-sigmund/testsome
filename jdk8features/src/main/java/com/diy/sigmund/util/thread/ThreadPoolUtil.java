package com.diy.sigmund.util.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 【强制】线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，这 样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。 说明：Executors
 * 返回的线程池对象的弊端如下：
 *
 * 1） FixedThreadPool 和 SingleThreadPool：
 *
 * 允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
 *
 * 2） CachedThreadPool：
 *
 * 允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。
 * 
 * @author ylm-sigmund
 * @since 2020/12/16 23:06
 */
public final class ThreadPoolUtil {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolUtil.class);

    /**
     * cpu核数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 静态实例对象
     */
    private static ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil();

    /**
     * 无参构造
     */
    private ThreadPoolUtil() {}

    /**
     * 获取静态实例对象
     * 
     * @return ThreadPoolUtil
     */
    public static ThreadPoolUtil getInstance() {
        if (Objects.isNull(threadPoolUtil)) {
            threadPoolUtil = new ThreadPoolUtil();
        }
        return threadPoolUtil;
    }

    /**
     * 根据任务类型生成默认线程池
     * 
     * @param cpuIntensive
     *            任务类型，true:CPU密集型，false:IO密集型
     * 
     *            CPU密集型：线程个数为CPU核数。这几个线程可以并行执行，不存在线程切换到开销，提高了cpu的利用率的同时也减少了切换线程导致的性能损耗
     *
     *            IO密集型：线程个数为CPU核数的两倍。到其中的线程在IO操作的时候，其他线程可以继续用cpu，提高了cpu的利用率。
     * @param whatFeatureOfGroup
     *            根据外部特征进行分组的组名
     * @return ExecutorService
     */
    public ExecutorService newDefaultThreadPool(boolean cpuIntensive, String whatFeatureOfGroup) {
        int corePoolSize = cpuIntensive ? CPU_COUNT : CPU_COUNT * 2;
        // 配置队列容量，避免堆积大量的请求，从而导致 OOM。
        int capacity = corePoolSize * 10;
        final LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(capacity);
        final UserThreadFactory userThreadFactory = new UserThreadFactory(whatFeatureOfGroup);
        return getCustomThreadPool(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, workQueue, userThreadFactory,
            customeRejectedExecutionHandler());
    }

    /**
     * 只有一个线程的线程池
     * 
     * @param whatFeatureOfGroup
     *            根据外部特征进行分组的组名
     * @return ExecutorService
     */
    public ExecutorService newSingleThreadExecutor(String whatFeatureOfGroup) {
        int corePoolSize = 1;
        // 配置队列容量，避免堆积大量的请求，从而导致 OOM。
        int capacity = corePoolSize * 10;
        final LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(capacity);
        final UserThreadFactory userThreadFactory = new UserThreadFactory(whatFeatureOfGroup);
        return getCustomThreadPool(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, workQueue, userThreadFactory,
            customeRejectedExecutionHandler());
    }

    /**
     * 固定数量的线程池
     * 
     * @param corePoolSize
     *            线程池维护线程的最少数量
     * @param whatFeatureOfGroup
     *            根据外部特征进行分组的组名
     * @return ExecutorService
     */
    public ExecutorService newFixedThreadPool(int corePoolSize, String whatFeatureOfGroup) {
        if (corePoolSize <= 0) {
            corePoolSize = CPU_COUNT;
        }
        // 配置队列容量，避免堆积大量的请求，从而导致 OOM。
        int capacity = corePoolSize * 10;
        final LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(capacity);
        final UserThreadFactory userThreadFactory = new UserThreadFactory(whatFeatureOfGroup);
        return getCustomThreadPool(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, workQueue, userThreadFactory,
            customeRejectedExecutionHandler());
    }

    /**
     * 测试线程池运行规律
     * 
     * @param corePoolSize
     *            线程池维护线程的最少数量
     * 
     * @param maximumPoolSize
     *            线程池维护线程的最大数量
     * @param whatFeatureOfGroup
     *            根据外部特征进行分组的组名
     * @return ExecutorService
     */
    public ExecutorService newTestFixedThreadPool(int corePoolSize, int maximumPoolSize, String whatFeatureOfGroup) {
        if (corePoolSize <= 0) {
            corePoolSize = CPU_COUNT;
        }
        // 配置队列容量，避免堆积大量的请求，从而导致 OOM。
        // int capacity = corePoolSize * 10;
        int capacity = 10;
        final LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(capacity);
        final UserThreadFactory userThreadFactory = new UserThreadFactory(whatFeatureOfGroup);
        // future.get()会阻塞
        // final RejectedExecutionHandler rejectedExecutionHandler = customeRejectedExecutionHandler();
        // 对拒绝任务抛弃处理，并且直接抛出异常RejectedExecutionException
        final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
        // CallerRunsPolicy在任务被拒绝添加后，会调用当前线程池的所在的线程去执行被拒绝的任务。
        // currentThread().getName()=main,currentThread().getThreadGroup().getName()=main
        // final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        // 空实现，会让被线程池拒绝的任务直接抛弃，不会抛异常也不会执行 future.get()会阻塞
        // final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
        // DiscardOldestPolicy策略的作用是，当任务被拒绝添加时，会抛弃任务队列中最旧的任务也就是最先加入队列的，再把这个新任务添加进去。future.get()会阻塞
        // final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
        return getCustomThreadPool(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue,
            userThreadFactory, rejectedExecutionHandler);
    }

    /**
     * 等待任务执行结束，同步进行，可catch住线程中的Runnable或Callable
     * 
     * 该方法中抛出的线程是主线程，非线程池中的线程 currentThread().getName()=main
     * 
     * @param futureList
     *            futureList
     * @param <V>
     *            泛型
     * @return 集合
     */
    public <V> List<V> awaitCallableTaskDone(List<Future<V>> futureList) {
        List<V> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(futureList)) {
            for (Future<V> future : futureList) {
                try {
                    // result.add(future.get(10, TimeUnit.SECONDS));
                    result.add(future.get());
                } catch (Exception exception) {
                    result.add(null);
                    LOGGER.error("ThreadPoolUtil.awaitTaskDone future.get() error,currentThread().getName()={}",
                        Thread.currentThread().getName(), exception);
                }
            }
        }
        return result;
    }

    /**
     * 等待任务执行结束，同步进行，无返回值，可catch住线程中的Runnable或Callable
     * 
     * 该方法中抛出的线程是主线程，非线程池中的线程 currentThread().getName()=main
     * 
     * @param futureList
     *            futureList
     */
    public void awaitRunnableTaskDone(List<Future> futureList) {
        if (CollectionUtils.isNotEmpty(futureList)) {
            for (Future future : futureList) {
                try {
                    future.get();
                } catch (Exception exception) {
                    LOGGER.error("ThreadPoolUtil.awaitTaskDoneNoReturn future.get() error,currentThread().getName()={}",
                        Thread.currentThread().getName(), exception);
                }
            }
        }
    }

    /**
     * 自定义ExecutorService
     * 
     * @param corePoolSize
     *            线程池维护线程的最少数量。线程池至少会保持改数量的线程存在，即使没有任务可以处理。（注意：这里说的至少是指线程达到这个数量后，即使有空闲的线程也不会释放，而不是说线程池创建好之后就会初始化这么多线程）
     * @param maximumPoolSize
     *            池中允许的最大线程数
     * @param keepAliveTime
     *            线程池维护线程所允许的空闲时间。当线程池中的线程数量大于 corePoolSize时，超过corePoolSize的线程如果空闲时间超过keepAliveTime，线程将被终止
     * @param unit
     *            keepAliveTime参数的时间单位
     * @param workQueue
     *            在执行任务之前用于保留任务的队列。 此队列将仅保存execute方法提交的Runnable任务。
     * @param threadFactory
     *            执行程序创建新线程时要使用的工厂
     * @param handler
     *            线程池对拒绝任务的处理策略。因达到线程界限和队列容量而被阻止执行时使用的处理程序.AbortPolicy、DiscardPolicy、DiscardOldestPolicy、CallerRunsPolicy、自定义
     * @return ExecutorService
     */
    private ExecutorService getCustomThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
        TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
        RejectedExecutionHandler handler) {
        LOGGER.info(
            "Start to initialize the thread pool[corePoolSize={},maximumPoolSize={},keepAliveTime={},TimeUnit={}]...",
            corePoolSize, maximumPoolSize, keepAliveTime, unit);
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
            workQueue, threadFactory, handler);
        LOGGER.info("Initializing the thread pool=complete!");
        return executorService;
    }

    /**
     * 自定义拒绝策略
     * 
     * @return RejectedExecutionHandler
     */
    private RejectedExecutionHandler customeRejectedExecutionHandler() {
        return (Runnable runnable, ThreadPoolExecutor executor) -> LOGGER.error(
            "customRejectedExecutionHandler:The thread pool is full and the task is discarded,ThreadPoolExecutor={}",
            executor.toString());
    }
}
