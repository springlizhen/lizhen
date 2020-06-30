package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "T_PUB_SYS_USER")
public class SysUser extends BaseEntity {

    public static final String OP_AUTH = "auth";

    public SysUser(){};
    @Transient
    private String id;
    @Transient
    private Boolean isNewRecord;
    @Transient
    private String userRoleString;
    @Transient
    private String op;
    @Id
    private String userCode;
    private String loginCode;
    private String userName;
    private String password;
    private String email;
    private String mobile;
    private String phone;
    private String sex;
    private String avatar;
    private String sign;
    private String wxOpenid;
    private String mobileImei;
    private String userType;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "ref_code")
    private Employee employee;
    private String refName;
    private String mgrType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private Integer pwdSecurityLevel;
    private Date pwdUpdateDate;
    private String pwdUpdateRecord;
    private String pwdQuestion;
    private String pwdQuestionAnswer;
    private String pwdQuestion_2;
    private String pwdQuestionAnswer_2;
    private String pwdQuestion_3;
    private String pwdQuestionAnswer_3;
    private Date pwdQuestUpdateDate;
    private String lastLoginIp;
    private Date lastLoginDate;
    private Date freezeDate;
    private String freezeCause;
    private Integer userWeight;
    private String corpCode;
    private String corpName;
    private String idCardNo;
    private String nativePlace;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name="T_PUB_SYS_USER_ROLE",	//用来指定中间表的名称
            //用于指定本表在中间表的字段名称，以及中间表依赖的是本表的哪个字段
            joinColumns= {@JoinColumn(name="user_code")},
            //用于指定对方表在中间表的字段名称，以及中间表依赖的是它的哪个字段
            inverseJoinColumns= {@JoinColumn(name="role_code")}
    )
    @Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE , org.hibernate.annotations.CascadeType.DELETE})
    private Set<Role> roleSet;

    public String getId() {
        return userCode;
    }

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
