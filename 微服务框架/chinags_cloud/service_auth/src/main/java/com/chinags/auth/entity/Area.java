package com.chinags.auth.entity;

import com.chinags.common.entity.TreeEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_PUB_SYS_AREA")
public class Area extends TreeEntity{

    public Area() {
    }

    public Area(String id) {
        this.areaCode = id;
    }

    @Transient
    private String id;
    @Transient
    private Boolean isNewRecord;
    /**
     * 区域代码
     */
    @Id
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
     */
    private String areaType;
    /**
     * 区域简称
     */
    private String areaSimpleName;
    /**
     * 区域面积
     */
    private String proportion;

    public String getId() {
        return areaCode;
    }

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
