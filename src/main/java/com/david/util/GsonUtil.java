package com.david.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private GsonUtil() {

    }

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss") // 日期格式化
            .serializeNulls()                     // 序列化 null 值
            .setPrettyPrinting()                  // 輸出美化
            .disableHtmlEscaping()                // 禁用 HTML 特殊字符轉義
            .create();

    /**
     * 將對象轉換為 JSON 字符串
     *
     * @param object 要序列化的對象
     * @return JSON 字符串
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return GSON.toJson(object);
    }

    /**
     * 將 JSON 字符串轉換為對象
     *
     * @param json  JSON 字符串
     * @param clazz 目標對象類型
     * @param <T>   泛型類型
     * @return 反序列化後的對象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return GSON.fromJson(json, clazz);
    }

    /**
     * 將 JSON 字符串轉換為對象（用於泛型類型）
     *
     * @param json JSON 字符串
     * @param type 目標對象的類型
     * @param <T>  泛型類型
     * @return 反序列化後的對象
     */
    public static <T> T fromJson(String json, Type type) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return GSON.fromJson(json, type);
    }

    /**
     * 將 JSON 字符串轉換為 List
     *
     * @param json  JSON 字符串
     * @param clazz List 中的元素類型
     * @param <T>   泛型類型
     * @return 反序列化後的 List
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return GSON.fromJson(json, type);
    }

    /**
     * 將 JSON 字符串轉換為 Map
     *
     * @param json       JSON 字符串
     * @param keyClass   Map 的鍵類型
     * @param valueClass Map 的值類型
     * @param <K>        鍵的類型
     * @param <V>        值的類型
     * @return 反序列化後的 Map
     */
    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        Type type = TypeToken.getParameterized(Map.class, keyClass, valueClass).getType();
        return GSON.fromJson(json, type);
    }

}
