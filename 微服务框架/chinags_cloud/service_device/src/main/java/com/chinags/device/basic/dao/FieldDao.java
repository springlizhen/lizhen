package com.chinags.device.basic.dao;

import com.chinags.device.basic.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FieldDao extends JpaRepository<Field, String>, JpaSpecificationExecutor<Field> {
    public Field getFieldById(String id);

    public Field getFieldByFieldCode(String fieldCode);

    public List<Field> getFieldsByIdIn(List<String> ids);

    public List<Field> getFieldsByFieldGroupIsNull();
    @Query(value = "SELECT * FROM T_ENM_FIELD WHERE FIELD_GROUP=:deviceClass", nativeQuery = true)
    public  List<Field> selectByDeviceClass(String deviceClass);
}
