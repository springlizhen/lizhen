package com.chinags.file.entity;

import lombok.Data;

/**
 * 慧正中心收文稿纸
 * @Author : Mark_Wang
 * @Date : 2019/12/29
 **/
@Data
public class HzZxSwGz {
    /**
     * 慧正id
     */
    private String id;
    /**
     * 收文日期
     */
    private String swrq;
    /**
     * 来文单位
     */
    private String lwdw;
    /**
     * 来文号
     */
    private String lwh;
    /**
     * 收文类别
     */
    private String swlb;
    /**
     * 收文号
     */
    private String swh;
    /**
     * 办理期限
     */
    private String blqx;
    /**
     * 生效日期
     */
    private String sxrq;
    /**
     * 密级
     */
    private String mj;
    /**
     * 缓急
     */
    private String hj;
    /**
     * 标题
     */
    private String bt;
    /**
     * 拟办意见
     */
    private String nbyj;
    /**
     * 领导批示
     */
    private String ldps;
    /**
     * 主办处室办理意见
     */
    private String zbcsblyj;
    /**
     * 主办处室办理结果
     */
    private String zbcsbljg;
    /**
     * 协办处室办理意见
     */
    private String xbcsblyj;
    /**
     * 协办处室办理结果
     */
    private String xbcsbljg;
    /**
     * 备注
     */
    private String bz;
    /**
     * 分中心
     */
    private String fzx;
}
