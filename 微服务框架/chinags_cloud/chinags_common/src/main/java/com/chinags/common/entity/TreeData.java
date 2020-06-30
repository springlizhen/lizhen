package com.chinags.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TreeData {

    private String id;
    private String pId;
    private String name;
    private String title;
    private String titleStatus;
    private boolean titleST;

    public TreeData(String id, String pId, String name, String title, String titleStatus){
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.title = title;
        this.titleStatus = titleStatus;
        this.titleST = titleStatus.equals("0");
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
