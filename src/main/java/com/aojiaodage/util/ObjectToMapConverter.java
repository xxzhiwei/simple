package com.aojiaodage.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

// Object -> Map转换工具
public class ObjectToMapConverter {
    public static Map<String, Object> convert(Object object) {
        Map<String, Object> map = new HashMap<>();
        Field[] declaredFields = object.getClass().getDeclaredFields();

        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
