package com.diy.sigmund.testsome;

import java.util.concurrent.atomic.LongAdder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 【参考】volatile 解决多线程内存不可见问题。对于一写多读，是可以解决变量同步问题，但 是如果多写，同样无法解决线程安全问题。
 * 
 * 说明：如果是 count++操作，使用如下类实现：AtomicInteger count = new AtomicInteger(); count.addAndGet(1);
 * 
 * 如果是 JDK8，推荐使用 LongAdder 对象，比 AtomicLong 性能更好（减少乐观 锁的重试次数）。
 * 
 * @author ylm-sigmund
 * @since 2020/12/15 20:09
 */
public class LongAdderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LongAdder.class);

    @Test
    public void testLongAdder() {
        final LongAdder longAdder = new LongAdder();
        final int i = longAdder.intValue();
        LOGGER.info(i + "");
        longAdder.increment();
        final int i1 = longAdder.intValue();
        LOGGER.info(i1 + "");
    }
}
