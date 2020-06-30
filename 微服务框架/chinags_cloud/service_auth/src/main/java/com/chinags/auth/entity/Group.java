package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_PUB_SYS_GROUP")
public class Group extends BaseEntity{

    public Group(){}

    @Transient
    private Boolean isNewRecord;
    /**
     * 岗位编码
      */
    @Id
    @GenericGenerator(name = "group-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "group-uuid")
    @Column(name = "id", nullable = false, length = 19)
    private String id;
    /**
     * 岗位名称
     */
    private String groupName;
    /**
     * 岗位排序（升序）
     */
    private Integer groupSort;

    private String userCode;
    private String userName;

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
