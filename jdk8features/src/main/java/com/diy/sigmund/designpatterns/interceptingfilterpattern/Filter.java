package com.diy.sigmund.designpatterns.interceptingfilterpattern;

/**
 * 创建过滤器接口 Filter。
 * 
 * @author ylm-sigmund
 * @since 2020/11/30 17:22
 */
public interface Filter {
    void execute(String request);
}
