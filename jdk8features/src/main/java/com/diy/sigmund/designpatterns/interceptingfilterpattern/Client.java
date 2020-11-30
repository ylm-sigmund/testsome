package com.diy.sigmund.designpatterns.interceptingfilterpattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 17:31
 */
public class Client {
    FilterManager filterManager;

    public void setFilterManager(FilterManager filterManager) {
        this.filterManager = filterManager;
    }

    public void sendRequest(String request) {
        filterManager.filterRequest(request);
    }
}
