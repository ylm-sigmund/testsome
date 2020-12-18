package com.diy.sigmund.util.timecost;

import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 资料：https://www.cnblogs.com/yihuihui/p/12404509.html
 * 
 * 统计代码块耗时的工具，在代码的简洁性和统一管理上都要优雅很多，可以减少大量冗余代码
 * 
 * 在try(){}执行完毕之后，会调用方法AutoCloseable#close方法；
 * 
 * 该应用类似于lambda表达式，也可以做类似于动态代理的事情
 * 
 * @author ylm-sigmund
 * @since 2020/12/18 20:03
 */
public class TimeCostUtil implements AutoCloseable {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeCostUtil.class);

    /**
     * 开始时间
     */
    private LocalDateTime start;

    /**
     * 关键信息
     */
    private String info;

    /**
     * 有参构造
     * 
     * @param info
     *            关键信息
     */
    public TimeCostUtil(String info) {
        this.start = LocalDateTime.now();
        this.info = info;
    }

    /**
     * 此处不抛出Exception
     */
    @Override
    public void close() {
        LOGGER.info("{} 耗时={} ms", info, Duration.between(start, LocalDateTime.now()).toMillis());
    }
}
