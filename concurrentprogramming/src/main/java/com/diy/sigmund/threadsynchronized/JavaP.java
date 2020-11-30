package com.diy.sigmund.threadsynchronized;

/**
 * @author ylm-sigmund
 * @since 2020/11/8 18:30
 */
public class JavaP {
    public static void main(String[] args) {
        synchronized (JavaP.class) {
            System.out.println("JavaP");
        }
    }
}
