package com.chinags.device.schedule.service;

import com.chinags.device.schedule.dao.ScheduleCommDao;
import com.chinags.device.schedule.entity.ScheduleComm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 工程进度管理工程验收接口
 * @Author : Mark_wang
 * @Date : 2019-9-10
 **/
@Service
public class ScheduleCommService {
    @Autowired
    private ScheduleCommDao scheduleCommDao;

    /**
     * 保存工程进度验收
     * @param scheduleComm
     */
    public void save(ScheduleComm scheduleComm) {
        scheduleCommDao.save(scheduleComm);
    }

    /**
     * 根据工程进度id查询验收信息
     * @param scheduleId
     * @return
     */
    public List<ScheduleComm> findByScheduleId(String scheduleId) {
        return scheduleCommDao.findByScheduleId(scheduleId);
    }

    /**
     * 根据验收信息id删除验收信息
     * @param id
     */
    public void deleteById(String id) {
        scheduleCommDao.deleteById(id);
    }

    /**
     * 工程进度验收信息归档
     * @param scheduleId
     */
    public void updateStatusByScheduleId(String scheduleId) {
        scheduleCommDao.updateStatusByScheduleId(scheduleId);
    }

    /**
     * 根据进度id删除验收信息
     * @param scheduleId
     */
    public void deleteByScheduleId(String scheduleId) {
        scheduleCommDao.deleteByScheduleId(scheduleId);
    }

    /**
     * 保存
     * @param scheduleComms
     */
    public void saves(List<ScheduleComm> scheduleComms) {
       scheduleCommDao.saveAll(scheduleComms);
    }
}
