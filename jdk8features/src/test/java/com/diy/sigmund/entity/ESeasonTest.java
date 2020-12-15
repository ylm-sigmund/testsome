package com.diy.sigmund.entity;

import org.junit.Test;

/**
 * @author ylm-sigmund
 * @since 2020/12/15 20:44
 */
public class ESeasonTest {

    @Test
    public void testSome() {
        final ESeason[] values = ESeason.values();
        for (ESeason value : values) {
            System.out.println(value);
        }
        System.out.println(ESeason.getCode("SPRING"));
        System.out.println(ESeason.getDesc("WINTER"));
    }
}