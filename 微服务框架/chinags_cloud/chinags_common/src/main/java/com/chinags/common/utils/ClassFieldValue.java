package com.chinags.common.utils;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 反射实体封装
 * @author Zhang.Mr
 * @param <T>
 */
public class ClassFieldValue<T> {

    /**
     * 反射获取字段值
     * @param key 获取参数
     * @param t 实体对象
     * @return
     */
    public Object classFieldValue(String key, T t){
        try {
            Class<?> clazz = t.getClass();
            Field declaredField = clazz.getDeclaredField(key);
            declaredField.setAccessible(true);
            Object o = declaredField.get(t);
            return o;
        } catch (IllegalAccessException e) {
            new Exception("无获取属性", e).printStackTrace();
        } catch (NoSuchFieldException e) {
            new Exception("获取不到指定对象私有属性", e).printStackTrace();
        }
        return null;
    }

    /**
     * 反射获取字段值
     * @param t 实体对象
     * @return
     */
    public Map<String,Object> classMapValue(T t){
        try {
            Map<String,Object> map = MapUtils.newHashMap();
            Class<?> clazz = t.getClass();
//            Field[] declaredFields = clazz.getDeclaredFields();
            Field[] declaredFields = getAllFields(t);
            for (Field field: declaredFields){
                field.setAccessible(true);
                map.put(field.getName(),field.get(t));
            }
            return map;
        } catch (IllegalAccessException e) {
            new Exception("无获取属性", e).printStackTrace();
        }
        return null;
    }

    /**
     * 获取父类以及方法
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object){
        Class clazz = object.getClass();
        List<Field> fieldList = ListUtils.newArrayList();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
