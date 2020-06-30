package com.chinags.device.plan.dao;

import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectDao extends JpaRepository<Project, String>, JpaSpecificationExecutor<Project> {

    List<Project> getByEnginneringIdOrderByCreateDate(String enginneringId);

    List<Project> getByEnginneringIdIn(List<String> enginneringId);
    @Query(value = "SELECT * FROM T_ENM_PROJECT where ENGINNERING_ID=:id", nativeQuery = true)
    List<Project> selectByProject(String id);
}
