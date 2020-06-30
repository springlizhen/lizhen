package com.chinags.dbra.entity;

/**
 * 通用属性传递信息类
 * @author Mark_Wang
 * @date 2019/7/1
 **/
public class BasicData {
    /**
     * 属性名称
     */
    private String name;
    /**
     * 属性数据类型
     */
    private String type;
    /**
     * 属性注释
     */
    private String comments;
    /**
     * 属性是否为空
     */
    private String able;
    /**
     * 属性是否是主键
     */
    private String key;
    /**
     * 属性值
     */
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAble() {
        return able;
    }

    public void setAble(String able) {
        this.able = able;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
