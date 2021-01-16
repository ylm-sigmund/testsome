package com.diy.sigmund.entity;

import java.util.Date;
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

    /**
     * 序列化时格式化，按一定格式反序列化
     *
     * "date":"2020-11-25 21:24:31"
     *
     * date=Wed Nov 25 21:18:21 CST 2020
     */
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;

    public User() {
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]").add("id=" + id).add("name='" + name + "'")
            .add("date=" + date).toString();
    }
}
