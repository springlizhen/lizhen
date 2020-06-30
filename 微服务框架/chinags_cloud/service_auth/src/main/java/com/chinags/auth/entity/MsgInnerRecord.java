package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.Date;

/**
 * 内部消息发送记录表Entity
 * @author ThinkGem
 * @version 2019-03-12
 */
@Data
@Entity
@Table(name = "T_COA_SYS_MSG_INNER_RECORD")
public class MsgInnerRecord{

	// 读取状态（0未送达 1已读 2未读）
	public static final String READ_STATUS_READ = "1";
	public static final String READ_STATUS_UNREAD = "2";
	
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "database-uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@GeneratedValue(generator = "database-uuid")
	@Column(name = "id", nullable = false, length = 19)
	private String id;
	private String msgInnerId;			// 所属消息
	private String receiveUserCode;		// 接受者用户编码
	private String receiveUserName;		// 接受者用户姓名
	private String readStatus;			// 读取状态（0未送达 1已读 2未读）
	private Date readDate;				// 阅读时间
	private String isStar;				// 是否标星

	@Transient
	private String orderBy;
	@Transient
	private Integer pageNo;
	@Transient
	private Integer pageSize;
	@Transient
	private String desc;

	public MsgInnerRecord() {
		this(null);
	}

	public MsgInnerRecord(String id){
		this.id = id;
	}

	public String getOrderBy() {
		if(orderBy==null) {
			return null;
		}
		String orderBy = this.orderBy.split("%20")[0];
		return orderBy;
	}

	public Sort.Direction getDesc() {
		if(orderBy==null) {
			return Sort.Direction.ASC;
		}
		String desc = this.orderBy.split("%20")[1];
		return "desc".equals(desc)?Sort.Direction.DESC:Sort.Direction.ASC;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getPageNo() {
		return pageNo==null?0:pageNo-1;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize==null?20:pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}