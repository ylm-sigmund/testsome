package com.diy.sigmund.chainofresponsibility;

/**
 * @author ylm-sigmund
 * @since 2020/9/27 21:40
 */
public interface IRequestProcessor {

    void process(Request request);
}
