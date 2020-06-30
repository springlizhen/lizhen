package com.chinags.layer.dao.base;

import com.chinags.layer.entity.base.Office;
import com.chinags.layer.entity.master.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface OfficeDao extends JpaRepository<Office, String>, JpaSpecificationExecutor<Office> {
    @Query(value = "select * from t_pub_sys_office where office_type!='2' and longitude is not null and latitude is not null", nativeQuery = true)
    public List<Map<String,Object>> selectOffice();
}
