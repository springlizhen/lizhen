package com.chinags.device.measuring.dao;

import com.chinags.device.measuring.entity.PointAlarm;
import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.entity.SubCentersPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * 报警测点信息dao
 * @Author : Mark_Wang
 * @Date : 2020/3/5
 **/
public interface PointAlarmDao extends JpaRepository<PointAlarm, String>, JpaSpecificationExecutor<PointAlarm> {
    /**
     * 根据测点编号和期数查询测点报警信息
     * @param pid
     * @param dateNum
     * @return
     */
    @Query(value = "select * from t_enm_point_alarm where pid=:pid and date_num=:dateNum", nativeQuery = true)
    PointAlarm getByPointIdAndDateNum(String pid, int dateNum);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_ENM_POINT_ALARM SET STATUS=1 WHERE ID=:id", nativeQuery = true)
    void updateStatusById(String id);

    @Query(value = "SELECT * FROM T_ENM_POINT_ALARM WHERE ID=:id", nativeQuery = true)
    PointAlarm selectById(String id);
    @Query(value = "SELECT * FROM T_ENM_POINT_ALARM order by update_date desc", nativeQuery = true)
    List<PointAlarm> selectAll();
    @Query(value = "SELECT count(1) as count_num,EQU FROM T_ENM_POINT_ALARM WHERE rownum<=5  GROUP BY EQU ORDER BY COUNT(EQU) desc", nativeQuery = true)
    List<Map<String,Object>> selectBycount();
    @Query(value = "SELECT DATAID FROM HORIZON.TWR_HZ_INSTANCE where WORKID=:workId ", nativeQuery = true)
    List<String> getPointAlarmsByPidTo(String workId);
    @Query(value = "SELECT FORM_DATA_ID FROM HORIZON.TDR_HORIZON_PAGE_DATA where DATA_ID=:dataId ", nativeQuery = true)
    List<String> getDataId(String dataId);
    @Query(value = "SELECT PLAN_ID FROM HORIZON.T_PLAN_FENJV where ID=:dataIdTo ", nativeQuery = true)
    List<String>  getdataIdTo(String dataIdTo);
    @Query(value = "SELECT SUBCENTERS_PLAN FROM HORIZON.T_PLAN_SHENGJV where ID=:dataIdTo ", nativeQuery = true)
    List<String> getdataIdToZ(String dataIdTo);
    @Query(value = "select * from T_ENM_POINT_ALARM where EQU=:cedian and PID=:id order by CREATE_DATE DESC", nativeQuery = true)
    List<PointAlarm> selectPointAlarm(String cedian, String id);
    @Query(value = "select * from T_ENM_POINT_ALARM where PID=:id order by CREATE_DATE DESC", nativeQuery = true)
    List<PointAlarm> selectPointAlarmLb(String id);
}
