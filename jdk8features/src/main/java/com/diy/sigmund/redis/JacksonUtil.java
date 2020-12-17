package com.diy.sigmund.redis;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
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
public class JacksonUtil {
    /**
     * 对象映射器
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);

    /**
     * 序列化时格式化，按一定格式反序列化
     *
     * "date":"2020-11-25 21:24:31"
     *
     * date=Wed Nov 25 21:18:21 CST 2020
     */
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OBJECT_MAPPER.setDateFormat(dateFormat);
    }

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
            LOGGER.info("toJson value null");
            return "";
        }
        String jsonString = "";
        try {
            jsonString = OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception exception) {
            LOGGER.error("toJson error, value={}, exception={}", value, exception);
        }
        return jsonString;
    }

    /**
     * 反序列化对象
     * 
     * 例如：JsonUtil.toObject(mapJson, new TypeReference<Map<Integer, String>>() {});
     * 
     * @param content
     *            content
     * @param typeReference
     *            typeReference
     * @param <T>
     *            <T>
     * @return 结果信息
     */
    public static <T> T toObject(String content, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(content)) {
            LOGGER.info("toObject content isEmpty");
            return null;
        }
        T object = null;
        try {
            object = OBJECT_MAPPER.readValue(content, typeReference);
        } catch (Exception exception) {
            LOGGER.error("toObject error, content={}, exception={}", content, exception);
        }
        return object;
    }

}
