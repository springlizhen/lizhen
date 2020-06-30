package com.chinags.device.plan.dao;

import com.chinags.device.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PlanDao extends JpaRepository<Plan, String>, JpaSpecificationExecutor<Plan> {
    public Plan getById(String id);

    @Modifying
    @Transactional
    @Query(value = "update Plan set planStatus='1' , planId = null , planNames = null where planId = :planId")
    public void saveClearPlanId(String planId);

    @Modifying
    @Transactional
    @Query(value = "update T_ENM_PLAN set plan_status = '3',plan_id=:parentId,plan_names=:parentName where id in (:planIds)", nativeQuery = true)
    public void savePlanStatus(String[] planIds, String parentId, String parentName);

    @Query(value = "select id from T_ENM_PLAN where status <> '1' and plan_id=:parentid", nativeQuery = true)
    public List<String> getPlanIds(String parentid);

    @Query(value = "select * from T_ENM_PLAN where plan_id=:parentId", nativeQuery = true)
    public List<Plan> getPlanParent(String parentId);

    @Modifying
    @Transactional
    @Query(value = "update T_ENM_PLAN set plan_status = :status where id = :parentId", nativeQuery = true)
    public void savePlanStatus(String parentId, String status);

    @Modifying
    @Transactional
    @Query(value = "update T_ENM_PLAN set plan_report = :status, plan_status='0' where id = :parentId", nativeQuery = true)
    public void savePlanReport(String parentId, String status);

    @Query(value = "select * from T_ENM_PLAN where plan_id = :planId and org_id= :orgId", nativeQuery = true)
    List<Plan> getPlanOrgParent(String planId, String orgId);

    @Query(value = "select * from T_ENM_PLAN where plan_id = :planId or plan_status= :status  order by create_date asc", nativeQuery = true)
    List<Plan> findSub(String planId, String status);

    @Query(value = "select * from T_ENM_PLAN where (plan_id = :planId or (plan_status= :status and station_id = :parentid)) order by create_date asc", nativeQuery = true)
    List<Plan> findSubs(String planId, String status, String parentid);

    @Query(value = "select * from T_ENM_PLAN where (plan_id = :planId or plan_status= :status) order by create_date asc", nativeQuery = true)
    List<Plan> findSubsys(String planId, String status);
    @Query(value = "select * from T_ENM_PLAN where plan_id = :planId   order by create_date asc", nativeQuery = true)
    List<Plan> findAt(String planId);
    @Query(value = "select * from T_ENM_PLAN where plan_status = :statusDelete and plan_office = :id   order by create_date asc", nativeQuery = true)
    List<Plan> selectBystatus(String id, String statusDelete);
    @Query(value = "SELECT * FROM T_ENM_PLAN where ID=:list", nativeQuery = true)
    Plan getPlan(String list);
    @Query(value = "SELECT * FROM T_ENM_PLAN", nativeQuery = true)
    List<Plan> selectByPlan(String status);
}
