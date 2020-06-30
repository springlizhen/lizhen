package com.chinags.device.check.entity;

import com.chinags.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="T_COA_DOCUMENT")
public class Document extends BaseEntity {
    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    private String id;
    /**
     * 设备代码
     */
    private String documentId;
    /**
     * 设备名称
     */
    private String documentName;
    /**
     * 简称
     */
    private String releaseDepartment;


    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date releaseDate;

    private Date createDate;


    /**
     * 所属类别名称
     */
    private String introduction;
    /**
     * 所属设备
     */
    private String itemUpload;



}
