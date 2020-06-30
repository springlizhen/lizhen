package com.chinags.device.threshold.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 工程安全阈值设置entity
 * @author XieWenqing
 * @date 2019/12/10 上午 11:25
 */
@Data
@Entity
@Table(name="T_COA_THRESHOLD_VAL")
public class ThresholdVal {
    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;
    @Transient
    private String orgId;  //所属机构id

    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    private String id;
    private String createBy;  //创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;  //创建时间
    private String updateBy;  //更新人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;  //更新时间
    private String remarks;  //备注

    private String measureType;  //测点类型
    private String measurePoint;  //测点种类（高程测点，水平测点）.
    private String deceiveId;  //测点id
    private String deceiveCode;  //设备简称+设备编号
    private Double nowUpper;  //高程测点：本期沉降阈值上限/水平测点：X轴本期沉降阈值上限
    private Double nowLower;  //高程测点：本期沉降阈值下限/水平测点：X轴本期沉降阈值下限
    private Double addUpper;  //高程测点：累计沉降阈值上限/水平测点：X轴累计沉降阈值上限
    private Double addLower;  //高程测点：累计沉降阈值下限/水平测点：X轴累计沉降阈值下限
    private String isUse;  //是否使用，0否1是
    private String isShow;  //列表是否显示该数据，0否1是
    //以下4个值只有水平测点进行显示
    private Double yNowUpper;  //Y轴本期沉降阈值上限
    private Double yNowLower;  //Y轴本期沉降阈值下限
    private Double yAddUpper;  //Y轴累计沉降阈值上限
    private Double yAddLower;  //Y轴累计沉降阈值下限
}
