package com.chinags.device.maintain.dao;

import com.chinags.device.maintain.entity.MaintainInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author XieWenqing
 * @date 2019/12/6 下午 4:43
 */
public interface MaintainInfoDao extends JpaRepository<MaintainInfo, String>, JpaSpecificationExecutor<MaintainInfo> {
    /**
     * 根据id查询养护记录
     * @param id  养护记录id
     */
    @Query(value = "SELECT * FROM T_COA_MAINTAIN_INFO WHERE ID=:id", nativeQuery = true)
    MaintainInfo selectById(String id);

    /**
     * 根据id删除养护记录
     * @param id 养护记录id
     */
    @Override
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM T_COA_MAINTAIN_INFO WHERE id=:id", nativeQuery = true)
    void deleteById(String id);
    @Query(value = "SELECT * FROM T_COA_MAINTAIN_INFO WHERE ID=:id", nativeQuery = true)
    MaintainInfo selectBymaitainId(String id);
    @Modifying
    @Transactional
    @Query(value = "UPDATE T_COA_MAINTAIN_INFO SET COUNT_NUM=:countNum WHERE ID=:id", nativeQuery = true)
    public void updateBycount(String id,Integer countNum);
    @Query(value = "SELECT * FROM T_COA_MAINTAIN_INFO WHERE rownum<=5 ORDER BY COUNT_NUM DESC", nativeQuery = true)
    List<MaintainInfo> listDataTo();
    @Query(value = "SELECT * FROM T_COA_MAINTAIN_INFO where to_char(END_TIME,'yyyy-mm-dd') between :startDate and :endDate", nativeQuery = true)
    List<MaintainInfo> selectBymaintainDate(String startDate, String endDate);
    @Query(value = "SELECT * FROM T_COA_MAINTAIN_INFO order by END_TIME DESC", nativeQuery = true)
    List<MaintainInfo> selectBymaintainDateTo();
    @Query(value = "SELECT a.*,b.COUNT as count  FROM T_COA_MAINTAIN_INFO a  INNER JOIN (SELECT count(1) as count,DECEIVE_ID from T_COA_MAINTAIN_INFO GROUP BY DECEIVE_ID) b on a.DECEIVE_ID = b.DECEIVE_ID  ORDER BY b.count DESC", nativeQuery = true)
    List<MaintainInfo> listDataToK();
    @Query(value = "SELECT count(1) FROM T_COA_MAINTAIN_INFO where DECEIVE_ID=:id group by DECEIVE_ID", nativeQuery = true)
    Integer listCount(String id);
}
