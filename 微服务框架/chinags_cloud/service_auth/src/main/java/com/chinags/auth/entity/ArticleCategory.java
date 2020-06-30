package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_PUB_SYS_ARTICLE_CATEGORY")
public class ArticleCategory extends BaseEntity {

    public ArticleCategory() {
    }

    public ArticleCategory(String id) {
        this.categoryCode = id;
    }

    @Transient
    private String id;

    /**
     * 分类编码
     */
    @Id
    @GenericGenerator(name = "database-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "categoryCode", nullable = false, length = 19)
    private String categoryCode;

    /**
     * 父级编号
     */
    private String parentCode;

    /**
     * 所有父级编号
     */
    private String parentCodes;

    /**
     * 本级排序号
     */
    private Integer treeSort;

    /**
     * 所有级别排序号
     */
    private String treeSorts;

    /**
     * 是否最末级
     */
    private String treeLeaf;

    /**
     * 层次级别
     */
    private Integer treeLevel;

    /**
     * 全节点名
     */
    private String treeNames;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 是否显示（1显示 0隐藏）
     */
    private String isShow;

    /**
     * 是否有子级
     * @return
     */
    public Boolean getIsTreeLeaf() {
        return "1".equals(this.treeLeaf);
    }

    /**
     * 是否有父级
     * @return
     */
    public boolean getIsRoot() {
        return "0".equals(this.getParentCode());
    }

    /**
     * 是否新增
     * @return
     */
    public Boolean getIsNewRecord() {
        return StringUtils.isEmpty(id);
    }

    public String getParentName(){
        String[] names = treeNames.split("/");
        if(names.length>=2)
            return names[names.length-2];
        return "";
    }

    public String getId() {
        return categoryCode;
    }

}
