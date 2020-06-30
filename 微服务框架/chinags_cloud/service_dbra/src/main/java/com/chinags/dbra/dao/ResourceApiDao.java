package com.chinags.dbra.dao;

import com.chinags.dbra.entity.ResourceApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * 资源共享表接口
 * @author Mark_Wang
 * @date 2019/7/1
 **/
public interface ResourceApiDao extends JpaRepository<ResourceApi, String>, JpaSpecificationExecutor<ResourceApi> {
    @Query(value = "select * from t_meta_resource_api where re_id=:reId", nativeQuery = true)
    ResourceApi findByReId(@Param("reId") String reId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_meta_resource_api SET API_HOST=:apiHost , API_COUNT_HOST=:apiCountHost  WHERE ID=:id", nativeQuery = true)
    void updateHostById(String apiHost, String apiCountHost, String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_meta_resource_api SET CALL_NUM=CALL_NUM+1 WHERE ID=:id", nativeQuery = true)
    void updateCallNumById(String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_meta_resource_api SET CONN_NUM=CONN_NUM+1 WHERE RE_ID=:reId", nativeQuery = true)
    void updateConnNumByReId(String reId);

}
