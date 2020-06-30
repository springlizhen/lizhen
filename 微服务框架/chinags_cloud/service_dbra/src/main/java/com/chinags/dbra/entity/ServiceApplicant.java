package com.chinags.dbra.entity;

import com.chinags.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Mark_Wang
 * @date 2019/7/22
 **/
@Data
@Entity
@Table(name = "t_meta_service_applicant")
public class ServiceApplicant extends BaseEntity {
    @Id
    @GenericGenerator(name = "applicant-uuid", strategy = "uuid")
    @GeneratedValue(generator = "applicant-uuid")
    @Column(name = "id", nullable = false, length = 64)
    /**
     * 服务注册id
     */
    private String id;
    /**
     * 服务名称
     */
    private String name;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 申请时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 审核状态
     */
    private String status;
    /**
     * 服务id
     */
    private String resourceId;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 用户token
     */
    private String token;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 审核人
     */
    private String updateBy;
    /**
     * 审核世间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 服务分类（this:本地数据服务 other:第三方服务 file:档案服务）
     */
    private String type;
}
