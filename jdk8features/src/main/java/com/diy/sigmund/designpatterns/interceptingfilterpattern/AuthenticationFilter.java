package com.diy.sigmund.designpatterns.interceptingfilterpattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 17:23
 */
public class AuthenticationFilter implements Filter {
    @Override
    public void execute(String request) {
        System.out.println("Authenticating request: " + request);
    }
}
