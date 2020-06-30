package com.chinags.dbra.entity;

import com.chinags.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 第三方服务
 * @author Mark_Wang
 * @date 2019/7/17
 **/
@Data
@Entity
@Table(name = "t_meta_other_resource")
public class OtherResource extends BaseEntity {
    /**
     * 服务id
     */
    @Id
    private String id;
    /**
     * 服务名称
     */
    private String name;
    /**
     * 接入类型
     */
    private String accessType;
    /**
     * 服务分类
     */
    private String serviceClass;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 服务访问地址
     */
    private String host;
    /**
     * 状态
     */
    private String status;
    /**
     * 服务信息说明
     */
    private String remarks;

    /**
     * 服务简介
     */
    private String sumup;

    /**
     * 提供单位
     */
    private String provider;
    /**
     * 调用次数
     */
    private int callNum;
    /**
     * 应用连接数
     */
    private int connNum;
    /**
     * 接口调用地址
     */
    private String url;

}
