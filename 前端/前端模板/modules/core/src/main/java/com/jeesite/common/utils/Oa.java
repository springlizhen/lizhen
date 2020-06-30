package com.jeesite.common.utils;

import com.jeesite.modules.sys.utils.OALoginUtiles;

/**
 * 模拟实体类，无数据库表格
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
public class Oa {

    private String id;
    private String flowId;
    private String flowName;
    private String title;
    private String sendUserName;
    private String sendTime;
    private String startTime;
    private String sendSubjectionName;
    private String trackId;
    private String workId;
    private String href;
    private String subjectionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendSubjectionName() {
        return sendSubjectionName;
    }

    public void setSendSubjectionName(String sendSubjectionName) {
        this.sendSubjectionName = sendSubjectionName;
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

    public String getHref() {
        return href==null? OALoginUtiles.oaHZhttp+"workflow/module/workflow/index.html?workId="+(getWorkId()==null||getWorkId().equals("")?getId():(getWorkId()+"&trackId="+getTrackId()+"&subjection="+getSubjectionId())):href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSubjectionId() {
        return subjectionId;
    }

    public void setSubjectionId(String subjectionId) {
        this.subjectionId = subjectionId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
