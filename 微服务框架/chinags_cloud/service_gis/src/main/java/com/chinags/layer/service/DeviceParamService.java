package com.chinags.layer.service;

import com.chinags.common.service.BaseService;
import com.chinags.layer.dao.master.DeviceDao;
import com.chinags.layer.dao.master.DeviceParamDao;
import com.chinags.layer.entity.master.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceParamService extends BaseService<Device>{
    @Autowired
    private DeviceParamDao deviceParamDao;
    public List<Map<String,Object>> selectDeviceByCodeId(String id) {
        return deviceParamDao.getDeviceByCodeId(id);
    }


    public List<Map<String,Object>> selectAACParam(String id) {
        return deviceParamDao.getAACParamByCodeId(id);
    }
        public List<Map<String,Object>> selectAADParam(String id) {
            return deviceParamDao.getAADParamByCodeId(id);
        }
    public List<Map<String,Object>> selectDeviceParam(String id) {
        return  deviceParamDao.getDeviceParamByCodeId(id);
    }





}
