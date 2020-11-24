package com.diy.sigmund.redis;

import java.util.Objects;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json工具类，对jackson包的应用
 * 
 * @author ylm-sigmund
 * @since 2020/11/21 13:16
 */
public class JsonUtil {
    /**
     * 对象映射器
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 序列化对象为字符串
     * 
     * OBJECT_MAPPER.writerWithDefaultPrettyPrinter() 漂亮打印机
     * 
     * @param value
     *            value
     * @return String
     */
    public static String toJson(Object value) {
        if (Objects.isNull(value)) {
            return "";
        }
        String jsonString = "";
        try {
            jsonString = OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception exception) {
            LOGGER.error("toJson error, value is {}, exception is {}", value, exception);
        }
        return jsonString;
    }

    /**
     * 反序列化对象
     * 
     * 例如：JsonUtil.toObject(mapJson, () -> new TypeReference<Map<Integer, String>>() {});
     * 
     * @param content
     *            content
     * @param supplier
     *            supplier
     * @param <T>
     *            <T>
     * @return 结果信息
     */
    public static <T> T toObject(String content, Supplier<TypeReference<T>> supplier) {
        T object = null;
        try {
            final TypeReference<T> valueType = supplier.get();
            object = OBJECT_MAPPER.readValue(content, valueType);
        } catch (Exception exception) {
            LOGGER.error("toObject error, content is {}, exception is {}", content, exception);
        }
        return object;
    }
}
