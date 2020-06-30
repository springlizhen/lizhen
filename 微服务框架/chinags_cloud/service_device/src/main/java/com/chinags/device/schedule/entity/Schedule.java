package com.chinags.device.schedule.entity;

import com.chinags.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 工程计划进度管理
 * @Author : Mark_wang
 * @Date : 2019-9-9
 **/
@Data
@Entity
@Table(name = "T_ENM_SCHEDULE")
public class Schedule {
    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;
    @Id
    @GenericGenerator(name = "device-uuid", strategy = "uuid")
    @GeneratedValue(generator = "device-uuid")
    /**
     * 项目进度id
     */
    private String id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 年度
     */
    private int year;
    /**
     * 合同金额
     */
    private Double money;
    /**
     * 合同编号
     */
    private String htId;
    /**
     * 关联计划
     */
    private String linkPlan;
    /**
     * 施工单位
     */
    private String construction;
    /**
     * 招标代理结构
     */
    private String bidAgn;
    /**
     * 计划开工日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date planStartDate;
    /**
     * 计划完工日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date planEndDate;
    /**
     * 实际开工日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 实际完工日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改日期
     */
    private Date updateDate;
    /**
     * 完成金额
     */
    private Double endMoney;

    public Integer getPageNo() {
        return pageNo==null?0:pageNo-1;
    }

    public Integer getPageSize() {
        return pageSize==null?20:pageSize;
    }


}
