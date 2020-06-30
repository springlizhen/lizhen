package com.chinags.auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_PUB_SYS_ROLE_DATA_SCOPE")
public class RoleDataScope {
    private static final long serialVersionUID = 2L;

    @Transient
    private String roleDataScopeListJson;
    @Transient
    private String dataScope;

    @Id
    @GenericGenerator(name = "column-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "column-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    private String roleCode;
    private String ctrlType;
    private String ctrlData;
    private String ctrlPermi;

    private String authCode;
    private String roleType;

}
