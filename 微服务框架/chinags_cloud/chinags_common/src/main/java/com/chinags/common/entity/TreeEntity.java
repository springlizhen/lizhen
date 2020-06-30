package com.chinags.common.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class TreeEntity extends BaseEntity{

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

    public String getParentName(){
        String[] names = treeNames.split("/");
        if(names.length>=2) {
            return names[names.length - 2];
        }
        return "";
    }
}
