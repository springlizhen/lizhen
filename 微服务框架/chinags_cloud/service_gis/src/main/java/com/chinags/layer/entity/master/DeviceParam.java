package com.chinags.layer.entity.master;

import com.chinags.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_ENM_DEVICE_PARAM")
public class DeviceParam extends BaseEntity {

    @Transient
    private Boolean isNewRecord;

    @Id
    @GenericGenerator(name = "param-uuid", strategy = "uuid")
    @GeneratedValue(generator = "param-uuid")
    private String id;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 代码字段id
     */
    private String codeId;
    /**
     * 数值1
     */
    private String value1;
    /**
     * 数值2
     */
    private String value2;
}
