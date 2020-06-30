package com.chinags.device.basic.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_DEVICE_OFFICE_PB")
public class DerviceOfficePb extends BaseEntity {

    @Transient
    private Boolean isNewRecord;

    private String checked;

    @Id
    @GenericGenerator(name = "derf-uuid", strategy = "uuid")
    @GeneratedValue(generator = "derf-uuid")
    private String id;
    private String name;

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }
}
