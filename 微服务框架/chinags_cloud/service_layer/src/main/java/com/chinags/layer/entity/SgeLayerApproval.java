package com.chinags.layer.entity;

import com.chinags.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_seg_layer_approval")
public class SgeLayerApproval extends BaseEntity {

    @Id
    @GenericGenerator(name = "sge-uuid", strategy = "uuid")
    @GeneratedValue(generator = "sge-uuid")
    private String id;
    /**
     * 图层名称
     */
    private String layerName;
    /**
     * 图层id
     */
    private String layerId;
    /**
     * 审核状态
     */
    private String useStatus;
    /**
     * 申请人
     */
    private String pepName;
}
