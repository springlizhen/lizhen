package com.chinags.device.plan.entity;

import com.chinags.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_ENGINEERING")
public class Enginnering extends BaseEntity {
    @Transient
    private Integer count;
    @Id
    @GenericGenerator(name = "engineering-uuid", strategy = "uuid")
    @GeneratedValue(generator = "engineering-uuid")
    private String id;
    /**
     * 工程名称
     */
    private String enginneringName;
    /**
     * 工程类型
     */
    private String enginneringType;
    /**
     * 计划id
     */
    private String enginneringPlan;

}
