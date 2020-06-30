package com.chinags.dbra.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 主题分类
 * @Author : Mark_wang
 * @Date : 2019-9-19
 **/
@Data
@Entity
@Table(name = "T_META_THEME")
public class Theme {
    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;
    @Transient
    private String img;
    @Transient
    private int resourceSum;
    @Id
    @GenericGenerator(name = "database-uuid", strategy = "uuid")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "id", nullable = false, length = 64)
    /**
     * 主题分类id
     */
    private String id;
    /**
     * 主题分类名称
     */
    private String name;

    private String createBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    private String updateBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    public Integer getPageNo() {
        return pageNo==null?0:pageNo-1;
    }

    public Integer getPageSize() {
        return pageSize==null?20:pageSize;
    }
}
