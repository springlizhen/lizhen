package com.chinags.common.utils;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.collect.SetUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 返回值支持string类型，后期拓展
 * 通过反射读取对象指定字段，返回指定字段数组字段或指定对象
 * result均为map，key对应比较字段数据，value对应数组
 * @author Zhang.Mr
 */
public class ResultMap<T> {

    private Map<String, List<T>> mapList;

    private Map<String, List<String>> mapStringList;

    /**
     * 实体类原集合
     */
    private List<T> entityList = ListUtils.newArrayList();
    /**
     * 实体类Map集合
     */
    private List<Map<String,Object>> entityListMap = ListUtils.newArrayList();

    private ClassFieldValue<T> classFieldValue = new ClassFieldValue<>();

    public ResultMap(){
        mapStringList = MapUtils.newHashMap();
        mapList = MapUtils.newHashMap();
    }

    /**
     * 集合放入
     * @param entityList
     */
    public void setEntityList(List<T> entityList){
        this.entityList = entityList;
        entityListMap.clear();
        for (T t : entityList){
            Map<String, Object> map = classFieldValue.classMapValue(t);
            entityListMap.add(map);
        }
    }

    /**
     *
     * @param value 返回字段
     * @param key 判断帅选字段
     */
    public Map<String, List<String>> resultString(boolean status,String key, String value) {
        Set<String> valtype = valTypeList(key);
        for (String km: valtype) {
            mapStringList.put(km, valObjectListString(status, key, km, value));
        }
        return mapStringList;
    }

    /**
     *
     * @param key 判断帅选字段
     * @return Map map的key为key不同的值
     */
    public Map<String, List<T>> resultList(boolean status,String key) {
        Set<String> valtype = valTypeList(key);
        for (String km: valtype) {
            mapList.put(km, valObjectList(status, key, km));
        }
        return mapList;
    }

    /**
     * 通过对象字段对应value帅选集合
     * @param key 判断帅选字段
     * @return Map map的key为传入参数value
     */
    public Map<String, List<T>> resultListStrings(boolean status,String key, String... value) {
        for (String km: value) {
            mapList.put(km, valObjectList(status, key, km));
        }
        return mapList;
    }

    /**
     *  通过对象字段对应value帅选集合
     * @param key 判断帅选字段
     */
    public List<T> resultLists(boolean status, String key, String... value) {
        List<T> list = ListUtils.newArrayList();
        for (String km: value) {
            list.addAll(valObjectList(status, key, km));
        }
        return list;
    }

    /**
     * 获取指定字段不同的值 ，不会存在重复数据
     * @param key
     * @return 返回set集合，将会自动去重
     */
    public Set<String> valTypeList(String key){
        Set<String> valtype = SetUtils.newHashSet();
        for (T t : entityList) {
            valtype.add((String) classFieldValue.classFieldValue(key, t));
        }
        return valtype;
    }

    /**
     * 获取指定字段不同的值 ，不会存在重复数据
     * @param key 指定字段
     * @param val  对应字段值
     * @param value 返回字段
     * @return 返回字符串集合
     */
    private List<String> valObjectListString(boolean status, String key, String val, String value){
        Map<String, Object> map = MapUtils.newHashMap();
        map.put(key, val);
        return valObjectList(status, map ,value);
    }

    /**
     * 获取指定字段不同的值 ，不会存在重复数据
     * @param key 指定字段
     * @param val  对应字段值
     * @return 返回对象集合
     */
    private List<T> valObjectList(boolean status, String key, String val){
        Map<String, Object> map = MapUtils.newHashMap();
        map.put(key, val);
        return valObjectList(status, map);
    }

    /**
     * 条件帅选 AND
     * @param map
     * @return
     */
    public List<T> valObjectList(boolean status, Map<String,Object> map){
        List<T> list = ListUtils.newArrayList();
        int mou = 0;
        head:
        for (Map<String, Object> mval:entityListMap) {
            mou++;
            if (status) {
                if (mapBoolean(map, mval)) continue head;
            }else{
                if (mapErrorBoolean(map, mval)) continue head;
            }
            list.add(entityList.get(mou-1));
        }
        return list;
    }

    /**
     * 条件帅选 AND
     * @param map
     * @return
     */
    public List<String> valObjectList(boolean status, Map<String,Object> map, String key){
        List<String> list = ListUtils.newArrayList();
        int mou = 0;
        head:
        for (Map<String, Object> mval:entityListMap) {
            mou++;
            if (status) {
                if (mapBoolean(map, mval)) continue head;
            }else{
                if (mapErrorBoolean(map, mval)) continue head;
            }
            list.add((String) classFieldValue.classFieldValue(key, entityList.get(mou-1)));
        }
        return list;
    }

    /**
     * 判断字段相等循环
     * @param map
     * @param mval
     * @return
     */
    private boolean mapBoolean(Map<String, Object> map, Map<String, Object> mval) {
        for (Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            Object o = mval.get(entry.getKey());
            if (!o.equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字段不相等循环
     * @param map
     * @param mval
     * @return
     */
    private boolean mapErrorBoolean(Map<String, Object> map, Map<String, Object> mval) {
        for (Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            Object o = mval.get(entry.getKey());
            if (!o.equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }
}