package com.chinags.device.check.dao;

import com.chinags.device.check.entity.CheckNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CheckNoteDao extends JpaRepository<CheckNote, String>, JpaSpecificationExecutor<CheckNote> {
    @Query(value="select longitude ,latitude,check_center,manage_station,manage_office  from T_COA_CHECK where id=:id",nativeQuery = true)
    public List<Map<String,Object>> selectCheckLongitudeById(String id);
    public CheckNote getCheckNoteById(String id);
    @Query(value="select  check_Name, id from T_COA_CHECK",nativeQuery = true)
    public List<Map<String,Object>> getCheckName();
    @Query(value="select longitude,latitude  from T_COA_CHECK_NOTE where check_person like '%'||:checkPerson ||'%' and to_char(create_date,'yyyy-MM-dd')=:createDate order by create_date",nativeQuery = true)
    public List<Map<String,Object>> getCheckNote(@Param("checkPerson") String checkPerson, @Param("createDate") String createDate);
    @Query(value="select  * from T_COA_CHECK_NOTE where 1=1  and check_person=:checkPerson order by partoal desc",nativeQuery = true)
    List<CheckNote> selectcheckList(@Param("checkPerson") String checkPerson);


//    @Query(value="select *  from T_COA_CHECK_NOTE where 1=:i and id<>id and  check_person=:checkPerson order by create_date",nativeQuery = true)
//    List<CheckNote> selectchekKb(String id, String checkPerson,String i);
}
