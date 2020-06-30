package com.chinags.device.schedule.dao;

import com.chinags.device.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-9-9
 **/
public interface ScheduleDao extends JpaRepository<Schedule, String>, JpaSpecificationExecutor<Schedule> {

    // TODO 现在不知道权限的信息，后续需要根据权限获取权限下的数据

    @Query(value = "SELECT * FROM T_ENM_SCHEDULE WHERE ID=:id", nativeQuery = true)
    Schedule selectById(String id);

    @Query(value = "SELECT DISTINCT YEAR FROM T_ENM_SCHEDULE", nativeQuery = true)
    List<String> findAllYear();

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_ENM_SCHEDULE SET END_MONEY=END_MONEY+:money WHERE ID=:id", nativeQuery = true)
    void addEndMoneyById(Double money, String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_ENM_SCHEDULE SET END_MONEY=END_MONEY-:money WHERE ID=:id", nativeQuery = true)
    void redEndMoneyById(Double money, String id);

}
