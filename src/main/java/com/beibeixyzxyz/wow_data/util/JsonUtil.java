package com.beibeixyzxyz.wow_data.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER;
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 设置时区
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 设置时间格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }
    /**
     * Object 转换成json字符串
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        return objectToJson(obj,OBJECT_MAPPER);
    }

    /**
     * Object 转换成json字符串
     * @param obj
     * @param objectMapper
     * @return
     */
    public static String objectToJson(Object obj,ObjectMapper objectMapper){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Object转换Json出错",e);
            throw new RuntimeException("Object转换Json出错", e);
        }
    }
    /**
     * 对象 转换为json字符串,忽略空值
     * @param obj
     * @return
     */
    public static String obj2jsonIgnoreNull(Object obj) {
        return obj2jsonIgnoreNull(obj,new ObjectMapper());
    }
    /**
     * 对象 转换为json字符串,忽略空值
     * @param obj
     * @param objectMapper
     * @return
     */
    public static String obj2jsonIgnoreNull(Object obj,ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectToJson(obj,objectMapper);
    }
    /**
     * json 转 对象
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String jsonString, Class<T> clazz) {
        return jsonToObject(jsonString,clazz,OBJECT_MAPPER);
    }

    /**
     * Json 转 对象
     * @param jsonString
     * @param clazz
     * @param objectMapper
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String jsonString, Class<T> clazz,ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            logger.error("Json转换对象出错",e);
            throw new RuntimeException("Json转换对象出错", e);
        }
    }
    /**
     * json字符串转换为map
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonTomap(String jsonString) {
        return jsonTomap(jsonString,OBJECT_MAPPER);
    }
    /**
     * json字符串转换为map
     * @param jsonString
     * @param objectMapper
     * @return
     */
    public static Map<String, Object> jsonTomap(String jsonString,ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return jsonToObject(jsonString,Map.class,objectMapper);
    }
    /**
     * 深度转换json成list
     * @param json
     * @return
     */
    public static List<Object> jsonToListDeeply(String json){
        return jsonToListDeeply(json, OBJECT_MAPPER);
    }

    /**
     * 把json解析成list，如果list内部的元素存在jsonString，继续解析
     * @param json
     * @param mapper 解析工具
     * @return
     */
    public static List<Object> jsonToListDeeply(String json, ObjectMapper mapper) {
        if (json == null) {
            return null;
        }
        try {
            List<Object> list = mapper.readValue(json, List.class);
            for (Object obj : list) {
                if (obj != null && obj instanceof String) {
                    String str = (String) obj;
                    if (str.startsWith("[")) {
                        obj = jsonToListDeeply(str, mapper);
                    } else if (obj.toString().startsWith("{")) {
                        obj = jsonToMapDeeply(str, mapper);
                    }
                }
            }
            return list;
        } catch (JsonProcessingException e) {
            logger.error("Json转换list失败",e);
            throw new RuntimeException("Json转换list失败", e);
        }

    }
    /**
     * 深度转换json成map
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMapDeeply(String json){
        return jsonToMapDeeply(json, OBJECT_MAPPER);
    }

    /**
     * 深度转换把json解析成map，
     * 如果map内部的value存在jsonString，继续解析
     * @param json
     * @param mapper
     * @return
     */
    public static Map<String, Object> jsonToMapDeeply(String json, ObjectMapper mapper){
        if (json == null) {
            return null;
        }
        try {
            Map<String, Object> map = mapper.readValue(json, Map.class);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object obj = entry.getValue();
                if (obj != null && obj instanceof String) {
                    String str = ((String) obj);
                    if (str.startsWith("[")) {
                        List<?> list = jsonToListDeeply(str, mapper);
                        map.put(entry.getKey(), list);
                    } else if (str.startsWith("{")) {
                        Map<String, Object> mapRecursion = jsonToMapDeeply(str, mapper);
                        map.put(entry.getKey(), mapRecursion);
                    }
                }
            }
            return map;
        } catch (JsonProcessingException e) {
            logger.error("Json转换map失败",e);
            throw new RuntimeException("Json转换map失败", e);
        }
    }

    /**
     * json数组字符串转换为列表
     * @param jsonArrayStr
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonToList(String jsonArrayStr) throws Exception {
        List<T> lst = (List<T>) OBJECT_MAPPER.readValue(jsonArrayStr, List.class);
        return lst;
    }
    /**
     * json数组字符串转换为列表
     * @param jsonArrayStr
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonToList(String jsonArrayStr, TypeReference<List<T>> typeReference) throws Exception {
        List<T> lst = OBJECT_MAPPER.readValue(jsonArrayStr, typeReference);
        return lst;
    }


    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    /**
     * object  转JavaBean
     */
    public static <T> T obj2pojo(Object obj, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }


}