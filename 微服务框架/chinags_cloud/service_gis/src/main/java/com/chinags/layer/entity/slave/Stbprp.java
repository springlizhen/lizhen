package com.chinags.layer.entity.slave;

import com.chinags.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@Entity
@Table(name = "DWD_B_HYD_STBPRP_PP")
public class Stbprp  {

    @Id
    private Integer id;
    @Transient
    private String yearId;

    private String stcd;

    private String stnm;

    private String rvcd;

    private String rvnm;

    private String hncd;

    private String bscd;
    private String areaId;

    private String provId;

    private String cityId;

    private String stlc;



    private String dtmnm;


    private String sttp;
    private AliasToEntityMapResultTransformer resultTransformer;


    public void setResultTransformer(AliasToEntityMapResultTransformer resultTransformer) {
        this.resultTransformer = resultTransformer;
    }
}
