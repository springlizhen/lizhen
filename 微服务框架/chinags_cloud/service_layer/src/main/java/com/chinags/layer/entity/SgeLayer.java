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
@Table(name = "t_seg_layer")
public class SgeLayer extends BaseEntity {

    @Id
    @GenericGenerator(name = "sge-uuid", strategy = "uuid")
    @GeneratedValue(generator = "sge-uuid")
    private String id;
    /**
     * 图层名称
     */
    private String name;
    /**
     * 图层类型
     */
    private String type;
    /**
     * 图层服务地址
     */
    private String url;
    /**
     * 图层比例尺
     */
    private String scale;
    /**
     * 发布状态
     */
    private String useStatus;
}
