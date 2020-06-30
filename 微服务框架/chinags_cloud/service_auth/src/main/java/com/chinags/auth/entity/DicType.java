package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_PUB_SYS_DICT_TYPE")
public class DicType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Transient
    private Boolean isNewRecord;

    @Id
    @GenericGenerator(name = "dic-type-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "dic-type-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 是否系统字典
     */
    private String isSys;
    /**
     * 系统
     */
    private String sysCode;

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}