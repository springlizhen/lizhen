package com.chinags.device.schedule.dao;

import com.chinags.device.schedule.entity.ScheduleComm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-9-9
 **/
public interface ScheduleCommDao extends JpaRepository<ScheduleComm, String>, JpaSpecificationExecutor<ScheduleComm> {

    @Query(value = "SELECT * FROM T_ENM_SCHEDULE_COMM WHERE ID=:id ORDER BY CREATE_DATE DESC ", nativeQuery = true)
    ScheduleComm selectById(String id);

    @Query(value = "SELECT * FROM T_ENM_SCHEDULE_COMM WHERE SCHEDULE_ID=:scheduleId ORDER BY CREATE_DATE ASC", nativeQuery = true)
    List<ScheduleComm> findByScheduleId(String scheduleId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_ENM_SCHEDULE_COMM SET STATUS=1 WHERE SCHEDULE_ID=:scheduleId", nativeQuery = true)
    void updateStatusByScheduleId(String scheduleId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM T_ENM_SCHEDULE_COMM WHERE SCHEDULE_ID=:scheduleId", nativeQuery = true)
    void deleteByScheduleId(String scheduleId);

}
