package com.diy.sigmund.util.timecost;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * 
 * @author ylm-sigmund
 * @since 2020/12/18 20:12
 */
public class TimeCostUtilTest {

    @Test
    public void testSome() {
        try (final TimeCostUtil getString = new TimeCostUtil("getString")) {
            getString();
            // 哪怕抛出异常也会打印耗时
            throw new RuntimeException();
        }
    }

    @Test
    public void testSome1() {
        TimeCostUtils.printTimeCost("getString", () -> getString());
        TimeCostUtils.printTimeCost("getString", () -> {
            getString();
            throw new RuntimeException();
        });
    }

    public String getString() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {

        }
        return "ok";
    }

}