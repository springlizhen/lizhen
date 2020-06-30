package com.chinags.device.plan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableData{

    private String sort;
    private String name;
    /**
     * 单位
     */
    private String projectUnit;
    /**
     * 数量
     */
    private String projectQuantity;
    /**
     * 单价
     */
    private String projectPrice;
    /**
     * 合价
     */
    private String money;
    private String remarks;
    private Boolean merge;
    private String mergeName;

    public TableData(String sort, String name, String money, String remarks, String mergeName){
        this.sort = sort;
        this.name = name;
        this.money = money;
        this.remarks = remarks;
        this.mergeName = mergeName;
        this.merge = true;
    }

    public TableData(String sort, String name, String projectUnit, String projectQuantity, String projectPrice, String money){
        this.sort = sort;
        this.name = name;
        this.projectUnit = projectUnit;
        this.projectQuantity = projectQuantity;
        this.projectPrice = projectPrice;
        this.money = money;
        this.remarks = remarks;
        this.merge = false;
    }
}
