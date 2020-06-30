package com.chinags.device.basic.dao;

import com.chinags.device.basic.entity.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface SensorTypeDao extends JpaRepository<SensorType, String>, JpaSpecificationExecutor<SensorType> {
    SensorType getById(String id);
    SensorType getByCode(String code);

    @Query(value = "select count(t) from SensorType t where parent_codes LIKE CONCAT('%',:parentCodes,'%') and status=0")
    public Integer getStopParentCodesCount(@Param("parentCodes") String parentCodes);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SensorType SET treeLeaf='1' WHERE id=:id")
    void updateApplyNumById(String id);
}
