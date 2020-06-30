package com.chinags.device.plan.dao;

import com.chinags.device.plan.entity.Enginnering;
import com.chinags.device.plan.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnginneringDao extends JpaRepository<Enginnering, String>, JpaSpecificationExecutor<Enginnering> {

    List<Enginnering> getByEnginneringPlan(String enginneringPlan);
    @Query(value = "SELECT * FROM T_ENM_ENGINEERING where ENGINNERING_PLAN=:id", nativeQuery = true)
    List<Enginnering> selectEnginnering(String id);
}
