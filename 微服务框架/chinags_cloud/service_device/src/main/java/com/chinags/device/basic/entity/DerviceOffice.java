package com.chinags.device.basic.entity;

import com.chinags.common.entity.TreeEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_DERVICE_OFFICE")
public class DerviceOffice extends TreeEntity{

    public DerviceOffice(){};

    /**
     * 机构部门
     */
    public static final String OFFICETYPETWO = "2";

    public DerviceOffice(String id) {
        this.officeCode = id;
    }

    @Transient
    private String id;
    @Transient
    private Boolean isNewRecord;
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
    private String areaId;
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
    /**
     * 公共分组
     */
    private String fieldGroup;

    public String getId() {
        return officeCode;
    }

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
