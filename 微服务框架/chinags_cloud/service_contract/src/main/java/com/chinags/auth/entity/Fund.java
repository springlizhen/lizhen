package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "T_COA_FUND")
public class Fund extends BaseEntity{
    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    @Column(name = "uuid", nullable = false, length = 64)
    private String uuid;
    private String id;
    //@Column(name = "CONTRACT_TITLE")
    private String contractType;
    private String title;
    private String fund;
    private String fundType;
    private String fundMoney;
    private String remitType;
    private String remitAccount;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date remitTime;
    private String contractCode;



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundMoney() {
        return fundMoney;
    }

    public void setFundMoney(String fundMoney) {
        this.fundMoney = fundMoney;
    }

    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    public String getRemitAccount() {
        return remitAccount;
    }

    public void setRemitAccount(String remitAccount) {
        this.remitAccount = remitAccount;
    }

    public Date getRemitTime() {
        return remitTime;
    }

    public void setRemitTime(Date remitTime) {
        this.remitTime = remitTime;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
}
