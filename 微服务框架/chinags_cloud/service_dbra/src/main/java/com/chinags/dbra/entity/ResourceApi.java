package com.chinags.dbra.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 数据资源共享实体
 * @author Mark_Wang
 * @date 2019/7/1
 **/
@Entity
@Table(name = "t_meta_resource_api")
public class ResourceApi {

    @Id
    @GenericGenerator(name = "share-uuid", strategy = "uuid")
    @GeneratedValue(generator = "share-uuid")
    @Column(name = "id", nullable = false, length = 64)
    /**
     * 数据共享id
     */
    private String id;
    /**
     * 数据库id
     */
    private String reId;
    /**
     * 共享的字段集合
     *
     * 改字段用于生成sql和前端查看已选择那些属性用
     */
    private String columns;
    /**
     * 生成的sql
     */
    private String sql;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改日期
     */
    private Date updateDate;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 数据库id
     */
    private String dbId;
    /**
     * 数据表id
     */
    private String tbId;
    /**
     * 统计记录数sql
     */
    private String countSql;
    /**
     * api访问地址
     */
    private String apiHost;
    /**
     * api统计记录数访问地址
     */
    private String apiCountHost;
    /**
     * 功能说明
     */
    private String sumup;
    /**
     * 版本号
     */
    private String version;
    /**
     * 查询所有数据sql
     */
    private String allSql;
    @GeneratedValue(generator = "share-uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReId() {
        return reId;
    }

    public void setReId(String reId) {
        this.reId = reId;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
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

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getTbId() {
        return tbId;
    }

    public void setTbId(String tbId) {
        this.tbId = tbId;
    }

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public String getApiCountHost() {
        return apiCountHost;
    }

    public void setApiCountHost(String apiCountHost) {
        this.apiCountHost = apiCountHost;
    }

    public String getSumup() {
        return sumup;
    }

    public void setSumup(String sumup) {
        this.sumup = sumup;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAllSql() {
        return allSql;
    }

    public void setAllSql(String allSql) {
        this.allSql = allSql;
    }

    public ResourceApi() {

    }
}
