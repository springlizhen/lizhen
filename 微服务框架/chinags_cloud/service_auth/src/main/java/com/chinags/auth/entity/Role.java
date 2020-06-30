package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "T_PUB_SYS_ROLE")
public class Role extends BaseEntity {
    private static final long serialVersionUID = 4L;

    public Role(){};

    public Role(String id) {
        this.roleCode = id;
    }

    @Transient
    private String id;
    @Transient
    private Boolean isNewRecord;
    @Transient
    private String roleMenuListJson;
    @Transient
    private String op;
    @Transient
    private String userRoleString;
    /**
     * 角色编码
     */
    @Id
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色分类（高层，中层、基层、其他）
     */
    private String roleType;
    /**
     * 角色排序（升序）
     */
    private Integer roleSort;
    /**
     * 系统内置（0是1否）
     */
    private String isSys;
    /**
     * 用户类型（employee员工 member会员）
     */
    private String userType;
    /**
     * 数据范围设置（0未设置 1全部数据 2自定义数据3本部门数据）
     */
    private String dataScope;
    /**
     * 租户代码
     */
    private String corpCode;
    /**
     * 租户名称
     */
    private String corpName;
    /**
     * 菜单
     */
    @ManyToMany
    @JoinTable(name="T_PUB_SYS_ROLE_MENU",	//用来指定中间表的名称
            //用于指定本表在中间表的字段名称，以及中间表依赖的是本表的哪个字段
            joinColumns= {@JoinColumn(name="role_code")},
            //用于指定对方表在中间表的字段名称，以及中间表依赖的是它的哪个字段
            inverseJoinColumns= {@JoinColumn(name="menu_code")}
    )
    @Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    private Set<Menu> menuSet;

    public String getId() {
        return roleCode;
    }

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }

}
