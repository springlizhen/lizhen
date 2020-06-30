package com.chinags.archives.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author XieWenqing
 * @date 2019/9/29 下午 2:19
 */
@Data
@Entity
@Table(name = "T_COA_CLERK_DOCUMENT_FILE")
public class ClerkDocumentFile {
    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;
    @Transient
    private String clerkDocumentId2;  // 所属案卷id，查询使用
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate_gte;  //查询使用
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate_lte;  //查询使用
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate_gte;  //查询使用
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate_lte;  //查询使用

    private String receivetype;  //类型，0所有1用户2部门
    private String receiveNames;
    private String receiveCodes;

    @Id
    @GenericGenerator(name = "database-uuid", strategy = "uuid")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    private String caseNo;		// 全宗号
    private String catalogNo;		// 目录号
    private String archivesNo;		// 案卷号
    @JsonFormat(pattern = "yyyy")
    private Date year;		// 年度
    private String officeCode;		// 机构id
    private String clerkDocumentId;		// 所属案卷id
    private String storageTime;		// 保管期限
    private String secretLevel;		// 密级（秘密，机密，绝密）
    private Integer sort;		// 序号
    private String responsiblePerson;		// 责任者
    private String fileNo;		// 文号
    private String title;		// 文件标题
    private Date time;		// 登记日期
    private String pageNumber;		// 页号
    private Integer pages;		// 页数
    private String subjectTerm;		// 主题词
    private String type;		// 文件类型（批复，请示）
    private String category;		// 类别（归档，预归档，不归档）
    private String docClass;  //档案类型
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;		// 起始时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;		// 终止时间
    private String filePath;  //上传附件的路径（包含自动生成的乱码文件名），多个文件用“|”隔开
    private String fileName;  //上传附件的名字，多个文件用“|”隔开
    private String isPublic;  //是否公共档案，0否1是
    private String createBy;  //创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;  //创建时间
    private String updateBy;  //更新人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;  //更新时间
    private String remarks;  //备注
    private String engFileType;  //工程档案分类（工程档案专用）：工程主体档案、工程设备、其他（数据字典管理）

    private String place;	// 存放位置
    private String videoPeople;		//视频人物
    private String location;		//拍摄地点
    private String documentType;	//公文种类（收文发文，字典管理）
    private String boxNo;	// 盒号
    private Integer pieceNo;	// 件数
    private String organization;	//单位（来文单位等）
    private String companyUnit;		//编制单位
}
