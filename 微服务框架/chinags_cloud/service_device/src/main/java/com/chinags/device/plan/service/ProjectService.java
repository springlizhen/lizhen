package com.chinags.device.plan.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.plan.dao.PlanDao;
import com.chinags.device.plan.dao.ProjectDao;
import com.chinags.device.plan.entity.Enginnering;
import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.entity.Project;
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
public class ProjectService {
    @Autowired
    private ProjectDao projectDao;

    /**
     * 获得项目 by id
     * @param id
     * @return
     */
    public Project getOne(String id){
        return projectDao.getOne(id);
    }

    //根据计划id获取工程。
    public PageResult<Project> findAll(String enginneringId) {
        return new PageResult<Project>(projectDao.getByEnginneringIdOrderByCreateDate(enginneringId));
    }

    /**
     * 保存项目
     * @param project 项目
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Project project){
        projectDao.save(project);
    }

    /**
     * 删除项目
     * @param id
     * @return
     */
    public void delete(String id) {
        projectDao.deleteById(id);
    }

    public List<Project> selectByProject(String id) {
        return projectDao.selectByProject(id);
    }
}
