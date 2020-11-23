package com.diy.sigmund.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.diy.sigmund.redis.RedisClusterConfig;
import com.diy.sigmund.redis.RedisPoolConfig;
import com.diy.sigmund.redis.RedisUtil;

/**
 * @author ylm-sigmund
 * @since 2020/11/22 12:34
 */
@Configuration
@ComponentScan("com.diy.sigmund")
public class BeansConfig {
    /**
     * redisClusterConfig
     * 
     * @return RedisClusterConfig
     */
    public RedisClusterConfig redisClusterConfig() {
        return new RedisClusterConfig();
    }

    /**
     * redisPoolConfig
     * 
     * @return RedisPoolConfig
     */
    public RedisPoolConfig redisPoolConfig() {
        return new RedisPoolConfig();
    }

    /**
     * 初始化redis集群的工具类
     * 
     * @param redisClusterConfig
     *            redisClusterConfig
     * @param redisPoolConfig
     *            redisPoolConfig
     * @return RedisUtil
     */
    @Bean(initMethod = "initJedisCluster")
    public RedisUtil redisUtil(RedisClusterConfig redisClusterConfig, RedisPoolConfig redisPoolConfig) {
        return new RedisUtil(redisClusterConfig, redisPoolConfig);
    }
}
