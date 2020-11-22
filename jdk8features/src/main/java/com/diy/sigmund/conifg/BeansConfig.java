package com.diy.sigmund.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.diy.sigmund.redis.RedisClusterConfig;
import com.diy.sigmund.redis.RedisUtil;

/**
 * @author ylm-sigmund
 * @since 2020/11/22 12:34
 */
@Configuration
@ComponentScan("com.diy.sigmund")
public class BeansConfig {
    public RedisClusterConfig redisClusterConfig() {
        return new RedisClusterConfig();
    }

    @Bean(initMethod = "initJedisCluster")
    public RedisUtil redisUtil(RedisClusterConfig redisClusterConfig) {
        return new RedisUtil(redisClusterConfig);
    }
}
