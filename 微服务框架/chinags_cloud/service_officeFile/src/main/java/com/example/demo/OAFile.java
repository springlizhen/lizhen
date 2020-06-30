package com.example.demo;

import com.chinags.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 测试数据Entity
 * @author ThinkGem
 * @version 2018-04-22
 */
@Data
@Entity
@Table(name = "ta_horizon_info")
public class OAFile{

    @Id
    private String id;
    private String fileName;
    private String saveName;
    private String extention;
    private String fileSize;
    private String orderNo;
    private String objectId;
    private String active;
    private String saveType;
    private String saveTime;
    private String savePath;
    private String userId;
    private String tableName;
    private String columnName;
    private String fileContentId;
    private String comments;
    private String viewFileName;
    private String fieldName;
    private String status;
}
