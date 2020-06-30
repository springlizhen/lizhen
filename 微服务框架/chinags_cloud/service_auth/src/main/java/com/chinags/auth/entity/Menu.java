package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Data
@Entity
@Table(name = "T_PUB_SYS_MENU")
public class Menu extends BaseEntity {

    public Menu() {
    }

    public Menu(String id) {
        this.menuCode = id;
    }

    @Transient
    private String id;

    /**
     * 菜单编码
     */
    @Id
    @GenericGenerator(name = "database-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "menuCode", nullable = false, length = 19)
    private String menuCode;
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
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单类型（1菜单 2权限 3开发）
     */
    private String menuType;
    /**
     * 菜单链接
     */
    private String menuHref;
    /**
     * 目标
     */
    private String menuTarget;
    /**
     * 图标
     */
    private String menuIcon;
    /**
     * 颜色
     */
    private String menuColor;
    /**
     * 权限标识
     */
    private String permission;
    /**
     * 是否显示（1显示 0隐藏）
     */
    private String isShow;
    /**
     * 归属系统
     */
    private String sysCode;
    /**
     * 归属模块
     */
    private String moduleCodes;

    /**
     * 是否为菜单
     * @return
     */
    public Boolean getIsMenu() {
        return "1".equals(this.menuType);
    }

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
     * 是否权限
     * @return
     */
    public Boolean getIsPerm() {
        return "2".equals(this.menuType);
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
        return menuCode;
    }
}
