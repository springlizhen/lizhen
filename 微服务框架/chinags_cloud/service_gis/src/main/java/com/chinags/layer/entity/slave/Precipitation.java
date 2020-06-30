package com.chinags.layer.entity.slave;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "DWD_D_HYD_PPTN_R")
public class Precipitation {

    @Id
    private String id;

    private String yearid;

    private String dayid;

    private String stcd;

    private Date tm;

    private String drp;

    private String intv;

    private String pdr;

    private String wth;

    private String ifstorm;

    private String stnm;
}
