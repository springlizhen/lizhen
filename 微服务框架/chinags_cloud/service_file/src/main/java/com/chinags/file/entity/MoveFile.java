package com.chinags.file.entity;

import java.util.List;
import java.util.Map;

public class MoveFile {
    private String userName;
    private String userCode;
    private String pid;
    private String workId;
    private List<Map<String, Object>> fjList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public List<Map<String, Object>> getFjList() {
        return fjList;
    }

    public void setFjList(List<Map<String, Object>> fjList) {
        this.fjList = fjList;
    }
}
