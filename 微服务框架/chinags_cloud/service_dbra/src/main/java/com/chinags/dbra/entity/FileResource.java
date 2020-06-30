package com.chinags.dbra.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author : Mark_wang
 * @Date : 2019-8-29
 **/
@Data
@Entity
@Table(name = "T_META_FILE_RESOURCE")
public class FileResource {

    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;
    /**
     * 档案API编码
     */
    @Id
    @GenericGenerator(name = "table-uuid", strategy = "uuid")
    @GeneratedValue(generator = "table-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    /**
     * 档案服务名称
     */
    private String name;
    /**
     * 档案类型
     */
    private String type;
    /**
     * 案卷号
     */
    private String fileNo;
    /**
     * 年度
     */
    private String year;
    /**
     * 编制单位
     */
    private String company;
    /**
     * 责任人
     */
    private String leader;
    /**
     * 页号
     */
    private Integer pageNum;
    /**
     * 页数
     */
    private Integer pageNumber;
    /**
     * 案卷名称
     */
    private String fileName;
    /**
     * 下载地址
     */
    private String host;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 开放状态
     */
    private String status;
    /**
     * 案卷大小
     */
    private String fileSize;
    /**
     * 申请次数
     */
    private int applyNum;
    /**
     * 下载次数
     */
    private int downNum;
    /**
     * 档案ID
     */
    private String fileId;

    public Integer getPageNo() {
        return pageNo==null?0:pageNo-1;
    }

    public Integer getPageSize() {
        return pageSize==null?20:pageSize;
    }

}
