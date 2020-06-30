package com.chinags.device.schedule.dao;

import com.chinags.device.schedule.entity.ScheduleProject;
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
public interface ScheduleProjectDao extends JpaRepository<ScheduleProject, String>, JpaSpecificationExecutor<ScheduleProject> {

    @Query(value = "SELECT * FROM T_ENM_SCHEDULE_PROJECT WHERE ID=:id", nativeQuery = true)
    ScheduleProject selectById(String id);

    @Query(value = "SELECT * FROM T_ENM_SCHEDULE_PROJECT WHERE SCHEDULE_ID=:scheduleId ORDER BY CREATE_DATE DESC", nativeQuery = true)
    List<ScheduleProject> findByScheduleId(String scheduleId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM T_ENM_SCHEDULE_PROJECT WHERE SCHEDULE_ID=:scheduleId", nativeQuery = true)
    void deleteByScheduleId(String scheduleId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_ENM_SCHEDULE_PROJECT SET SCHEDULE_ID=:scheduleId WHERE CREATE_BY=:createBy AND SCHEDULE_ID=:createBy ", nativeQuery = true)
    void updateScheduleIdByCreateBy(String scheduleId, String createBy);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM T_ENM_SCHEDULE_PROJECT WHERE CREATE_BY=:createBy AND SCHEDULE_ID=:createBy", nativeQuery = true)
    void deleteOldBycreateBy(String createBy);

    @Query(value = "SELECT SUM(PLAN_MONEY) FROM T_ENM_SCHEDULE_PROJECT WHERE SCHEDULE_ID=:scheduleId AND STATUS='已完成'", nativeQuery = true)
    Double getEndMoneyByScheduleId(String scheduleId);

    @Query(value = "SELECT COUNT(*) FROM T_ENM_SCHEDULE_PROJECT WHERE SCHEDULE_ID=:scheduleId AND STATUS='已完成'", nativeQuery = true)
    Integer getEndMoneypRrojectsByScheduleId(String scheduleId);

}
