package com.chinags.auth.dao;

import com.chinags.auth.entity.DicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DicTypeDao extends JpaRepository<DicType, String>, JpaSpecificationExecutor<DicType> {

    public DicType getById(String id);

    public DicType getByDictType(String dictype);
}
