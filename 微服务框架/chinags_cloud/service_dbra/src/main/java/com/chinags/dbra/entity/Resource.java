package com.chinags.dbra.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 数据资源服务目录
 * @author Mark_Wang
 * @date 2019/7/17
 **/
@Data
@Entity
@Table(name = "t_meta_resource")
public class Resource {
    /**
     * 数据量
     */
    @Transient
    private Long dataSize;
    /**
     * 数据量
     */
    @Transient
    private String themeName;
    /**
     * 资源编码
     */
    @Id
    @GenericGenerator(name = "table-uuid", strategy = "uuid")
    @GeneratedValue(generator = "table-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    /**
     * 资源中文名称
     */
    private String nameCn;
    /**
     * 资源英文名称
     */
    private String nameUs;
    /**
     * 数据资源摘要
     */
    private String sumup;
    /**
     * 提供单位
     */
    private String provider;
    /**
     * 发布日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 所属行业
     */
    private String subBusiness;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 数据库id
     */
    private String dbId;
    /**
     * 数据表id
     */
    private String tbId;
    /**
     * 所属主题
     */
    private String theme;
    /**
     * 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 开放状态
     */
    private String status;
    /**
     * 下载次数
     */
    private int downNum;
    /**
     * 浏览次数
     */
    private int catNum;

    private String createBy;

    private String updateBy;

}
