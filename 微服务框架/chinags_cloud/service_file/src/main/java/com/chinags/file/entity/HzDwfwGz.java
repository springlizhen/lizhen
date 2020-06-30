package com.chinags.file.entity;

import lombok.Data;

/**
 * 党委发文稿纸
 * @Author : Mark_Wang
 * @Date : 2019/12/25
 **/
@Data
public class HzDwfwGz {
    /**
     * 慧正id
     */
    private String id;
    /**
     * 密级
     */
    private String mj;
    /**
     * 签发
     */
    private String qf;
    /**
     * 审签
     */
    private String sq;
    /**
     * 会签
     */
    private String hq;
    /**
     * 处理意见
     */
    private String clyj;
    /**
     * 拟稿人
     */
    private String ngr;
    /**
     * 拟稿人处室
     */
    private String ngrcs;
    /**
     * 拟稿人电话
     */
    private String ngrPhone;
    /**
     * 拟办单位核稿人
     */
    private String hgr;
    /**
     * 本文标题
     */
    private String bwbt;
    /**
     * 发送单位
     */
    private String fsdw;
    /**
     * 抄报单位
     */
    private String cbdw;
    /**
     * 抄送单位
     */
    private String csdw;
    /**
     * 打印份数
     */
    private String number;
    /**
     * 打字
     */
    private String dz;
    /**
     * 校对人
     */
    private String userName;
    /**
     * 发文号
     */
    private String fw;
    /**
     * 日期
     */
    private String day;
}
