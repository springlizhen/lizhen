package com.chinags.device.plan.dao;

import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.entity.SubCentersPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface SubCentersPlanDao extends JpaRepository<SubCentersPlan, String>, JpaSpecificationExecutor<SubCentersPlan> {
    public SubCentersPlan getById(String id);

    @Modifying
    @Transactional
    @Query(value = "update T_ENM_SUBCENTERS_PLAN set plan_status = :status where id = :parentId", nativeQuery = true)
    public void savePlanStatus(String parentId, String status);

    @Modifying
    @Transactional
    @Query(value = "update T_ENM_SUBCENTERS_PLAN set plan_report=:status,plan_status='0' where id=:parentId", nativeQuery = true)
    public void savePlanReport(String parentId, String status);
    @Query(value = "SELECT * FROM T_ENM_SUBCENTERS_PLAN where ID=:list", nativeQuery = true)
    SubCentersPlan getPlanSheng(String list);
}
