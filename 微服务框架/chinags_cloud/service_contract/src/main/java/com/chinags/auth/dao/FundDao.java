package com.chinags.auth.dao;

import com.chinags.auth.entity.File;
import com.chinags.auth.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface FundDao extends JpaRepository<Fund, String>, JpaSpecificationExecutor<Fund> {
    @Query(value = "select * from t_coa_fund where id=:id", nativeQuery = true)
    public Fund selectFundById(String id);
    @Modifying
    @Transactional
    @Query(value = "delete from t_coa_fund where uuid=:uuid", nativeQuery = true)
    public void deleteFundById(String uuid);

}
