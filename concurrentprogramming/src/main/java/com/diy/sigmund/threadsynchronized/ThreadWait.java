package com.diy.sigmund.threadsynchronized;

/**
 * @author ylm-sigmund
 * @since 2020/11/8 19:01
 */
public class ThreadWait extends Thread {

    private final Object lock;

    public ThreadWait(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            System.out.println("ThreadWait start");
            try {
                // 实现线程的阻塞
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ThreadWait end");
        }
    }
}
