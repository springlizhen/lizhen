package com.chinags.dbra.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 数据表实体
 * @author Mark_Wang
 * @date 2019/6/26
 * @time 11:31
 **/
@Entity
@Table(name = "t_meta_table")
public class DbTable {
    /**
     * 数据表id
     */
    @Id
    @GenericGenerator(name = "table-uuid", strategy = "uuid")
    @GeneratedValue(generator = "table-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    /**
     * 数据库id
     */
    private String dbId;
    /**
     * 数据表名称
     */
    private String name;
    /**
     * 数据表备注
     */
    private String remarks;
    /**
     * 数据表中文名
     */
    private String nameCn;
    /**
     * 数据表主键
     */
    private String primaryKey;
    /**
     * 数据表注释
     */
    private String notes;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改日期
     */
    private Date updateDate;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 修改者
     */
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

}
