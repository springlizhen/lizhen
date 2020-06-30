package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "T_PUB_SYS_ARTICLE")
public class Article extends BaseEntity{

    public static final String STATUS_DELETE = "1";

    public Article(){}

    public Article(String id) {
        this.articleCode = id;
    }

    @Transient
    private String id;

    @Transient
    private Boolean isNewRecord;

    /**
     * 文章编码
      */
    @Id
//    @GenericGenerator(name = "database-uuid", strategy = "org.hibernate.id.UUIDGenerator")
//    @GeneratedValue(generator = "database-uuid")
    @Column(name = "articleCode", nullable = false, length = 19)
    private String articleCode;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 是否显示（1显示 0隐藏）
     */
    private String isShow;

    /**
     * 是否首页轮播（1是 0否）
     */
    private String isIndexCarousel;

    /**
     * 所属分类
     */
    @ManyToOne
    @JoinColumn(name = "article_category_code")
    private ArticleCategory articleCategory;

    /**
     * 展示图
     */
    @Transient
    private String img;

    /**
     * 文章创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ARTICLE_DATE")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date articleDate;

    /**
     * 审核状态（0 审核中 1 已通过 2 已驳回）
     */
    private String auditStatus;

    /**
     * 审核信息
     */
    private String auditInfo;

    /**
     * 是否需要二次审核（0 不需要 1 需要）
     */
    private String isSecondAudit;

    /**
     * 二次审核状态（0 审核中 1 已通过 2 已驳回）
     */
    private String secondAuditStatus;

    /**
     * 二次审核信息
     */
    private String secondAuditInfo;

    /**
     * 审核人ID
     */
    private String auditId;

    /**
     * 二次审核人ID
     */
    private String secondAuditId;

    /**
     * 审核进度（0 审核中 1 已通过 2 已驳回）
     */
    private String auditProgress;

    /**
     * 创建用户ID
     */
    private String userId;

    public String getId() {
        return articleCode;
    }

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }

}
