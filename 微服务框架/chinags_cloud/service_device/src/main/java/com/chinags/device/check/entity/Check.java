package com.chinags.device.check.entity;

import com.chinags.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="T_COA_CHECK")
public class Check extends BaseEntity {
    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    private String id;
    /**
     * 设备代码
     */
    private String checkName;
    /**
     * 设备名称
     */
    private String checkId;
    /**
     * 设备代码
     */
    private String checkCenter;
    /**
     * 设备名称
     */
    private String manageStation;
    /**
     * 设备代码
     */
    private String manageOffice;
    /**
     * 设备名称
     */
    private String longitude;
    private String latitude;
    /**
     * 设备代码
     */
    private String checkDescription;
    /**
     * 设备名称
     */
    private String checkDeviation;





}
