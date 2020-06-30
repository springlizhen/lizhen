package com.chinags.layer.entity.base;

import com.chinags.common.entity.TreeEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_PUB_SYS_OFFICE")
public class Office {

    public Office(){};

    /**
     * 机构部门
     */
    public static final String OFFICETYPETWO = "2";

    public Office(String id) {
        this.officeCode = id;
    }


    @Transient
    private String id;

    /**
     * 机构编码
     */
    @Id
    private String officeCode;
    /**
     * 机构代码（作为显示用，多租户内唯一）
     */
    private String viewCode;
    /**
     * 机构名称
     */
    private String officeName;
    /**
     * 机构全称
     */
    private String fullName;
    /**
     * 机构类型（1单位2部门）
     */
    private String officeType;
    /**
     * 负责人
     */
    private String leader;
    /**
     * 电话
     */
    private String phone;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 邮政编码
     */
    private String zipCode;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 所属区域
     */

    /**
     * 机构等级，单选，省局1级，分局2级，管理站/处3级，管理所4级
     */
    private String officeLevel;
    /**
     * 所属调度单元，单选，省局1级，分局2级，管理站/处3级，管理所4级
     */
    private String officeUnit;
    /**
     * 现地所分类（第4级机构使用），泵站所，渠道所，管道所
     */
    private String areaType;
    /**
     * 机构职能
     */
    private String function;
    /**
     * 行政机构级别
     */
    private String administrativeLevel;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;



}
