package com.chinags.common.utils;

import com.chinags.common.collect.ListUtils;

import java.util.List;

/**
 * 集合提取指定条件工具类
 * @author Zhang.Mr
 * @param <T>
 */
public class ResultKeyList<T> {

    private List<String> list;

    private List<Object> listObject;

    private ResultMap<T> resultMap = new ResultMap<>();

    private ClassFieldValue classFieldValue = new ClassFieldValue();

    public ResultKeyList(){
        this.list = ListUtils.newArrayList();
        this.listObject = ListUtils.newArrayList();
    }

    /**
     * 获取对象指定属性集合 String
     * @param lists
     * @param key
     * @return
     */
    public List<String> getStrings(List<T> lists, String key) {
        for (T t : lists) {
            Object o = classFieldValue.classFieldValue(key, t);
            list.add((String) o);
        }
        return list;
    }

    /**
     * 获取对象指定属性集合 String，符合指定条件
     * @param param 返回对象属性
     * @param key 帅选对象属性
     * @param value 帅选对象属性值
     * @return
     */
    public List<String> resultKeyList(String param, String key, String... value){
        List<T> lists = resultMap.resultLists(true, key, value);
        return getStrings(lists, param);
    }

    /**
     * 获取对象指定属性集合
     * @param lists  对象集合
     * @param key 返回对象属性
     * @return
     */
    public List<Object> resultList(List<T> lists, String key){
        for (T t : lists) {
            Object o = classFieldValue.classFieldValue(key, t);
            listObject.add(o);
        }
        return listObject;
    }


}
