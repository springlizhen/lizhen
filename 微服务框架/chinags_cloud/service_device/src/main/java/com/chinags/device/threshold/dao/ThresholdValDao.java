package com.chinags.device.threshold.dao;

import com.chinags.device.threshold.entity.ThresholdVal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author XieWenqing
 * @date 2019/12/10 上午 11:29
 */
public interface ThresholdValDao extends JpaRepository<ThresholdVal, String>, JpaSpecificationExecutor<ThresholdVal> {
    /**
     * 根据id查询工程安全阈值设置信息
     */
    @Query(value = "SELECT * FROM T_COA_THRESHOLD_VAL WHERE ID=:id", nativeQuery = true)
    ThresholdVal selectById(String id);

    /**
     * 根据设备id查询工程安全阈值设置信息
     */
    @Query(value = "SELECT * FROM T_COA_THRESHOLD_VAL WHERE DECEIVE_ID=:deceiveId", nativeQuery = true)
    ThresholdVal selectByDeceiveId(String deceiveId);

    /**
     * 更改安全阈值使用状态
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE T_COA_THRESHOLD_VAL SET IS_USE=:status, UPDATE_BY=:updateBy, UPDATE_DATE=:updateDate  WHERE ID=:id", nativeQuery = true)
    void updateStatusById(String id, String status, String updateBy, Date updateDate);

    /**
     * 更新安全阈值信息，是否可以在列表中显示
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE T_COA_THRESHOLD_VAL SET IS_SHOW=:isShow WHERE DECEIVE_ID=:deceiveId", nativeQuery = true)
    void updateIsShowByDeceiveId(String isShow, String deceiveId);
}
