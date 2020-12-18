package com.diy.sigmund.util.timecost;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计代码块耗时，打印时间
 * 
 * @author ylm-sigmund
 * @since 2020/12/18 20:33
 */
public class TimeCostUtils {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeCostUtils.class);

    /**
     * 统计代码块耗时，打印时间
     * 
     * @param info
     *            关键信息
     * @param supplier
     *            supplier
     * @param <T>
     *            泛型
     */
    public static <T> void printTimeCost(String info, Supplier<T> supplier) {
        final LocalDateTime start = LocalDateTime.now();
        try {
            supplier.get();
        } catch (Exception exception) {
            LOGGER.error("printTimeCost error", exception);
        }
        LOGGER.info("{} 耗时={} ms", info, Duration.between(start, LocalDateTime.now()).toMillis());
    }
}
