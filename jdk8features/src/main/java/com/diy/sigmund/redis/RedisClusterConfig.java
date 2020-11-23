package com.diy.sigmund.redis;

import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * redis集群的配置
 * 
 * @author ylm-sigmund
 * @since 2020/11/20 20:43
 */
@Component
public class RedisClusterConfig {
    /**
     * 集群节点
     */
    @Value("${redis.cluster.nodes}")
    private String nodes;
    /**
     * 集群密码
     */
    @Value("${redis.cluster.pwd}")
    private String pwd;

    /**
     * getNodes
     * 
     * @return String
     */
    public String getNodes() {
        return nodes;
    }

    /**
     * getPwd
     * 
     * @return String
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 只打印节点，不打印密码
     * 
     * @return String
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", RedisClusterConfig.class.getSimpleName() + "[", "]").add("nodes='" + nodes + "'")
            .toString();
    }
}
