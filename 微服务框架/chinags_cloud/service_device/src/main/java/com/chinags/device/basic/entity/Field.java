package com.chinags.device.basic.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_FIELD")
public class Field extends BaseEntity {

    @Transient
    private Boolean isNewRecord;

    @Id
    @GenericGenerator(name = "field-uuid", strategy = "uuid")
    @GeneratedValue(generator = "field-uuid")
    private String id;
    /**
     * 字段
     */
    private String fieldCode;
    /**
     * 字段说明
     */
    private String fieldName;
    /**
     * 字段类型，分为：int（整数）、double（小数）、varchar（字符）、dic（字典）
     */
    private String fieldType;
    /**
     * 如果是字典类型，需绑定字典的唯一标识符
     */
    private String fieldDicBind;
    /**
     * 字典类型名称
     */
    private String fieldDicBindName;
    /**
     * 单位（量）
     */
    private String fieldUnit;
    /**
     * 字段归类（1、基本数据，2、设计参数，3、主要工程量；根据需求可以自行增加类型）
     */
    private String fieldClass;
    /**
     * 是否必填
     */
    private String fieldRequired;
    /**
     * 分组
     */
    private String fieldGroup;
    /**
     * 分组name
     */
    private String fieldGroupName;

    /**
     * 是否公共字段
     */
    private String fieldPb;

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
