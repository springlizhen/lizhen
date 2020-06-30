package com.chinags.dbra.dao;

import com.chinags.dbra.entity.ServiceApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/22
 **/

public interface ServiceApplicantDao  extends JpaRepository<ServiceApplicant, String>, JpaSpecificationExecutor<ServiceApplicant> {

    @Query(value = "SELECT * FROM T_META_SERVICE_APPLICANT WHERE CREATE_BY=:createBy AND RESOURCE_ID=:resourceId ORDER BY CREATE_DATE", nativeQuery = true)
    ServiceApplicant findByCreateByAndResourceId(String createBy, String resourceId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_META_SERVICE_APPLICANT SET STATUS=:status, UPDATE_BY=:updateBy, UPDATE_DATE=:updateDate  WHERE ID=:id", nativeQuery = true)
    void updateStatusById(String id, String status, String updateBy, Date updateDate);

    @Query(value = "SELECT * FROM T_META_SERVICE_APPLICANT WHERE ID=:id", nativeQuery = true)
    ServiceApplicant getById(String id);

    @Query(value = "SELECT * FROM T_META_SERVICE_APPLICANT WHERE create_by=:createBy ORDER BY CREATE_DATE", nativeQuery = true)
    List<ServiceApplicant> findByUser(String createBy);

    @Query(value = "SELECT * FROM T_META_SERVICE_APPLICANT WHERE create_by=:createBy AND TYPE=:type AND STATUS='已审核'", nativeQuery = true)
    List<ServiceApplicant> findByUserAndType(String createBy, String type);

}
