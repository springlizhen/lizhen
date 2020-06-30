package com.chinags.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

    public static final String STATUS_NORMAL = "0";

    public static final String STATUS_DELETE = "1";

    @Column(name = "CREATE_BY", updatable=false)
    protected String createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false,name = "CREATE_DATE")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd", timezone = "GMT+8")
    protected Date createDate;

    @Column(name = "UPDATE_BY")
    protected String updateBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateDate;

    @Column(name = "REMARKS")
    protected String remarks;

    @Column(name = "STATUS")
    protected String status;

    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;
    @Transient
    private String desc;
    @Transient
    private String flowname;
    @Transient
    private String sendusername;
    @Transient
    private String sendtime;
    @Transient
    private String trackid;
    @Transient
    private String subjectionType;

    public String getOrderBy() {
        if(orderBy==null) {
            return null;
        }
        String orderBy = this.orderBy.split("%20")[0];
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNo() {
        return pageNo==null?0:pageNo-1;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize==null?20:pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Sort.Direction getDesc() {
        if(orderBy==null) {
            return Sort.Direction.ASC;
        }
        String desc = this.orderBy.split("%20")[1];
        return "desc".equals(desc)?Sort.Direction.DESC:Sort.Direction.ASC;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
