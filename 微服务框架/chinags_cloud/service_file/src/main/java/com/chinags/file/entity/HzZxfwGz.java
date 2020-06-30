package com.chinags.file.entity;

import lombok.Data;

/**
 * 中心发文稿纸
 * @Author : Mark_Wang
 * @Date : 2019/12/24
 **/
@Data
public class HzZxfwGz {
    /**
     * 慧正id
     */
    private String id;
    /**
     *  签发
     */
    private String qf;
    /**
     *  审阅
     */
    private String sy;
    /**
     * 会签
     */
    private String hq;
    /**
     *  办公室处理意见
     */
    private String bgsyj;
    /**
     *  日期
     */
    private String day;
    /**
     *  拟稿人
     */
    private String ngr;
    /**
     * 拟稿人处室
     */
    private String ngrcs;
    /**
     *  拟稿人电话
     */
    private String ngrPhone;
    /**
     *  核稿人
     */
    private String hgr;
    /**
     *  核稿人电话
     */
    private String hgrPhone;
    /**
     *  公文标题
     */
    private String title;
    /**
     *  主送单位
     */
    private String zsCompany;
    /**
     *  抄送单位
     */
    private String csCompany;
    /**
     *  印发机关
     */
    private String yfjg;
    /**
     *  打印份数
     */
    private String number;
    /**
     *  校对人
     */
    private String userName;
    /**
     *  发文号
     */
    private String fileNumber;
    /**
     * 发文机关（0：中心发文）
     */
    private String type;
    /**
     * 分中心
     */
    private String fzx;

}
