package com.chinags.device.basic.dao;

import com.chinags.device.basic.entity.DeviceParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceParamDao extends JpaRepository<DeviceParam, String>, JpaSpecificationExecutor<DeviceParam> {

    public DeviceParam getDeviceParamById(String id);

    public DeviceParam getDeviceParamByDeviceIdAndCodeId(String deviceId, String codeId);

    public int countByDeviceIdAndCodeId(String deviceId, String codeId);

    @Query(value = "select id from DeviceParam where deviceId=:deviceId and codeId=:codeId")
    public List<String> getIdDeviceIdAndCodeId(String deviceId, String codeId);
    @Query(value = "SELECT * FROM T_ENM_DEVICE_PARAM WHERE DEVICE_ID=:id and CODE_ID=:fileid", nativeQuery = true)
   public DeviceParam selectByFileIdAndId(String id, String fileid);
}
