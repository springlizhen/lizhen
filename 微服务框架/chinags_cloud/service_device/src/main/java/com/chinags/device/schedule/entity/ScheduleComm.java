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
 * 工程进度验收
 * @Author : Mark_wang
 * @Date : 2019-9-9
 **/
@Data
@Entity
@Table(name = "T_ENM_SCHEDULE_COMM")
public class ScheduleComm {
    @Id
    @GenericGenerator(name = "device-uuid", strategy = "uuid")
    @GeneratedValue(generator = "device-uuid")
    /**
     * 工程进度验收ID
     */
    private String id;
    /**
     * 工程进度ID
     */
    private String scheduleId;
    /**
     * 验收类型
     */
    private String ysType;
    /**
     * 验收内容描述
     */
    private String content;
    /**
     * 验收单位
     */
    private String company;
    /**
     * 验收时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date ysDate;
    /**
     * 项目负责人
     */
    private String proLeader;
    /**
     * 工程科负责人
     */
    private String engLeader;
    /**
     * 项目专业工程师
     */
    private String engineer;
    /**
     * 设计单位
     */
    private String desCompany;
    /**
     * 监理单位
     */
    private String supCompany;
    /**
     * 施工单位名称
     */
    private String conCompany;
    /**
     * 施工单位负责人
     */
    private String conCompanyLeader;
    /**
     * 验收结论
     */
    private String verdict;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 是否归档
     */
    private int status;
}
