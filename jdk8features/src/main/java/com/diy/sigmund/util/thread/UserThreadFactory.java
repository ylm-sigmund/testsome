package com.diy.sigmund.util.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 【强制】创建线程或线程池时请指定有意义的线程名称，方便出错时回溯。
 * 
 * 正例：自定义线程工厂，并且根据外部特征进行分组，比如，来自同一机房的调用，把机房编号赋值给 whatFeaturOfGroup
 * 
 * @author ylm-sigmund
 * @since 2020/12/15 19:42
 */
public class UserThreadFactory implements ThreadFactory {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserThreadFactory.class);
    /**
     * 名称前缀
     */
    private final String namePrefix;
    /**
     * 线程组编号
     */
    private final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * 定义线程组名称，在 jstack 问题排查时，非常有帮助
     * 
     * @param whatFeatureOfGroup
     *            whatFeatureOfGroup
     */
    public UserThreadFactory(String whatFeatureOfGroup) {
        namePrefix = "From UserThreadFactory's " + whatFeatureOfGroup + "-Worker-";
    }

    /**
     * 创建线程，打印线程名
     * 
     * @param task
     *            Runnable
     * @return Thread
     */
    @Override
    public Thread newThread(Runnable task) {
        String name = namePrefix + nextId.getAndIncrement();
        Thread thread = new Thread(task, name);
        // 处理Runnable线程内的非受检异常
        thread.setUncaughtExceptionHandler((th, throwable) -> LOGGER
            .error("From UserThreadFactory uncaughtException name={},Throwable={}", th.getName(), throwable));
        LOGGER.info("UserThreadFactory newThread'name={}", thread.getName());
        return thread;
    }
}
