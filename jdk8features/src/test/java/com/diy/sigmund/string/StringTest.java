package com.diy.sigmund.string;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ylm-sigmund
 * @since 2020/9/12 13:35
 */
public class StringTest {
    Logger logger = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void testString() {
        String str = "[as]";
        logger.info(StringUtils.strip(str, "[]"));// as

        String str1 = "  a  s     1";
        logger.info(str1.replace(" ", ""));// as1
        logger.info(str1.replaceAll(" ", ""));// as1
        logger.info(str1.replaceAll("[\\s]+", ""));// as1
        logger.info(str1.replaceFirst(" ", ""));// a s 1
    }
}
