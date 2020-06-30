package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "T_COA_FILE")
public class File extends BaseEntity{
    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    private String id;
    private String fileName;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date uploadTime;
    private String fileBasis;
    private String fileExplain;
    private String fileAddress;
    private String itemUpload;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getFileBasis() {
        return fileBasis;
    }

    public void setFileBasis(String fileBasis) {
        this.fileBasis = fileBasis;
    }

    public String getFileExplain() {
        return fileExplain;
    }

    public void setFileExplain(String fileExplain) {
        this.fileExplain = fileExplain;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }
    public String getItemUpload() {
        return itemUpload;
    }

    public void setItemUpload(String itemUpload) {
        this.itemUpload = itemUpload;
    }
}
