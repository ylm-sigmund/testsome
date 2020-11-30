package com.diy.sigmund.threadstatus;

import java.util.concurrent.TimeUnit;

/**
 * @author ylm-sigmund
 * @since 2020/10/21 20:39
 */
public class ThreadResetDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {// 默认是false
                if (Thread.currentThread().isInterrupted()) {
                    // before:true
                    System.out.println("before:" + Thread.currentThread().isInterrupted());
                    // 对线程进行复位，由 true 变成 false
                    Thread.interrupted();
                    // after:false
                    System.out.println("after:" + Thread.currentThread().isInterrupted());
                }
            }
        });
        thread.start();

        // RUNNABLE
        System.out.println(thread.getState());

        TimeUnit.SECONDS.sleep(1);
        // 把isInterrupted设置成true
        thread.interrupt();

    }
}
