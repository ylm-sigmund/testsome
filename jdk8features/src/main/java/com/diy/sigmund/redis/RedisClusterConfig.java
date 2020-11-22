package com.diy.sigmund.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ylm-sigmund
 * @since 2020/11/20 20:43
 */
@Component
public class RedisClusterConfig {
    @Value("${redis.cluster.nodes}")
    private String nodes;
    @Value("${redis.cluster.pwd}")
    private String pwd;

    public String getNodes() {
        return nodes;
    }

    public String getPwd() {
        return pwd;
    }
}
