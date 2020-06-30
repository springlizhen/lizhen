package com.chinags.file.dao;

import com.chinags.file.entity.HzQz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 慧正签章
 * @Author : Mark_wang
 * @Date : 2020-2-17
 **/
public interface HzQzDao extends JpaRepository<HzQz, String>, JpaSpecificationExecutor<HzQz> {

    @Query(value = "SELECT * FROM T_PUB_HZQZ WHERE HZ_ID=:hzId", nativeQuery = true)
    HzQz selectByHzId(String hzId);

}
