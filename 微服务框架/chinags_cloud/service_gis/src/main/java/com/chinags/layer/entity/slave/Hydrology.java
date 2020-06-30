package com.chinags.layer.entity.slave;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Administrator
 */
@Data
@Entity
@Table(name = "MSG_POINT_VALUE")
public class Hydrology {



    @Id
    private String id;
    @Transient
    private String qn;

    private String mn;

    private String code;

    private String type;

    private String flag;

    private String value;


}
