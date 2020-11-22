package com.diy.sigmund.entity;

import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author ylm-sigmund
 * @since 2020/11/21 13:31
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }
}
