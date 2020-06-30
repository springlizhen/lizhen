package com.chinags.device.safe.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 安全工程档案
 * @author XieWenqing
 * @date 2019/12/5 上午 10:29
 */
@Data
@Entity
@Table(name="T_COA_SAFE_PRO_DOC")
public class SafeProDoc {
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
    private String name;  //名称

    private String createBy;  //创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;  //创建时间
    private String updateBy;  //更新人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateDate;  //更新时间
    private String remarks;  //备注
}
