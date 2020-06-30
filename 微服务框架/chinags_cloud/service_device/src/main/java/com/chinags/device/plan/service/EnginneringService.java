package com.chinags.device.plan.service;

import com.chinags.common.entity.PageResult;
import com.chinags.device.plan.dao.EnginneringDao;
import com.chinags.device.plan.dao.PlanDao;
import com.chinags.device.plan.entity.Enginnering;
import com.chinags.device.plan.entity.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnginneringService {
    @Autowired
    private EnginneringDao enginneringDao;

    //根据计划id获取工程。
    public List<Enginnering> findAll(String planid) {
        return enginneringDao.getByEnginneringPlan(planid);
    }

    /**
     * 保存代码字段
     * @param enginnering 工程数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Enginnering enginnering){
        enginneringDao.save(enginnering);
    }

    /**
     * 删除工程
     * @param id
     * @return
     */
    public void delete(String id) {
        enginneringDao.deleteById(id);
    }

    public List<Enginnering> selectEnginnering(String id) {
        return enginneringDao.selectEnginnering(id);
    }
}
