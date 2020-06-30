package com.chinags.device.schedule.service;

import com.chinags.device.schedule.dao.ScheduleProjectDao;
import com.chinags.device.schedule.entity.ScheduleProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工程进度管理工程进度项目接口
 * @Author : Mark_wang
 * @Date : 2019-9-10
 **/
@Service
public class ScheduleProjectService {

    @Autowired
    private ScheduleProjectDao scheduleProjectDao;

    /**
     * 根据工程进度id获取进度下的素有项目
     * @param scheduleId
     * @return
     */
    public List<ScheduleProject> findByScheduleId(String scheduleId) {
        return scheduleProjectDao.findByScheduleId(scheduleId);
    }

    /**
     * 保存工程进度下的项目
     * @param scheduleProject
     */
    public void save(ScheduleProject scheduleProject) {
        scheduleProjectDao.save(scheduleProject);
    }

    /**
     * 根据id删除工程进度下的项目
     * @param id
     */
    public void deleteById(String id) {
        scheduleProjectDao.deleteById(id);
    }

    /**
     * 根据id
     * @param id
     * @return
     */
    public ScheduleProject selectById(String id) {
        return scheduleProjectDao.selectById(id);
    }

    /**
     * 根据进度id删除项目信息
     * @param scheduleId
     */
    public void deleteByScheduleId(String scheduleId) {
        scheduleProjectDao.deleteByScheduleId(scheduleId);
    }

    /**
     * 根据创建人修改进程id
     * @param scheduleId
     * @param createBy
     */
    public void updateScheduleIdByCreateBy(String scheduleId, String createBy) {
        scheduleProjectDao.updateScheduleIdByCreateBy(scheduleId, createBy);
    }

    /**
     * 根据用户清理没有进程id的脏数据
     * @param createBy
     */
    public void deleteOldBycreateBy(String createBy) {
        scheduleProjectDao.deleteOldBycreateBy(createBy);
    }

    /**
     * 获取进度的完成金额
     * @param scheduleId
     * @return
     */
    public Double getEndMoneyByScheduleId(String scheduleId) {
        return scheduleProjectDao.getEndMoneyByScheduleId(scheduleId);
    }

    /**
     * 获取进度下是否有完成的项目
     * @param scheduleId
     * @return
     */
    public Integer getEndMoneypRrojectsByScheduleId(String scheduleId) {
        return scheduleProjectDao.getEndMoneypRrojectsByScheduleId(scheduleId);
    }
}
