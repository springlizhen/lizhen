package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "T_COA_ITEM")
public class Item extends BaseEntity {
    @Id
    private String id;
    private String itemCode;
    private String trackId;
    private String workId;
    private String subjection;
    private String title;
    private String itemLeader;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endDate;
    private String approver;
    private String subApprover;

    public String getSubApprover() {
        return subApprover;
    }

    public void setSubApprover(String subApprover) {
        this.subApprover = subApprover;
    }

    private String approverRole;
    private String approvalResult;
    private String status;
    private Double money;
    //private String createBy;
    private String remarks;
    private String itemUpload;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date selectMinStartDate;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date selectMaxStartDate;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date selectMinEndDate;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date selectMaxEndDate;
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    private String limit;
    public Date getSelectMinStartDate() {
        return selectMinStartDate;
    }

    public void setSelectMinStartDate(Date selectMinStartDate) {
        this.selectMinStartDate = selectMinStartDate;
    }

    public Date getSelectMaxStartDate() {
        return selectMaxStartDate;
    }

    public void setSelectMaxStartDate(Date selectMaxStartDate) {
        this.selectMaxStartDate = selectMaxStartDate;
    }

    public Date getSelectMinEndDate() {
        return selectMinEndDate;
    }

    public void setSelectMinEndDate(Date selectMinEndDate) {
        this.selectMinEndDate = selectMinEndDate;
    }

    public Date getSelectMaxEndDate() {
        return selectMaxEndDate;
    }

    public void setSelectMaxEndDate(Date selectMaxEndDate) {
        this.selectMaxEndDate = selectMaxEndDate;
    }

    public String getItemUpload() {
        return itemUpload;
    }

    public void setItemUpload(String itemUpload) {
        this.itemUpload = itemUpload;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApproverRole() {
        return approverRole;
    }

    public void setApproverRole(String approverRole) {
        this.approverRole = approverRole;
    }

    public Date getStartDate() {
        return startDate;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getItemLeader() {
        return itemLeader;
    }

    public void setItemLeader(String itemLeader) {
        this.itemLeader = itemLeader;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }



    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
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

    /*public String getCreateBy() {
        return createBy;
    }*/

    /*public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    */


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
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
