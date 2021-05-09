package com.diy.sigmund.redis;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis集群的工具类
 * 
 * @author ylm-sigmund
 * @since 2020/11/20 20:43
 */
@Component
public class RedisUtil {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
    /**
     * jedisCluster
     */
    private static JedisCluster jedisCluster;
    /**
     * redisClusterConfig
     */
    private RedisClusterConfig redisClusterConfig;
    /**
     * redisPoolConfig
     */
    private RedisPoolConfig redisPoolConfig;

    /**
     * RedisUtil注入属性
     *
     * @param redisClusterConfig
     *            redisClusterConfig
     * @param redisPoolConfig
     *            redisPoolConfig
     */
    public RedisUtil(RedisClusterConfig redisClusterConfig, RedisPoolConfig redisPoolConfig) {
        this.redisClusterConfig = redisClusterConfig;
        this.redisPoolConfig = redisPoolConfig;
    }

    /**
     * 将键 key 的值设置为 value ， 并将键 key 的生存时间设置为 seconds 秒钟。
     *
     * 如果键 key 已经存在， 那么 SETEX 命令将覆盖已有的值。
     *
     * @param key
     *            key
     * @param value
     *            value
     * @param timeout
     *            超时时间
     * @param timeUnit
     *            时间单位 SECONDS(秒),MINUTES(分),HOURS(时),DAYS(天),
     * @return 成功true或者失败false
     */
    public static boolean put(String key, Object value, int timeout, TimeUnit timeUnit) {
        try {
            int seconds = (int)timeUnit.toSeconds(timeout);
            jedisCluster.setex(key, seconds, JacksonUtil.toJson(value));
            return true;
        } catch (Exception exception) {
            LOGGER.error("Redis setex key={}, value={}, timeout={}, timeUnit={}, exception={}", key, value, timeout,
                timeUnit, exception);
            return false;
        }
    }

    /**
     * 返回与键 key 相关联的值（已将字符串处理为对象）。
     *
     * 例如：RedisUtil.get(key1, new TypeReference<User>() {});
     *
     * @param key
     *            key
     * @param typeReference
     *            typeReference
     * @param <T>
     *            <T>
     * @return return
     */
    public static <T> T get(String key, TypeReference<T> typeReference) {
        try {
            final String json = jedisCluster.get(key);
            return JacksonUtil.toObject(json, typeReference);
        } catch (Exception exception) {
            LOGGER.error("Redis get key={}, exception={}", key, exception);
            return null;
        }
    }

    /**
     * Redis Expire 命令用于设置 key 的过期时间。key 过期后将不再可用。若 key 存在，则会覆盖过期时间
     *
     * @param key
     *            key
     * @param timeout
     *            timeout
     * @param timeUnit
     *            timeUnit
     * @return Long
     */
    public static Long expire(String key, int timeout, TimeUnit timeUnit) {
        try {
            int seconds = (int)timeUnit.toSeconds(timeout);
            return jedisCluster.expire(key, seconds);
        } catch (Exception exception) {
            LOGGER.error("Redis expire key={}, exception={}", key, exception);
            return null;
        }
    }

    /**
     * Redis EXISTS 命令用于检查给定 key 是否存在。
     *
     * @param key
     *            key
     * @return Boolean
     */
    public static Boolean exists(String key) {
        try {
            return jedisCluster.exists(key);
        } catch (Exception exception) {
            LOGGER.error("Redis exists key={}, exception={}", key, exception);
            return false;
        }
    }

    /**
     * Redis TTL 命令以秒为单位返回 key 的剩余过期时间。
     *
     * @param key
     *            key
     * @return Long
     */
    public static Long ttl(String key) {
        try {
            return jedisCluster.ttl(key);
        } catch (Exception exception) {
            LOGGER.error("Redis ttl key={}, exception={}", key, exception);
            return null;
        }
    }

    /**
     * Redis DEL 命令用于删除已存在的键。不存在的 key 会被忽略。
     *
     * @param key
     *            key
     * @return Long
     */
    public static Long del(String key) {
        try {
            return jedisCluster.del(key);
        } catch (Exception exception) {
            LOGGER.error("Redis del key={}, exception={}", key, exception);
            return null;
        }
    }

    /**
     * 执行lua脚本
     *
     * 例如：
     *
     * 192.168.92.100:8001> eval "return redis.call('set',KEYS[1],ARGV[1])" 1 name sym
     *
     * OK
     *
     * 192.168.92.100:8001> eval "return redis.call('get',KEYS[1])" 1 name
     *
     * "sym"
     *
     * @param script
     *            script
     * @param keys
     *            keys
     * @param args
     *            args
     * @return Object
     */
    public static Object eval(String script, List<String> keys, List<String> args) {
        try {
            return jedisCluster.eval(script, keys, args);
        } catch (Exception exception) {
            LOGGER.error("Redis eval script={}, keys={}, args={}, exception={}", script, keys.toString(),
                args.toString(), exception);
            return null;
        }
    }

    /**
     * 尝试获取分布式锁
     *
     */
    public static boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        final List<String> args = Stream.of(requestId, String.valueOf(expireTime)).collect(Collectors.toList());
        Object result = jedisCluster.eval(LOCK_SCRIPT, Collections.singletonList(lockKey), args);
        return Objects.equals(SUCCESS, result);
    }

    private static final Long SUCCESS = 1L;
    /**
     * lua脚本：加锁，成功就设置过期时间
     */
    private static final String LOCK_SCRIPT =
        "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then  return redis.call('expire',KEYS[1],ARGV[2])  else return 0 end";
    /**
     * lua脚本：判断锁住值是否为当前线程持有，是的话解锁，不是的话解锁失败
     */
    private static final String RELEASELOCK_SCRIPT =
        "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 释放分布式锁
     *
     * @param lockKey
     *            锁
     * @param requestId
     *            请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(String lockKey, String requestId) {
        Object result = jedisCluster.eval(RELEASELOCK_SCRIPT, Collections.singletonList(lockKey),
            Collections.singletonList(requestId));
        return Objects.equals(SUCCESS, result);
    }

    /**
     * 初始化 JedisCluster
     */

    private void initJedisCluster() {
        LOGGER.info("redisClusterConfig={}", redisClusterConfig.toString());
        LOGGER.info("redisPoolConfig={}", redisPoolConfig.toString());
        Set<HostAndPort> node = getNode(redisClusterConfig.getNodes());
        final String pwd = redisClusterConfig.getPwd();
        JedisPoolConfig jedisPoolConfig = getJedisPoolConfig();
        jedisCluster = new JedisCluster(node, 2000, 2000, 5, pwd, jedisPoolConfig);
    }

    /**
     * 获取JedisPoolConfig
     *
     * @return JedisPoolConfig
     */
    private JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisPoolConfig.getMaxTotal());
        jedisPoolConfig.setMaxIdle(redisPoolConfig.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisPoolConfig.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(redisPoolConfig.getMaxWaitMillis());
        return jedisPoolConfig;
    }

    /**
     * 解析redis机器的ip+port
     *
     * @param nodes
     *            nodes
     * @return node
     */
    private Set<HostAndPort> getNode(String nodes) {
        Set<HostAndPort> node = new HashSet<>();
        String[] hostAndPorts = nodes.split(",");
        for (String hostAndPort : hostAndPorts) {
            String host = hostAndPort.split(":")[0];
            int port = Integer.parseInt(hostAndPort.split(":")[1]);
            HostAndPort hap = new HostAndPort(host, port);
            node.add(hap);
        }
        return node;
    }
}
