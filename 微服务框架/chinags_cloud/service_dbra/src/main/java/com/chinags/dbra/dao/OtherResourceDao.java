package com.chinags.dbra.dao;

import com.chinags.dbra.entity.OtherResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/17
 **/
public interface OtherResourceDao extends JpaRepository<OtherResource, String>, JpaSpecificationExecutor<OtherResource> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_META_OTHER_RESOURCE SET STATUS=:status  WHERE ID=:theId", nativeQuery = true)
    void updateStatusById(String status, String theId);

    @Query(value = "SELECT DISTINCT STATUS FROM T_META_OTHER_RESOURCE", nativeQuery = true)
    List<String> getStatus();

    @Query(value = "SELECT DISTINCT SERVICE_CLASS FROM T_META_OTHER_RESOURCE", nativeQuery = true)
    List<String> getServiceClasses();

    @Query(value = "SELECT DISTINCT ACCESS_TYPE FROM T_META_OTHER_RESOURCE", nativeQuery = true)
    List<String> getAccessTypes();

    @Query(value = "SELECT * FROM T_META_OTHER_RESOURCE WHERE ID=:id", nativeQuery = true)
    OtherResource getById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_META_OTHER_RESOURCE SET CALL_NUM=CALL_NUM+1 WHERE ID=:id", nativeQuery = true)
    void addCallNum(String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_META_OTHER_RESOURCE SET CONN_NUM=CONN_NUM+1 WHERE ID=:id", nativeQuery = true)
    void addConnNum(String id);
}
