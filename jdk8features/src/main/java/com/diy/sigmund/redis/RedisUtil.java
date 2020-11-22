package com.diy.sigmund.redis;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ylm-sigmund
 * @since 2020/11/20 20:43
 */
@Component
public class RedisUtil {
    private static JedisCluster jedisCluster;

    private RedisClusterConfig redisClusterConfig;

    public RedisUtil(RedisClusterConfig redisClusterConfig) {
        this.redisClusterConfig = redisClusterConfig;
    }

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 解析redis机器的ip+port
     * 
     * @param nodes
     *            nodes
     * @return node
     */
    private Set<HostAndPort> parseHostAndPort(String nodes) {
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

    /**
     * 初始化 JedisCluster
     */

    private void initJedisCluster() {
        Set<HostAndPort> node = parseHostAndPort(redisClusterConfig.getNodes());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 注意：这里超时时间不要太短，他会有超时重试机制。而且其他像httpclient、dubbo等RPC框架也要注意这点
        jedisCluster = new JedisCluster(node, 1000, 1000, 1, redisClusterConfig.getPwd(), jedisPoolConfig);
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
    public static boolean setex(String key, Object value, int timeout, TimeUnit timeUnit) {
        try {
            int seconds = (int)timeUnit.toSeconds(timeout);
            jedisCluster.setex(key, seconds, JsonUtil.toJson(value));
            return true;
        } catch (Exception e) {
            LOGGER.error("Redis setex key {} value {} timeout {} timeUnit {}", key, value, timeout, timeUnit);
            return false;
        }
    }

    /**
     * 返回与键 key 相关联的值（已将字符串处理为对象）。
     * 
     * 例如：RedisUtil.get(key1, () -> new TypeReference<User>() {});
     *
     * @param key
     *            key
     * @param supplier
     *            supplier
     * @param <T>
     *            <T>
     * @return return
     */
    public static <T> T get(String key, Supplier<TypeReference<T>> supplier) {
        try {
            final String json = jedisCluster.get(key);
            return JsonUtil.toObject(json, supplier);
        } catch (Exception e) {
            LOGGER.error("Redis get key {}", key);
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
        } catch (Exception e) {
            LOGGER.error("Redis expire key {}", key);
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
        } catch (Exception e) {
            LOGGER.error("Redis exists key {}", key);
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
        } catch (Exception e) {
            LOGGER.error("Redis ttl key {}", key);
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
        } catch (Exception e) {
            LOGGER.error("Redis del key {}", key);
            return null;
        }
    }
}
