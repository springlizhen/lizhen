package com.chinags.file.entity;

import lombok.Data;

/**
 * 慧正工作流上行文下行文实体类
 * @Author : Mark_Wang
 * @Date : 2019/12/24
 **/
@Data
public class HzTopDown {
    /**
     * 慧正流程id
     */
    private String id;
    /**
     * 发文类型（0：下行文  1： 上行文  2：平行文  3:党委）
     */
    private int type;
    /**
     * 行文标题
     */
    private String title;
    /**
     * 行文内容
     */
    private String content;
    /**
     * 文件标号
     */
    private String fileNumber;
    /**
     * 签发人
     */
    private String userName;
    /**
     * 日期
     */
    private String day;
    /**
     * 发送单位
     */
    private String fsdw;
    /**
     * 抄送单位
     */
    private String csdw;
    /**
     * 联系人
     */
    private String lxr;
    /**
     * 联系电话
     */
    private String lxdh;
    /**
     * 流程类型
     */
    private String flowtype;
    /**
     * 结尾距离
     */
    private int endSize;

}
