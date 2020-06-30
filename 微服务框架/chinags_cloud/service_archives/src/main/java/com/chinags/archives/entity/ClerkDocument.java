package com.chinags.archives.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author XieWenqing
 * @date 2019/11/19 下午 6:15
 */

@Data
@Entity
@Table(name = "T_COA_CLERK_DOCUMENT")
public class ClerkDocument {
    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;

    @Id
    @GenericGenerator(name = "database-uuid", strategy = "uuid")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    private String caseNo;	// 全宗号
    private String catalogNo;	// 目录号
    private String archivesNo;	// 案卷号
    @JsonFormat(pattern = "yyyy")
    private Date year;	// 年度
    private String storageTime;		// 保管期限，永久，长期，短期
    private String secretLevel;		// 密级（普通，秘密，机密，绝密）
    private Integer pieceNo;		// 件数
    private String responsiblePerson;	// 责任者
    private String title;		// 案卷标题
    private Date startDate;		// 起始时间
    private Date endDate;		// 终止时间
    private String officeCode;	// 机构id
    private String docClass;  	//档案类型

    private String boxNo;	// 盒号
    private Integer pages;	// 页数
    private String item;	// 项目名称
    private String libraryNo;	//存放地点-库号
    private String cabinetNo;	//存放地点-柜号
    private String gridNo;	//存放地点-格号
    private String location;	//工程位置
    private String height;  //高度
    private String length;	//长度
    private String width;	//宽度
    private String cost;	// 造价
    private Date time;		// 时间
    private String structure;	// 结构
    private String area;		// 面积
    private String tier;		// 层数

    private String createBy;  //创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;  //创建时间
    private String updateBy;  //更新人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;  //更新时间
    private String remarks;  //备注

}
