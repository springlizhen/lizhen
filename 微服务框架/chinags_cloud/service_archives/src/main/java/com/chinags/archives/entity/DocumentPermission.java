package com.chinags.archives.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 文档权限表
 * @author XieWenqing
 * @date 2019/11/21 下午 5:28
 */
@Data
@Entity
@Table(name = "T_COA_DOCUMENT_PERMISSION")
public class DocumentPermission {
    @Id
    @GenericGenerator(name = "database-uuid", strategy = "uuid")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    private Date startDate;		// 权限开始时间
    private Date endDate;		// 权限结束时间
    private String cdId;		// 所属案卷著录id
    private String cdfId;		// 所属卷内文件id
    private String cdTitle;		// 所属案卷著录标题
    private String cdfTitle;		// 所属卷内文件标题
    private String permissionTime;		// 期限，0永久1有时间限制
    private String userId;		// 阅读权限用户id，因为慧正只能获取到用户登录名（登录名也是唯一值），这里存储用户登录名loginCode

    private String createBy;  //创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;  //创建时间
    private String updateBy;  //更新人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;  //更新时间

}
