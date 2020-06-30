package com.chinags.file.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Mark_Wang
 * @date 2019/7/26
 **/
@Entity
@Table(name = "t_pub_file")
public class FileUpload {

    /**
     * 文件id
     */
    @Id
    @GenericGenerator(name = "database-uuid", strategy = "uuid")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件所属
     */
    private String pid;
    /**
     * 文件地址
     */
    private String address;
    /**
     * 文件页数
     */
    private String pageSize;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建用户名
     */
    private String userName;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 预览文件存放路径
     */
    private String pdfAddress;
    /**
     * 预览文件状态（0：存在  2.不存在  3.文件太大不允许预览）
     */
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getPdfAddress() {
        return pdfAddress;
    }

    public void setPdfAddress(String pdfAddress) {
        this.pdfAddress = pdfAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
