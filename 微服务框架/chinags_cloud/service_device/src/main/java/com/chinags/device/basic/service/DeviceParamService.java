package com.chinags.device.basic.service;

import com.chinags.device.basic.dao.DeviceParamDao;
import com.chinags.device.basic.entity.DeviceParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DeviceParamService {
    @Autowired
    private DeviceParamDao deviceParamDao;

    /**
     * 获取左侧菜单栏
     * @param deviceParam 查询对象
     * @return Office集合
     */
    public List<DeviceParam> findAll(DeviceParam deviceParam){
        Example<DeviceParam> ex = Example.of(deviceParam);
        return deviceParamDao.findAll(ex);
    }

    /**
     * 获得代码字段信息 by id
     * @param id
     * @return
     */
    public DeviceParam getDeviceParamById(String id){
        return deviceParamDao.getDeviceParamById(id);
    }

    /**
     * 获得代码字段信息 by id
     * @param deviceId 设备id
     * @param codeId 代码id
     * @return
     */
    public DeviceParam getDeviceParamByDeviceIdAndCodeId(String deviceId, String codeId){
        return deviceParamDao.getDeviceParamByDeviceIdAndCodeId(deviceId, codeId);
    }

    /**
     * 是否存在值
     * @param deviceId 设备id
     * @param codeId 代码id
     * @return
     */
    public Boolean countByDeviceIdAndCodeId(String deviceId, String codeId){
        return deviceParamDao.countByDeviceIdAndCodeId(deviceId, codeId)>0;
    }

    /**
     * 获取id
     * @param deviceId 设备id
     * @param codeId 代码id
     * @return
     */
    public List<String> getIdDeviceIdAndCodeId(String deviceId, String codeId){
        return deviceParamDao.getIdDeviceIdAndCodeId(deviceId, codeId);
    }

    /**
     * 保存代码字段
     * @param deviceParam 代码字段数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(DeviceParam deviceParam){
        deviceParamDao.save(deviceParam);
    }

    /**
     * 批量保存代码字段
     * @param deviceParam 代码字段数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void saveAll(List<DeviceParam> deviceParam){
        deviceParamDao.saveAll(deviceParam);
    }

    /**
     * 根据编码获取到数值
     * @param id
     * @param fileid
     * @return
     */
    public DeviceParam selectByFileIdAndId(String id, String fileid) {
        return deviceParamDao.selectByFileIdAndId(id,fileid);
    }
}
