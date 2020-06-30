package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "T_PUB_SYS_POST")
public class Post extends BaseEntity{

    public Post(){}

    public Post(String id) {
        this.postCode = id;
    }


    @Transient
    private String id;
    @Transient
    private Boolean isNewRecord;
    /**
     * 岗位编码
      */
    @Id
    private String postCode;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 岗位分类（高管、中层、基层）
     */
    private String postType;
    /**
     * 岗位排序（升序）
     */
    private Integer postSort;

    /**
     * 所属机构
     */
    @ManyToOne
    @JoinColumn(name = "office_code")
    private Office officeCode;

    public String getId() {
        return postCode;
    }

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
