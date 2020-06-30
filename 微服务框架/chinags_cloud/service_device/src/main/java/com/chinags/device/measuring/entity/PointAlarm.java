package com.chinags.device.measuring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 报警测点
 * @Author : Mark_Wang
 * @Date : 2020/3/5
 **/
@Data
@Entity
@Table(name = "t_enm_point_alarm")
public class PointAlarm {

    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;
    @Transient
    private String sheibei;
    @Transient
    private String suoshu;
    @Transient
    private String chaochu;
    @Id
    @GenericGenerator(name = "device-uuid", strategy = "uuid")
    @GeneratedValue(generator = "device-uuid")
    /**
     * id
     */
    private String id;
    /**
     * 观测段编号
     */
    private String sctionId;
    /**
     * 基准点编号
     */
    private String pointId;
    /**
     * 分中心
     */
    private String subCenter;
    /**
     * 管理站
     */
    private String station;
    /**
     * 管理所
     */
    private String office;
    /**
     * 设施
     */
    private String equ;
    /**
     * 初始值
     */
    private Double inValue;
    /**
     * 期数
     */
    private int dateNum;
    /**
     * 测点类型
     */
    private String type;
    /**
     * 测点种类
     */
    private String such;
    /**
     * 高程/水平x轴
     */
    private Double altitude;
    /**
     * 高程中误差/水平x轴中误差
     */
    private Double altitudeError;
    /**
     * 填报人
     */
    private String createBy;
    /**
     * 填报时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**
     * 本期沉浮/水平x轴本期沉浮
     */
    private Double upDown;
    /**
     * 累计沉浮/水平x轴累计沉浮
     */
    private Double upDownSum;
    /**
     * 水平Y轴
     */
    private Double yAltitude;
    /**
     * 水平Y轴中误差
     */
    private Double yAltitudeError;
    /**
     * 水平Y轴本期沉浮
     */
    private Double yUpDown;
    /**
     * 水平Y轴累计沉浮
     */
    private Double yUpDownSum;
    /**
     * 测点编号
     */
    private String pid;
    /**
     * 本期上限
     */
    private Double nowUpper;
    /**
     * 本期下限
     */
    private Double nowLower;
    /**
     * 累计上限
     */
    private Double addUpper;
    /**
     * 累计下限
     */
    private Double addLower;
    /**
     * 报警信息
     */
    private String msg;
    /**
     * 处理状态
     */
    private int status;
    /**
     * y轴初始值
     */
    private Double yInValue;
    /**
     * y轴本期上限
     */
    private Double yNowUpper;
    /**
     * y轴本期下限
     */
    private Double yNowLower;
    /**
     * y轴累计上限
     */
    private Double yAddUpper;
    /**
     * y轴累计下限
     */
    private Double yAddLower;



    public Integer getPageNo() {
        return pageNo==null?0:pageNo-1;
    }

    public Integer getPageSize() {
        return pageSize==null?20:pageSize;
    }
}
