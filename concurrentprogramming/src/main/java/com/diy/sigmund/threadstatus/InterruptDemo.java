package com.diy.sigmund.threadstatus;

import java.util.concurrent.TimeUnit;

/**
 * @author ylm-sigmund
 * @since 2020/10/20 22:17
 */
public class InterruptDemo {

    private static int i;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {// 默认是false
                i++;
            }
            System.out.println(i);
        });
        thread.start();

        // RUNNABLE
        System.out.println(thread.getState());

        TimeUnit.SECONDS.sleep(1);
        // 把isInterrupted设置成true
        thread.interrupt();

    }
}
