package com.chinags.device.basic.entity;

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
@Table(name = "T_PUB_SYS_DICT_DATAT")
public class SensorType extends TreeEntity {

    @Transient
    private Boolean isNewRecord;

    @Id
    @GenericGenerator(name = "sensor-data-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "sensor-data-uuid")
    private String id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 类型编码
     */
    private String code;
    /**
     * 类型描述
     */
    private String description;
    /**
     * 排序
     */
    private String sort;

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }

}
