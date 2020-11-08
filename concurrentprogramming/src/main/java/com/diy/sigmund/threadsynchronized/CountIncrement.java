package com.diy.sigmund.threadsynchronized;

import java.util.concurrent.TimeUnit;

/**
 * synchronized
 * 2种表现形式：锁方法，锁代码块
 * 2种作用范围：对象锁，类锁（跨对象跨线程保护）
 * @author ylm-sigmund
 * @since 2020/10/25 12:46
 */
public class CountIncrement {

    private static int count = 0;

    public static void increment() {
        // public synchronized static void increment() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (CountIncrement.class) {
            count++;
        }
        // count++;
    }

    public void synchronizedIncrement() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            count++;
        }
        // count++;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            // new Thread(() -> increment()).start();
            // 实例锁，synchronized (this)均 无法锁住，达不到效果，因为只作用于当前实例加锁
            new Thread(() -> new CountIncrement().synchronizedIncrement()).start();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("final result:" + count);
    }
}
