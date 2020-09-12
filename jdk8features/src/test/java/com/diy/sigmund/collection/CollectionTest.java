package com.diy.sigmund.collection;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ylm-sigmund
 * @since 2020/9/12 13:35
 */
public class CollectionTest {
    Logger logger = LoggerFactory.getLogger(CollectionTest.class);

    @Test
    public void testCollectionRemove() {
        List<String> list1 = Stream.of("a", "b", "c", "a", "b", "1").collect(Collectors.toList());
        List<String> list2 = Stream.of("a", "b", "a").collect(Collectors.toList());
        // String remove = list1.remove(0);
        // logger.info(remove + " " + list1.toString());//a [b, c, a, b, 1]
        // 只会remove遇到的第一个元素，后面重复的不会remove
        boolean flag = list1.remove("a");
        logger.info(flag + " " + list1.toString());// true [b, c, a, b, 1]
        // 会remove包含的所有元素
        // boolean flag = list1.removeAll(list2);
        // logger.info(flag + " " + list1.toString());// true [c, 1]
    }
}
