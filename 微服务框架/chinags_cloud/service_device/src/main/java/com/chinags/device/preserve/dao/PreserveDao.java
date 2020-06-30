package com.chinags.device.preserve.dao;


import com.chinags.device.preserve.entity.Preserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreserveDao extends JpaRepository<Preserve, String>, JpaSpecificationExecutor<Preserve> {
    /**
     * 根据记录id查询记录
     * @param maintainId
     * @return
     */
    @Query(value = "SELECT id FROM T_COA_PRESERVE WHERE maintain_id=:maintainId", nativeQuery = true)
    List<String> selectByMaintainId(String maintainId);
    @Query(value = "SELECT * FROM T_COA_PRESERVE WHERE maintain_id=:id", nativeQuery = true)
    List<Preserve> findByScheduleId(String id);
    @Query(value = "SELECT count(*),MAINTAIN_ID FROM T_COA_PRESERVE group by MAINTAIN_ID", nativeQuery = true)
    List<Preserve> selectCount();
}
