package com.diy.sigmund.designpatterns.interceptingfilterpattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 17:24
 */
public class Target {
    public void execute(String request) {
        System.out.println("Executing request: " + request);
    }
}
