package com.chinags.device.basic.dao;

import com.chinags.device.basic.entity.DerviceOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DerviceOfficeDao extends JpaRepository<DerviceOffice, String>, JpaSpecificationExecutor<DerviceOffice> {

    public DerviceOffice getAreaByOfficeCode(String officeCode);

    public List<DerviceOffice> getAreaByParentCodesLike(String parentCodes);

    @Query(value = "select count(t) from DerviceOffice t where status<>1 and parent_codes LIKE CONCAT('%',:parentCodes,'%')")
    public Integer getParentCodesCount(@Param("parentCodes") String parentCodes);

    @Query(value = "select count(t) from DerviceOffice t where parent_codes LIKE CONCAT('%',:parentCodes,'%') and status=0")
    public Integer getStopParentCodesCount(@Param("parentCodes") String parentCodes);

    @Query(value = "select officeCode from DerviceOffice t where parentCodes LIKE CONCAT('%',:parentCodes,'%')")
    public List<String> getOfficeParentCodes(@Param("parentCodes") String parentCodes);
    @Query(value = "select * from T_ENM_DERVICE_OFFICE",nativeQuery =true)
    public List<DerviceOffice> selectFind();
    @Query(value = "select * from T_ENM_DERVICE_OFFICE where OFFICE_CODE=:deviceClass",nativeQuery =true)
    DerviceOffice selectByoffice(String deviceClass);
}
