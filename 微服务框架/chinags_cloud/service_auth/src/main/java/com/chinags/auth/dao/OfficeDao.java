package com.chinags.auth.dao;

import com.chinags.auth.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface OfficeDao extends JpaRepository<Office, String>, JpaSpecificationExecutor<Office> {

    public Office getAreaByOfficeCode(String officeCode);

    public List<Office> getAreaByParentCodesLike(String parentCodes);

    @Query(value = "select count(t) from Office t where parent_codes LIKE CONCAT('%',:parentCodes,'%')")
    public Integer getParentCodesCount(@Param("parentCodes") String parentCodes);

    @Query(value = "select count(t) from Office t where parent_codes LIKE CONCAT('%',:parentCodes,'%') and status=0")
    public Integer getStopParentCodesCount(@Param("parentCodes") String parentCodes);

    @Query(value = "select office_name from T_PUB_SYS_OFFICE t where office_level =:officeLevel and office_name<>'分中心'",nativeQuery =true)
    public List<Map<String,Object>> getOffice(@Param("officeLevel") String officeLevel);

    @Query(value = "select officeCode from Office t where parentCodes LIKE CONCAT('%',:parentCodes,'%')")
    public List<String> getOfficeParentCodes(@Param("parentCodes") String parentCodes);
    @Query(value = "select t.officeCode as officeCode,t.parentCode as parentCode,t.officeName as officeName,t.treeNames as treeNames,t.treeLevel as treeLevel,t.officeType as officeType,t.areaId.areaCode as areaID,t.officeLevel as officeLevel,t.officeUnit as officeUnit,t.areaType as areaType from Office t")
    List<Map<String,Object>> getOrg();

    @Query(value = "select concat(tsc.parent_codes,:id) from T_PUB_SYS_OFFICE tsc where tsc.office_code = :id and status<>1", nativeQuery = true)
    String getOfficeIdString(String id);

    @Query(value = "select * from (select * from T_PUB_SYS_OFFICE where office_code in (:ids) and OFFICE_LEVEL=:typeValue order by TREE_LEVEL) where ROWNUM = 1", nativeQuery = true)
    Office getOfficeTypeString(String[] ids, String typeValue);

    @Query(value = "select office_code from T_PUB_SYS_OFFICE where parent_codes like concat('%',concat(:id,',%'))", nativeQuery = true)
    List<String> getOrgOfficeChirldsId(String id);

    @Query(value = "select office_code from T_PUB_SYS_OFFICE where parent_codes like concat('%',concat(:id,',%')) and OFFICE_LEVEL=:typeValue", nativeQuery = true)
    List<String> getOrgOfficeChirldsAndTypeId(String id, String typeValue);

    @Query(value = "select * from T_PUB_SYS_OFFICE where parent_codes like concat('%',concat(:id,',%'))", nativeQuery = true)
    List<Office> getOrgOfficeChirlds(String id);

    @Query(value = "select * from T_PUB_SYS_OFFICE where parent_codes like concat('%',concat(:id,',%')) and OFFICE_LEVEL=:typeValue", nativeQuery = true)
    List<Office> getOrgOfficeChirldsAndType(String id, String typeValue);

    @Query(value = "select * from T_PUB_SYS_OFFICE where parent_code =:parentCode and OFFICE_LEVEL=:officeLevel", nativeQuery = true)
    List<Office> getListByParentAndLevel(String parentCode, String officeLevel);

    @Query(value = "select * from T_PUB_SYS_OFFICE where OFFICE_LEVEL=:officeLevel", nativeQuery = true)
    List<Office> getListByOfficeLevel(String officeLevel);
    @Query(value = "select * from T_PUB_SYS_OFFICE  where office_code =:officeCode",nativeQuery =true)
    List<Office> selectByofficeCode(String officeCode);
    @Query(value = "select office_code from T_PUB_SYS_OFFICE  where PARENT_CODES LIKE CONCAT('%',concat(:officeCode,',%'))",nativeQuery =true)
    List<String> selectByofficeCodeTo(String officeCode);
    @Query(value = "select * from T_PUB_SYS_OFFICE  where PARENT_CODES LIKE CONCAT('%',concat(:value,',%'))",nativeQuery =true)
    List<Office> selectByofficeCodeToL(String value);
    @Query(value = "select * from  T_PUB_SYS_OFFICE where PARENT_CODE =:value",nativeQuery =true)
    List<Office> selectByParentCode(String value);
    @Query(value = "select OFFICE_TYPE  where OFFICE_CODE =:userCode",nativeQuery =true)
    List<String> selectByorgId(String userCode);
    @Query(value = "select * from T_PUB_SYS_OFFICE  where office_code =:officeCode",nativeQuery =true)
    List<Office> QueryBytreeDate(String officeCode);
    @Query(value = "select * from T_PUB_SYS_OFFICE  where PARENT_CODES LIKE CONCAT('%',concat(:officeCode,',%'))",nativeQuery =true)
    List<Office> QueryByofficeCode(String officeCode);
    @Query(value = "select * from T_PUB_SYS_OFFICE where office_code =:gt", nativeQuery = true)
    Office selectByofficezl(String gt);
    @Query(value = "select * from T_PUB_SYS_OFFICE where office_code =:parcode", nativeQuery = true)
    Office selectByoffice(String parcode);
    @Query(value = "select office_code from T_PUB_SYS_OFFICE where PARENT_CODES LIKE CONCAT('%',concat(:officeCode,',%'))", nativeQuery = true)
    List<String> getziji(String officeCode);
    @Query(value = "select * from T_PUB_SYS_OFFICE where office_code =:officeCode", nativeQuery = true)
    Office getshangji(String officeCode);
    @Query(value = "select user_code from T_PUB_SYS_USER where LOGIN_CODE =:userName", nativeQuery = true)
    String selectByuserName(String userName);
    @Query(value = "select * from T_PUB_SYS_OFFICE where OFFICE_NAME =:officeName", nativeQuery = true)
    List<Office> getFenzhongxin(String officeName);
    @Query(value = "select OFFICE_NAME from T_PUB_SYS_OFFICE where PARENT_CODE =:officeCode", nativeQuery = true)
    List<String> getFenzhongxinT(String officeCode);
}
