package com.chinags.dbra.dao;

import com.chinags.dbra.entity.ApiParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-8-27
 **/
public interface ApiParamDao extends JpaRepository<ApiParam, String>, JpaSpecificationExecutor<ApiParam> {

    @Query(value = "SELECT * FROM t_meta_api_param WHERE TYPE=:type AND API_ID=:apiId", nativeQuery = true)
    List<ApiParam> findByTypeAndApiId(String type, String apiId);

    @Query(value = "SELECT * FROM t_meta_api_param WHERE API_ID=:apiId", nativeQuery = true)
    List<ApiParam> findByApiId(@Param("apiId") String apiId);

    @Query(value = "SELECT * FROM t_meta_api_param WHERE ID=:id", nativeQuery = true)
    ApiParam selectById(String id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM T_META_API_PARAM WHERE API_ID=:apiId", nativeQuery = true)
    void deleteByApiId(String apiId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_META_API_PARAM SET API_ID=:apiId WHERE API_ID=:userCode", nativeQuery = true)
    void updateApiIdByUserCode(String apiId, String userCode);

}
