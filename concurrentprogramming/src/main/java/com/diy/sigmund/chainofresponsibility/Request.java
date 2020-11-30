package com.diy.sigmund.chainofresponsibility;

import java.util.StringJoiner;

/**
 * @author ylm-sigmund
 * @since 2020/9/27 21:43
 */
public class Request {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Request.class.getSimpleName() + "[", "]").add("name='" + name + "'").toString();
    }
}
