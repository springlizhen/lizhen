package com.chinags.device.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 工程进度项目
 * @Author : Mark_wang
 * @Date : 2019-9-9
 **/
@Data
@Entity
@Table(name = "T_ENM_SCHEDULE_PROJECT")
public class ScheduleProject {
    @Id
    @GenericGenerator(name = "device-uuid", strategy = "uuid")
    @GeneratedValue(generator = "device-uuid")
    /**
     * 项目ID
     */
    private String id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 工程量比例
     */
    private String quantityRatio;
    /**
     * 计划金额(万元)
     */
    private Double planMoney;
    /**
     * 实际金额(万元)
     */
    private Double money;
    /**
     * 状态
     */
    private String status;
    /**
     * 完成时间
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
     * 工程进度ID
     */
    private String scheduleId;

}
