package com.chinags.layer.dao.master;

import com.chinags.layer.entity.master.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface   DeviceDao extends JpaRepository<Device, String>, JpaSpecificationExecutor<Device> {

    @Query(value = "select * from t_enm_device where DEVICE_CLASS=:type and DEVICE_POSITION is not null and ORG_ID=:orgId", nativeQuery = true)
    public List<Map<String,Object>> selectGate(String type,String orgId);
}
