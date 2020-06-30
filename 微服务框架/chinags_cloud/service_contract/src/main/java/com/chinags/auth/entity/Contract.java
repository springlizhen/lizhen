package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.TreeEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@DynamicUpdate(value = true)
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "T_COA_CONTRACT")
public class Contract extends BaseEntity {
    private String contractCode;
    private String itemUpload;
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    private String contractType;
    private String trackId;
    private String workId;
    private String subjection;
    private String contractname;

    // @OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    //@JoinTable(name="T_COA_FUND",
    // inverseJoinColumns = {
    //     @JoinColumn(name = "TITLE",referencedColumnName="CONTRACT_TITLE",insertable = true,updatable = true)
    // })
    //@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    // @JoinColumn(name="TITLE")
    private String title;
    private String text;
    private String contractObject;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String follow;
    private String customer;
    private String remarks;
    private String status;
    private Double money;
    private String relatedItemCode;
    private String relatedItemName;
    private String owner;
    private String ownerContact;
    private String ownerAddress;
    private String ownerPhone;
    private String ownerSign;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownerSigntime;
    private String otherOwner;
    private String otherOwnerContact;
    private String otherOwnerAddress;
    private String otherOwnerPhone;
    private String otherOwnerSign;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date otherOwnerSigntime;

    private String Time;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date remitTime;
    private String subContract;
    private String fileName;
    private String fileAddress;

    public String getSubContract() {
        return subContract;
    }

    public void setSubContract(String subContract) {
        this.subContract = subContract;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerSign() {
        return ownerSign;
    }

    public void setOwnerSign(String ownerSign) {
        this.ownerSign = ownerSign;
    }
    public String getItemUpload() {
        return itemUpload;
    }

    public void setItemUpload(String itemUpload) {
        this.itemUpload = itemUpload;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getOtherOwnerContact() {
        return otherOwnerContact;
    }

    public void setOtherOwnerContact(String otherOwnerContact) {
        this.otherOwnerContact = otherOwnerContact;
    }



    public Date getRemitTime() {
        return remitTime;
    }

    public void setRemitTime(Date remitTime) {
        this.remitTime = remitTime;
    }

    public Date getOwnerSigntime() {
        return ownerSigntime;
    }

    public void setOwnerSigntime(Date ownerSigntime) {
        this.ownerSigntime = ownerSigntime;
    }

    public String getOtherOwner() {
        return otherOwner;
    }

    public void setOtherOwner(String otherOwner) {
        this.otherOwner = otherOwner;
    }

    public String getOtherOwnerAddress() {
        return otherOwnerAddress;
    }

    public void setOtherOwnerAddress(String otherOwnerAddress) {
        this.otherOwnerAddress = otherOwnerAddress;
    }

    public String getOtherOwnerPhone() {
        return otherOwnerPhone;
    }

    public void setOtherOwnerPhone(String otherOwnerPhone) {
        this.otherOwnerPhone = otherOwnerPhone;
    }

    public String getOtherOwnerSign() {
        return otherOwnerSign;
    }

    public void setOtherOwnerSign(String otherOwnerSign) {
        this.otherOwnerSign = otherOwnerSign;
    }

    public Date getOtherOwnerSigntime() {
        return otherOwnerSigntime;
    }

    public void setOtherOwnerSigntime(Date otherOwnerSigntime) {
        this.otherOwnerSigntime = otherOwnerSigntime;
    }
    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }




    public String getSubjection() {
        return subjection;
    }

    public void setSubjection(String subjection) {
        this.subjection = subjection;
    }


    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractObject() {
        return contractObject;
    }

    public void setContractObject(String contractObject) {
        this.contractObject = contractObject;
    }

    public String getRelatedItemCode() {
        return relatedItemCode;
    }

    public void setRelatedItemCode(String relatedItemCode) {
        this.relatedItemCode = relatedItemCode;
    }

    public String getRelatedItemName() {
        return relatedItemName;
    }

    public void setRelatedItemName(String relatedItemName) {
        this.relatedItemName = relatedItemName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
