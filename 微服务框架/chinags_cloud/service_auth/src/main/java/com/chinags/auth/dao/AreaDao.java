package com.chinags.auth.dao;

import com.chinags.auth.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AreaDao extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area> {

    public Area getAreaByAreaCode(String areaCode);

    @Query(value = "select count(*) from t_pub_sys_area where parent_codes LIKE :parentCodes", nativeQuery = true)
    public Integer getParentCodesCount(@Param("parentCodes") String parentCodes);

    @Query(value = "select count(*) from t_pub_sys_area where parent_codes LIKE :parentCodes and status=0", nativeQuery = true)
    public Integer getStopParentCodesCount(@Param("parentCodes") String parentCodes);
}
