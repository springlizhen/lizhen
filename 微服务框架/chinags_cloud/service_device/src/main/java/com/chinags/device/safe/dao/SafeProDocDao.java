package com.chinags.device.safe.dao;

import com.chinags.device.safe.entity.SafeProDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author XieWenqing
 * @date 2019/12/5 上午 10:33
 */
public interface SafeProDocDao extends JpaRepository<SafeProDoc, String>, JpaSpecificationExecutor<SafeProDoc> {
    /**
     * 根据id查询安全工程档案
     * @param id  安全工程档案id
     */
    @Query(value = "SELECT * FROM T_COA_SAFE_PRO_DOC WHERE ID=:id", nativeQuery = true)
    SafeProDoc selectById(String id);

    /**
     * 根据id删除安全工程档案
     * @param id 安全工程档案id
     */
    @Override
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM T_COA_SAFE_PRO_DOC WHERE id=:id", nativeQuery = true)
    void deleteById(String id);

    /**
     * 根据id查询是否有该条记录，0否1是
     */
    @Query(value = "SELECT count(*) FROM T_COA_SAFE_PRO_DOC WHERE ID=:id", nativeQuery = true)
    Integer count (String id);

}
