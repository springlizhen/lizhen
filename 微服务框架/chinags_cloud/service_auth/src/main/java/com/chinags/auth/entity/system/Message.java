package com.chinags.auth.entity.system;

import lombok.Data;

import java.util.Date;

@Data
public class Message {

    /**
     * 通知标题
     */
    private String title;
    /**
     * 通知内容
     */
    private String content;
    /**
     * 即授人id，多个用逗号（，）隔开
     */
    private String receives;
    /**
     * 消息类型，0：普通 1：预警 2：调度指令
     */
    private Integer type;
    /**
     * 发送时间sendDate
     */
    private Date senddate;
    /**
     * msgid
     */
    private String msgid;
}
