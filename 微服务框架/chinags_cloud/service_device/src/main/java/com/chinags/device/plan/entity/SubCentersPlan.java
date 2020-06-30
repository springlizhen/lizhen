package com.chinags.device.plan.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_SUBCENTERS_PLAN")
public class SubCentersPlan extends BaseEntity {
    @Transient
    private Boolean isNewRecord;
    @Transient
    private String planIds;

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
     * 分中心id
     */
    private String stationId;
    /**
     * 分中心名称
     */
    private String stationName;
    /**
     * 计划上报状态（0未上报，1已上报）
     */
    private String planReport;
    /**
     * 计划审核状态（0未审核，1审核通过，2审核失败）
     */
    private String planStatus;

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
