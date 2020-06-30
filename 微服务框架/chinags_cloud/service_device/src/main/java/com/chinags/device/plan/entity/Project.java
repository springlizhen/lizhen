package com.chinags.device.plan.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_PROJECT")
public class Project extends BaseEntity {

    @Transient
    private Boolean isNewRecord;
    @Transient
    private String EnginnerName;
    @Id
    @GenericGenerator(name = "project-uuid", strategy = "uuid")
    @GeneratedValue(generator = "project-uuid")
    private String id;
    /**
     * 工程id
     */
    private String enginneringId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目类型
     */
    private String projectType;
    /**
     * 项目类型
     */
    private String projectTypeName;
    /**
     * 单位
     */
    private String projectUnit;
    /**
     * 单价
     */
    private String projectPrice;
    /**
     * 数量
     */
    private String projectQuantity;
    /**
     * 合价
     */
    private String projectPriceall;
    /**
     * 机构id
     */
    private String orgId;

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
