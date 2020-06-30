package com.chinags.layer.service;

import com.chinags.common.service.BaseService;
import com.chinags.layer.dao.master.DeviceDao;
import com.chinags.layer.entity.master.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceService extends BaseService<Device>{
    @Autowired
    private DeviceDao deviceDao;
    public List<Map<String,Object>> selectGate(String type,String orgId) {
        return  deviceDao.selectGate(type,orgId);
    }





}
