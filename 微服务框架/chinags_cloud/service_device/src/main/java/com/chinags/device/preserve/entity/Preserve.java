package com.chinags.device.preserve.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 维护记录entity
 * @author lizhen
 * @date 2020/3/3
 */
@Data
@Entity
@Table(name="T_COA_PRESERVE")
public class Preserve {
    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;


    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    private String id;
    private String createBy;  //维护人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;  //维护时间
    private String remark;  //备注
    private String content;  //维护内容
    private String maintainId;  //养护记录id
    private String countNum;





}
