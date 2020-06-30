package com.chinags.device.maintain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 养护记录entity
 * @author XieWenqing
 * @date 2019/12/6 下午 4:38
 */
@Data
@Entity
@Table(name="T_COA_MAINTAIN_INFO")
public class MaintainInfo {
    @Transient
    private String orderBy;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;
    @Transient
    private String orgId;  //所属机构id


    @Id
    @GenericGenerator(name = "column-uuid", strategy = "uuid")
    @GeneratedValue(generator = "column-uuid")
    private String id;
    private String createBy;  //创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;  //创建时间
    private String updateBy;  //更新人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;  //更新时间
    private String remarks;  //备注

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;  //维护完成时间
    private String responsiblePerson;  //维护责任人
    private String workCompany;  //施工单位
    private String deceiveId;  //设备id
    private String deceiveName;  //设备名称
    private Integer countNum;// 维护次数
    private String people;//维护人
    private String content;//维护内容
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date peopleTime;  //维护时间
    private String remark;  //备注
    private Integer count;
}
