package com.chinags.dbra.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Author : Mark_wang
 * @Date : 2019-8-27
 **/
@Data
@Entity
@Table(name = "t_meta_api_param")
public class ApiParam {

    @Id
    @GenericGenerator(name = "table-uuid", strategy = "uuid")
    @GeneratedValue(generator = "table-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    /**
     * api id
     */
    private String apiId;
    /**
     *  参数名
     */
    private String name;
    /**
     * 参数是否为必填
     */
    private String isNull;
    /**
     * 参数类型
     */
    private String colType;
    /**
     * 类型（通用参数，私有参数）
     */
    private String type;
    /**
     * 描述
     */
    private String remarks;
}
