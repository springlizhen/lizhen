package com.chinags.auth.entity;

import com.chinags.common.entity.TreeEntity;
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
@Table(name = "T_PUB_SYS_DICT_DATA")
public class DicData extends TreeEntity {

    @Transient
    private Boolean isNewRecord;

    @Column(updatable=false)
    private String id;
    /**
     * 系统内置
     */
    private String isSys;
    /**
     * 字典键值
     */
    private String dictValue;
    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典描述
     */
    private String description;
    /**
     * 字典编码
     */
    @Id
    @GenericGenerator(name = "dic-data-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "dic-data-uuid")
    @Column(name = "dictCode", nullable = false, length = 64)
    private String dictCode;

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getDictCode()):this.isNewRecord;
        return this.isNewRecord;
    }

}
