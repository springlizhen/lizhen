package com.chinags.file.entity;

import lombok.Data;

/**
 * 收文稿纸
 * @Author : Mark_Wang
 * @Date : 2019/12/25
 **/
@Data
public class HzDwSwGz {
    /**
     * 慧正id
     */
    private String id;
    /**
     * 收文号
     */
    private String sw;
    /**
     * 密级
     */
    //private String mj;
    /**
     * 日期
     */
    private String day;
    /**
     * 来文机关
     */
    private String lwjg;
    /**
     * 文件标题
     */
    private String wjbt;
    /**
     * 拟办意见
     */
    private String nbyj;
    /**
     * 领导批示
     */
    private String ldps;
    /**
     * 承办处室意见
     */
    private String cbcsyj;
    /**
     * 时效
     */
    private String sx;
    /**
     * 办结意见
     */
    private String bjyj;
    /**
     * 备注
     */
    private String bz;
    /**
     * 协办处室意见
     */
    private String xbcsyj;

}
