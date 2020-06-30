package com.chinags.layer.dao;

import com.chinags.layer.entity.SgeLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SgeLayerDao extends JpaRepository<SgeLayer, String>, JpaSpecificationExecutor<SgeLayer> {
    public SgeLayer getSgeLayerById(String id);

    @Query(value = "select name from SgeLayer where id=:id")
    public String getName(String id);
}
