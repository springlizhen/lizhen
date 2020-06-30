package com.chinags.auth.dao;

import com.chinags.auth.entity.DicData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DicDataDao extends JpaRepository<DicData, String>, JpaSpecificationExecutor<DicData> {
    DicData getById(String id);
    DicData getByDictCode(String dictCode);
    List<DicData> getAllByDictTypeOrderByTreeSort(String dictType);
}
