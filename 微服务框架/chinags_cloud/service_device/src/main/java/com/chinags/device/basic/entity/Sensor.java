package com.chinags.device.basic.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.lang.StringUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_SENSOR")
public class Sensor extends BaseEntity {

    @Transient
    private Boolean isNewRecord;

    @Id
    @GenericGenerator(name = "sensor-uuid", strategy = "uuid")
    @GeneratedValue(generator = "sensor-uuid")
    private String id;
    /**
     * 设备code
     */
    private String fieldCode;
    /**
     * 传感器编码
     */
    private String sensorCode;
    /**
     * 传感器名称
     */
    private String sensorName;
    /**
     * 传感器信号
     */
    private String sensorSignal;

    public void setIsNewRecord(Boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Boolean getIsNewRecord() {
        this.isNewRecord = this.isNewRecord==null? StringUtils.isBlank(this.getId()):this.isNewRecord;
        return this.isNewRecord;
    }

}
