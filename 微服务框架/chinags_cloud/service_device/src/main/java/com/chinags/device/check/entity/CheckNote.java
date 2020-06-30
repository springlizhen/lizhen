package com.chinags.device.check.entity;

import com.chinags.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="T_COA_CHECK_NOTE")
public class CheckNote extends BaseEntity {
    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    private String id;
    @Transient
    private String kb;
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
    private String checkPerson;
    private String checkResultDescription;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date checkDate;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date partoal;  //更新时间
    private String itemUpload;


}
