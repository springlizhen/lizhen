package com.chinags.layer.service;

import com.chinags.common.service.BaseService;
import com.chinags.layer.dao.base.OfficeDao;
import com.chinags.layer.dao.master.DeviceDao;
import com.chinags.layer.entity.base.Office;
import com.chinags.layer.entity.master.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OfficeService extends BaseService<Office>{
    @Autowired
    private OfficeDao officeDao;
    public List<Map<String,Object>> selectOffice() {
        return  officeDao.selectOffice();
    }





}
