package com.diy.sigmund.designpatterns.interceptingfilterpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 17:24
 */
public class FilterChain {

    private List<Filter> filters = new ArrayList<>();
    private Target target;

    public void setTarget(Target target) {
        this.target = target;

    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void execute(String request) {
        filters.forEach(filter -> filter.execute(request));
        target.execute(request);
    }
}
