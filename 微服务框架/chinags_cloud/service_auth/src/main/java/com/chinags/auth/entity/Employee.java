package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_PUB_SYS_EMPLOYEE")
public class Employee extends BaseEntity {

    @Transient
    private String id;
    @Transient
    private Boolean isNewRecord;
    @Transient
    private String postCode;
    @Transient
    private String loginCode;
    /**
     * 员工编码
     */
    @Id
    private String empCode;
    /**
     * 员工姓名
     */
    private String empName;
    /**
     * 员工英文名
     */
    private String empNameEn;
    /**
     * 机构编码
     */
    private String officeCode;
    /**
     * 机构名称
     */
    private String officeName;
    /**
     * 公司编码
     */
    private String companyCode;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 租户编码
     */
    private String corpCode;
    /**
     * 租户名称
     */
    private String corpName;
    /**
     * 岗位
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name="T_PUB_SYS_EMPLOYEE_POST",	//用来指定中间表的名称
            //用于指定本表在中间表的字段名称，以及中间表依赖的是本表的哪个字段
            joinColumns= {@JoinColumn(name="emp_code")},
            //用于指定对方表在中间表的字段名称，以及中间表依赖的是它的哪个字段
            inverseJoinColumns= {@JoinColumn(name="post_code")}
    )
    @Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    private Set<Post> postSet;

    public String getId() {
        return empCode;
    }

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
