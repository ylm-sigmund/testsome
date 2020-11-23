package com.diy.sigmund.redis;

import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 自定义JedisPoolConfig
 * 
 * @author ylm-sigmund
 * @since 2020/11/23 19:48
 */
@Component
public class RedisPoolConfig {
    /**
     * 资源池中的最大连接数
     */
    @Value("${redis.pool.maxTotal}")
    private Integer maxTotal;
    /**
     * 资源池允许的最大空闲连接数
     */
    @Value("${redis.pool.maxIdle}")
    private Integer maxIdle;
    /**
     * 资源池确保的最少空闲连接数
     */
    @Value("${redis.pool.minIdle}")
    private Integer minIdle;
    /**
     * 资源池确保的最少空闲连接数 当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。
     */
    @Value("${redis.pool.maxWaitMillis}")
    private Integer maxWaitMillis;

    /**
     * getMaxTotal
     * 
     * @return Integer
     */
    public Integer getMaxTotal() {
        return maxTotal;
    }

    /**
     * getMaxIdle
     * 
     * @return Integer
     */
    public Integer getMaxIdle() {
        return maxIdle;
    }

    /**
     * getMinIdle
     * 
     * @return Integer
     */
    public Integer getMinIdle() {
        return minIdle;
    }

    /**
     * getMaxWaitMillis
     * 
     * @return Integer
     */
    public Integer getMaxWaitMillis() {
        return maxWaitMillis;
    }

    /**
     * toString
     * 
     * @return String
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", RedisPoolConfig.class.getSimpleName() + "[", "]").add("maxTotal=" + maxTotal)
            .add("maxIdle=" + maxIdle).add("minIdle=" + minIdle).add("maxWaitMillis=" + maxWaitMillis).toString();
    }
}
