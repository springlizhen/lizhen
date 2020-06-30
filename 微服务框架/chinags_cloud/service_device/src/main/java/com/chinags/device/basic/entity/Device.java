package com.chinags.device.basic.entity;

import com.chinags.common.entity.TreeEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;

/**
 * @author Administrator
 */
@Data
@Entity
@Table(name = "T_ENM_DEVICE")
public class Device extends TreeEntity{

    public Device(){};

    public static final String FACILITYTYPETWO = "2";

    @Transient
    private String nodeid;
    @Transient
    private Boolean isNewRecord;
    @Transient
    private Map<String,String> dicParams;
    @Transient
    private String fieldCode;
    @Transient
    private String fieldValue;
    /**
     * 所在机构名称
     */
    @Transient
    private String orgName;
    /**
     * 查询使用，机构id
     */
    @Transient
    private String[] orgIds;
    @Id
    @GenericGenerator(name = "device-uuid", strategy = "uuid")
    @GeneratedValue(generator = "device-uuid")
    private String id;
    /**
     * 设备代码
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 简称
     */
    private String deviceShortName;
    /**
     * 所属类别
     */
    private String deviceClass;
    /**
     * 所属类别名称
     */
    private String deviceClassName;
    /**
     * 所属设备
     */
    private String deviceRelegation;
    /**
     * 所属设备
     */
    private String deviceRelegationName;
    /**
     * 所在机构
     */
    private String orgId;
    /**
     * 所在机构名称
     */
    private String orgIdName;
    /**
     * 所在地区划代码
     */
    private String areaCode;
    /**
     * 所在地区划代码
     */
    private String areaName;
    /**
     * 参控状态
     */
    private String ctlStatus;
    /**
     * 启用状态
     */
    private String useStatus;
    /**
     * 坐标
     */
    private String devicePosition;
    /**
     * 所属调度单元，单选，省局1级，分局2级，管理站/处3级，管理所4级
     */
    private String schUnit;
    /**
     * 排序
     */
    private String sort;
    /**
     * 设备类型
     */
    private String typeDicId;
    /**
     * 是否传感器
     */
    private String cgqStatus;
    /**
     * 传感器基准点编号/office/form
     */
    private String cgqCode;
    /**
     * 传感器观测点编号
     */
    private String cgqGcdCode;
    /**
     * 初始值（X轴）
     */
    private String cgqValue;
    /**
     * Y轴初始值
     */
    private String cgqValueYz;
    /**
     * 传感器类型
     */
    private String cgqType;
    /**
     * 传感器类型名称
     */
    private String cgqTypeName;
    /**
     * 设备编号
     */
    private String deviceNumber;

    /**
     * 分中心id
     */
    private String planParentId;
    /**
     * 管理站id
     */
    private String stationId;
    /**
     * 管理所id
     */
    private String officeId;
    @Transient
    private Map<String,Object> map;

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
