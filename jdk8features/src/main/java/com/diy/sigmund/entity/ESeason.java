package com.diy.sigmund.entity;

import java.util.Objects;

/**
 * @author ylm-sigmund
 * @since 2020/12/15 20:27
 */
public enum ESeason {
    SPRING(1, "001", "春天"),
    SUMMER(2, "002", "夏天"),
    AUTUMN(3, "003", "秋天"),
    WINTER(4, "004", "冬天");

    /**
     * 序号
     */
    private int ordinal;
    /**
     * 编码
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    ESeason(int ordinal, String code, String desc) {
        this.ordinal = ordinal;
        this.code = code;
        this.desc = desc;
    }

    public static String getCode(String name) {
        for (ESeason value : ESeason.values()) {
            if (Objects.equals(value.name(), name)) {
                return value.code;
            }
        }
        return null;
    }

    public static String getDesc(String name) {
        for (ESeason value : ESeason.values()) {
            if (Objects.equals(value.name(), name)) {
                return value.desc;
            }
        }
        return null;
    }
}
