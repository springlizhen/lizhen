package com.chinags.device.check.dao.plus;

import com.chinags.device.check.entity.CheckNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface CheckNoteDaoPlus {
    public List<Map<String,Object>> getCheckNoteByOffice(@Param("checkCenter") String checkCenter, @Param("manageStation") String manageStation, @Param("manageOffice") String manageOffice);
    public List<Map<String,Object>> getCheckByOffice(@Param("checkCenter") String checkCenter, @Param("manageStation") String manageStation, @Param("manageOffice") String manageOffice);
    public List<Map<String,Object>> getCheckNote(@Param("checkPerson") String checkPerson, @Param("createDate") String createDate);
    public  Page<CheckNote> selectCheckNote(@Param("checkNote") CheckNote checkNote, Pageable pageable);
    Page<CheckNote> selectCheckNoteTo(List<CheckNote> list3,Pageable pageable,@Param("checkNote") CheckNote checkNote);
}
