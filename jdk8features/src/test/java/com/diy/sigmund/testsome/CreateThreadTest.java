package com.diy.sigmund.testsome;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ylm-sigmund
 * @since 2020/10/10 21:13
 */
public class CreateThreadTest {

    @Test
    public void createThreadTest() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

    }
}
