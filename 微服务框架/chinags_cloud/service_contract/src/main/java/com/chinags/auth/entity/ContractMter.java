package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
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
public class ContractMter extends BaseEntity {
    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    @Column(name = "id", nullable = false, length = 64)
    private String id;
    private String contractid;
    private String contractname;
    private String customername;
    private String contractcode;
    private String contractamount;
    private String payamount;
    private String inputamount;
    private String payamountname;
    private String payterm;
    private String uploadfile;
    private String engincheck;
    private String officecheck;
    private String financecheck;
    private String leadercheck;
    private String leaderissue;
    private String moneytype;
    private String moneyclass;
    private Integer payaccount;
    private Integer advancecharge;
    private Integer invoiceamount;
    private Integer amountpay;
    private Integer retentmoney;
    private String dfff;
    private String workId;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractid() {
        return contractid;
    }

    public void setContractid(String contractid) {
        this.contractid = contractid;
    }

    public String getContractname() {
        return contractname;
    }

    public void setContractname(String contractname) {
        this.contractname = contractname;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getContractcode() {
        return contractcode;
    }

    public void setContractcode(String contractcode) {
        this.contractcode = contractcode;
    }

    public String getContractamount() {
        return contractamount;
    }

    public void setContractamount(String contractamount) {
        this.contractamount = contractamount;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getInputamount() {
        return inputamount;
    }

    public void setInputamount(String inputamount) {
        this.inputamount = inputamount;
    }

    public String getPayamountname() {
        return payamountname;
    }

    public void setPayamountname(String payamountname) {
        this.payamountname = payamountname;
    }

    public String getPayterm() {
        return payterm;
    }

    public void setPayterm(String payterm) {
        this.payterm = payterm;
    }

    public String getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(String uploadfile) {
        this.uploadfile = uploadfile;
    }

    public String getEngincheck() {
        return engincheck;
    }

    public void setEngincheck(String engincheck) {
        this.engincheck = engincheck;
    }

    public String getOfficecheck() {
        return officecheck;
    }

    public void setOfficecheck(String officecheck) {
        this.officecheck = officecheck;
    }

    public String getFinancecheck() {
        return financecheck;
    }

    public void setFinancecheck(String financecheck) {
        this.financecheck = financecheck;
    }

    public String getLeadercheck() {
        return leadercheck;
    }

    public void setLeadercheck(String leadercheck) {
        this.leadercheck = leadercheck;
    }

    public String getLeaderissue() {
        return leaderissue;
    }

    public void setLeaderissue(String leaderissue) {
        this.leaderissue = leaderissue;
    }

    public String getMoneytype() {
        return moneytype;
    }

    public void setMoneytype(String moneytype) {
        this.moneytype = moneytype;
    }

    public String getMoneyclass() {
        return moneyclass;
    }

    public void setMoneyclass(String moneyclass) {
        this.moneyclass = moneyclass;
    }

    public Integer getPayaccount() {
        return payaccount;
    }

    public void setPayaccount(Integer payaccount) {
        this.payaccount = payaccount;
    }

    public Integer getAdvancecharge() {
        return advancecharge;
    }

    public void setAdvancecharge(Integer advancecharge) {
        this.advancecharge = advancecharge;
    }

    public Integer getInvoiceamount() {
        return invoiceamount;
    }

    public void setInvoiceamount(Integer invoiceamount) {
        this.invoiceamount = invoiceamount;
    }

    public Integer getAmountpay() {
        return amountpay;
    }

    public void setAmountpay(Integer amountpay) {
        this.amountpay = amountpay;
    }

    public Integer getRetentmoney() {
        return retentmoney;
    }

    public void setRetentmoney(Integer retentmoney) {
        this.retentmoney = retentmoney;
    }

    public String getDfff() {
        return dfff;
    }

    public void setDfff(String dfff) {
        this.dfff = dfff;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
