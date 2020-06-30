package com.chinags.device.plan.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_PLAN")
public class Plan extends BaseEntity {

    @Transient
    private Boolean isNewRecord;

    @Id
    @GenericGenerator(name = "plan-uuid", strategy = "uuid")
    @GeneratedValue(generator = "plan-uuid")
    private String id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 计划编号
     */
    private String planCode;
    /**
     * 计划类型
     */
    private String planType;
    /**
     * 年度
     */
    private String planYear;
    /**
     * 计划说明
     */
    private String planMessage;
    /**
     * 附件
     */
    private String planAnnex;
    /**
     * 计划审核状态（0未审核，1审核通过，2审核失败,3分中心已提交）
     */
    private String planStatus;
    /**
     * 计划上报状态（0未上报，1已上报）
     */
    private String planReport;
    /**
     * 填报人
     */
    private String planPepName;
    /**
     * 填报人机构id
     */
    private String planOffice;
    /**
     * 填报人机构名称
     */
    private String planOfficeName;
    /**
     * 管理站id
     */
    private String stationId;
    /**
     * 管理站名称
     */
    private String stationName;
    /**
     * 分中心id
     */
    private String planParentId;
    /**
     * 分中心Name
     */
    private String planParentName;

    /**
     * 分中心计划id
     */
    private String planId;
    /**
     * 分中心计划name
     */
    private String planNames;

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
