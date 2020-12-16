package com.diy.sigmund.util.thread;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ylm-sigmund
 * @since 2020/12/16 23:06
 */
public final class ExecutorServiceUtil {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorServiceUtil.class);

    /**
     * CPU密集型
     */
    public static final String CPU_INTENSIVE = "CPUIntensive";
    /**
     * IO密集型
     */
    public static final String IO_INTENSIVE = "IOIntensive";

    /**
     *
     * @param taskType
     *            任务类型，包括CPU密集型，IO密集型
     * @param whatFeatureOfGroup
     *            whatFeatureOfGroup
     * @param maximumPoolSize
     *            池中允许的最大线程数
     * @return ExecutorService
     */
    public ExecutorService newFixedThreadPool(String taskType, String whatFeatureOfGroup, int maximumPoolSize) {
        int corePoolSize = getCorePoolSize(taskType);
        if (maximumPoolSize < corePoolSize) {
            maximumPoolSize = corePoolSize;
        }
        final LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>(maximumPoolSize * 10);
        final UserThreadFactory userThreadFactory = new UserThreadFactory(whatFeatureOfGroup);
        return getCustomThreadPool(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, runnables,
            userThreadFactory, customeRejectedExecutionHandler());
    }

    /**
     * 获取核心线程数量
     * 
     * @param taskType
     *            任务类型，包括CPU密集型，IO密集型
     * @return 数量
     */
    private int getCorePoolSize(String taskType) {
        // CPU核数
        final int cpuNum = Runtime.getRuntime().availableProcessors();
        int corePoolSize;
        if (Objects.equals(CPU_INTENSIVE, taskType)) {
            corePoolSize = cpuNum;
        } else if (Objects.equals(IO_INTENSIVE, taskType)) {
            corePoolSize = cpuNum * 2;
        } else {
            LOGGER.error("ExecutorServiceUtil.newFixedThreadPool taskType={}", taskType);
            throw new IllegalArgumentException("ExecutorServiceUtil.newFixedThreadPool taskType is error");
        }
        return corePoolSize;
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
     * @return
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
        return (Runnable runnable, ThreadPoolExecutor executor) -> {
            LOGGER.error(
                "customRejectedExecutionHandler:The thread pool is full and the task is discarded,ThreadPoolExecutor={}",
                executor.toString());
        };
    }
}
