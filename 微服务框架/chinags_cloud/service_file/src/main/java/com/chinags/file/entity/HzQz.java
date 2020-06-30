package com.chinags.file.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 慧正签章
 * @Author : Mark_wang
 * @Date : 2020-2-17
 **/
@Entity
@Table(name = "t_pub_hzqz")
@Data
public class HzQz {
    /**
     * 编号id
     */
    @Id
    @GenericGenerator(name = "database-uuid", strategy = "uuid")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    /**
     * 慧正id
     */
    private String hzId;
    /**
     * 合同档案id
     */
    private String documentId;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 页数
     */
    private String pageSize;
}
