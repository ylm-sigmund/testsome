package com.diy.sigmund.threadsynchronized;

/**
 * @author ylm-sigmund
 * @since 2020/11/8 19:06
 */
public class ThreadNotify extends Thread {
    private final Object lock;

    public ThreadNotify(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            System.out.println("ThreadNotify start");
            // 唤醒被阻塞的线程
            lock.notify();
            System.out.println("ThreadNotify end");
        }
    }

    public static void main(String[] args) {
        final Object lock = new Object();
        final ThreadWait threadWait = new ThreadWait(lock);
        threadWait.start();
        final ThreadNotify threadNotify = new ThreadNotify(lock);
        threadNotify.start();
    }
}
