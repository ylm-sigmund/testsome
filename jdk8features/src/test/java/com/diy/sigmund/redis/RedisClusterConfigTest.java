package com.diy.sigmund.redis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.diy.sigmund.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

/**
 * @author ylm-sigmund
 * @since 2020/11/20 20:50
 */
// @ComponentScan("com.diy.sigmund")//该注解在单元测试下无效
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration属性locations和classes只能取一个，否则会报错configure one or the other, but not both.
// @ContextConfiguration(locations = {"classpath:applicationContext.xml"},classes = BeansConfig.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RedisClusterConfigTest {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClusterConfigTest.class);
    @Autowired
    private RedisClusterConfig redisClusterConfig;

    /**
     * 测试执行lua脚本
     */
    @Test
    public void testEval() {
        String script1 = "return redis.call('set',KEYS[1],ARGV[1])";
        String script2 = "return redis.call('get',KEYS[1])";
        final List<String> list1 = Stream.of("e").collect(Collectors.toList());
        final Object eval1 = RedisUtil.eval(script1, list1, list1);
        LOGGER.info(Objects.requireNonNull(eval1).toString());
        LOGGER.info(Objects.requireNonNull(RedisUtil.eval(script2, list1, list1)).toString());

    }

    /**
     * redis集群常规测试
     */
    @Test
    public void setex() {
        final User user = new User();
        user.setId(1234);
        user.setName("jackson包");
        String key1 = "qqq";
        LOGGER.info("key1 是否存在 {}", RedisUtil.exists(key1));
        final boolean setex = RedisUtil.put(key1, user, 1, TimeUnit.MINUTES);
        LOGGER.info("setex key1 是否成功 {}", setex);
        LOGGER.info("key1 过期时间 {} 秒", RedisUtil.ttl(key1));
        final Long expire = RedisUtil.expire(key1, 1, TimeUnit.HOURS);
        LOGGER.info("expire key1 设置过期时间 {}", expire);
        LOGGER.info("key1 过期时间 {} 秒", RedisUtil.ttl(key1));
        final User user1 = RedisUtil.get(key1, new TypeReference<User>() {});
        LOGGER.info("get key1 {}", Objects.requireNonNull(user1).toString());
        final Long del = RedisUtil.del(key1);
        LOGGER.info("del key1 {}", del);
        LOGGER.info("key1 是否存在 {}", RedisUtil.exists(key1));
        final Long expire1 = RedisUtil.expire(key1, 1, TimeUnit.MINUTES);
        LOGGER.info("expire key1 设置过期时间 {}", expire1);
        LOGGER.info("key1 过期时间 {} 秒", RedisUtil.ttl(key1));
    }

    /**
     * 测试序列化和反序列化，jackson的应用
     */
    @Test
    public void testJackson() {
        LOGGER.info("key1 是否存在 {}", RedisUtil.exists("key1"));
        // 对象
        final User user = new User();
        user.setId(1234);
        user.setName("jackson包");
        // 序列化
        final String json = JacksonUtil.toJson(user);
        LOGGER.info("user={}", json);
        // 反序列化
        final User user1 = JacksonUtil.toObject(json, new TypeReference<User>() {});
        LOGGER.info(user1.toString());

        // map
        final Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(2234, "jackson包");
        // 序列化
        final String mapJson = JacksonUtil.toJson(map);
        LOGGER.info(mapJson);
        // 反序列化
        final Map<Integer, String> hashMap =
            JacksonUtil.toObject(mapJson, new TypeReference<Map<Integer, String>>() {});
        LOGGER.info(hashMap.toString());

        // list
        final List<User> collect = Stream.of(user).collect(Collectors.toList());
        // 序列化
        final String listJson = JacksonUtil.toJson(collect);
        LOGGER.info(listJson);
        // 反序列化
        final List<User> arrayList = JacksonUtil.toObject(listJson, new TypeReference<List<User>>() {});
        LOGGER.info(arrayList.toString());
    }

    /**
     * jackson泛型使用示例
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testGeneric() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        // List 泛型使用示例
        String JSONInString = "[{\"id\":1234,\"name\":\"jackson包\"}]";
        CollectionType JavaType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
        List<User> personList = objectMapper.readValue(JSONInString, JavaType);
        List<User> personList1 = objectMapper.readValue(JSONInString, new TypeReference<List<User>>() {});

        // Map 泛型使用示例
        String JSONInString1 = "{\"2234\":\"jackson包\"}";
        MapType JavaType1 = objectMapper.getTypeFactory().constructMapType(HashMap.class, Integer.class, String.class);
        Map<Integer, String> personMap2 = objectMapper.readValue(JSONInString1, JavaType1);
        Map<Integer, String> personMap3 =
            objectMapper.readValue(JSONInString1, new TypeReference<Map<Integer, String>>() {});
        LOGGER.info("222");
    }

    /**
     * 序列化反序列化时间
     */
    @Test
    public void testToJsonForDate() {
        // final ObjectMapper objectMapper = new ObjectMapper();
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // objectMapper.setDateFormat(dateFormat);
        final User user = new User();
        user.setId(1234);
        user.setName("jackson包");
        user.setDate(new Date());

        // {"id":1234,"name":"jackson包","date":"2020-11-25 21:04:19"}
        LOGGER.info(JacksonUtil.toJson(user));
        LOGGER.info(JacksonUtil.toObject("{\"id\":1234,\"name\":\"jackson包\",\"date\":\"2020-11-25 21:18:21\"}",
            new TypeReference<User>() {}).toString());
    }

    @Test
    public void testEmptyToObject() {
        final User user = JacksonUtil.toObject("", new TypeReference<User>() {});
        LOGGER.info(user != null ? user.toString() : null);
    }

}