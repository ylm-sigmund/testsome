package com.diy.sigmund.designpatterns.interceptingfilterpattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 17:27
 */
public class FilterManager {
    FilterChain filterChain;

    public FilterManager(Target target) {
        filterChain = new FilterChain();
        filterChain.setTarget(target);
    }

    public void setFilter(Filter filter) {
        filterChain.addFilter(filter);
    }

    public void filterRequest(String request) {
        filterChain.execute(request);
    }
}
