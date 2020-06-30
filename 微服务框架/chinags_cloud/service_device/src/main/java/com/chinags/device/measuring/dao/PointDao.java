package com.chinags.device.measuring.dao;

import com.chinags.device.measuring.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 测点维护dao
 * @Author : Mark_wang
 * @Date : 2019-12-4
 **/
public interface PointDao extends JpaRepository<Point, String>, JpaSpecificationExecutor<Point> {
    /**
     * 根据测点编号和期数查询测点维护信息
     * @param pid
     * @param dateNum
     * @return
     */
    @Query(value = "select * from t_enm_point where pid=:pid and date_num=:dateNum", nativeQuery = true)
    Point getByPointIdAndDateNum(String pid, int dateNum);

    /**
     * 根据测点类型查询最后期数的测点维护信息
     * @param such
     * @return
     */
    @Query(value = "SELECT a.* FROM T_ENM_POINT a INNER JOIN (SELECT POINT_ID, MAX(DATE_NUM) as DATE_NUM, SUCH from t_enm_point WHERE SUCH =:such GROUP BY POINT_ID, SUCH) b ON a.POINT_ID = b.POINT_ID AND a.SUCH = b.SUCH and a.DATE_NUM=b.DATE_NUM", nativeQuery = true)
    List<Point> getAllBySuch(String such);

    /**
     * 根据pointId查询测点维护信息集合
     * @param pid
     * @return
     */
    @Query(value = "select * from t_enm_point where pid=:pid", nativeQuery = true)
    List<Point> getPointsByPointId(String pid);

    /**
     * 根据期数区间获取所有的测点维护信息
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "select * from t_enm_point where such=:such and (date_num  between :startDate and :endDate)", nativeQuery = true)
    List<Point> getPointsByStartAndEndDateNumAndSuch(int startDate, int endDate, String such);

    /**
     * 根据期数区间获取所有的测点维护信息
     * @param startDate
     * @param endDate
     * @param pid
     * @return
     */
    @Query(value = "select * from t_enm_point where pid=:pid and (date_num between :startDate and :endDate)", nativeQuery = true)
    List<Point> getPointsByStartAndEndDateNumAndPointId(int startDate, int endDate, String pid);

    /**
     * 根据年度所有的测点维护信息
     * @param year
     * @return
     */
    @Query(value = "select * from t_enm_point where date_num like :year% and such=:such", nativeQuery = true)
    List<Point> getPointsByYearAndSuch(int year, String such);

    /**
     * 根据年度和测点id查询测点维护信息
     * @param year
     * @param pid
     * @return
     */
    @Query(value = "select * from t_enm_point where pid=:pid and date_num like :year%", nativeQuery = true)
    List<Point> getPointsByYearAndPointId(int year, String pid);

    /**
     * 根据设施设备ID级联查询人工传感器设备测点维护信息
     * @param deviceId
     * @return
     */
    @Query(value = "SELECT p FROM Point p left join Device t on p.pid = t.id where p.type = '人工测点' " +
            "and (t.parentCodes LIKE CONCAT('%,',:deviceId,',%') or t.id = :deviceId) " +
            "AND p.updateDate = (SELECT max( p1.updateDate ) FROM Point p1 WHERE p1.pid = p.pid)")
    List<Point> findAllByDeviceId(String deviceId);
    @Query(value = "select * from t_enm_point where EQU=:cedian and PID=:officeCode order by CREATE_DATE DESC", nativeQuery = true)
    List<Point> selectPoint(String cedian, String officeCode);
    @Query(value = "select * from t_enm_point where PID=:officeCode order by CREATE_DATE DESC", nativeQuery = true)
    List<Point> selectPointLb(String officeCode);

    /**
     * 查询所有的人工传感器设备测点维护信息
     * @return
     */
    @Query(value = "SELECT p FROM Point p where p.type = '人工测点'")
    List<Point> findAllRenGong();
}
