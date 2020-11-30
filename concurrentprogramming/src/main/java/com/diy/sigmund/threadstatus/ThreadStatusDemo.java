package com.diy.sigmund.threadstatus;

import java.util.concurrent.TimeUnit;

/**
 * F:\project\knight\github\testsome>jps 17680 ThreadStatusDemo
 *
 * F:\project\knight\github\testsome>jstack ThreadStatusDemo
 *
 * @author ylm-sigmund
 * @since 2020/10/20 21:49
 */
public class ThreadStatusDemo {

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Timed_Waiting_Thread").start();

        new Thread(() -> {
            while (true) {
                synchronized (ThreadStatusDemo.class) {
                    try {
                        ThreadStatusDemo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Wating_Thread").start();

        // TIMED_WAITING
        new Thread(new BlockedClass(), "Blocke01_Thread").start();
        // BLOCKED
        new Thread(new BlockedClass(), "Blocke02_Thread").start();
    }

    static class BlockedClass extends Thread {
        /**
         * If this thread was constructed using a separate <code>Runnable</code> run object, then that
         * <code>Runnable</code> object's <code>run</code> method is called; otherwise, this method does nothing and
         * returns.
         * <p>
         * Subclasses of <code>Thread</code> should override this method.
         *
         * @see #start()
         * @see #stop()
         * @see #Thread(ThreadGroup, Runnable, String)
         */
        @Override
        public void run() {
            synchronized (BlockedClass.class) {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
