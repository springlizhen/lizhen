package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 内部消息Entity
 * @author ThinkGem
 * @version 2019-03-12
 */
@Data
@Entity
@Table(name = "T_COA_SYS_MSG_INNER")
public class MsgInner extends BaseEntity {

	// 接受者类型（0所有 1用户 2部门 3角色 4岗位）
	public static final String RECEIVE_TYPE_ALL = "0";
	public static final String RECEIVE_TYPE_USER = "1";
	public static final String RECEIVE_TYPE_OFFICE = "2";
	public static final String RECEIVE_TYPE_ROLE = "3";
	public static final String RECEIVE_TYPE_POST = "4";
	
	// 内容级别（1普通 2一般 3紧急）
	public static final String CONTENT_LEVEL_1 = "1";
	public static final String CONTENT_LEVEL_2 = "2";
	public static final String CONTENT_LEVEL_3 = "3";
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false, length = 19)
	private String id;
	private String msgTitle;		// 消息标题
	private String contentLevel;	// 内容等级（1普通 2一般 3紧急）
	private String contentType;		// 内容类型（1公告 2新闻 3会议 4其它）
	private String msgContent;		// 消息内容
	private String receiveType;		// 接受者类型（1用户 2部门 3角色 4岗位）
	private String receiveCodes;	// 接受者字符串
	private String receiveNames;	// 接受者名称字符串
	private String sendUserCode;	// 发送者用户编码
	private String sendUserName;	// 发送者用户姓名
	private Date sendDate;			// 发送时间
	private String isAttac;			// 是否有附件
	private String notifyTypes;		// 通知类型（PC APP 短信 邮件 微信）多选
	
	public MsgInner() {
		this(null);
	}

	public MsgInner(String id) {
		this.id = id;
	}
	
}