package com.chinags.device.measuring.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.device.measuring.dao.PointAlarmDao;
import com.chinags.device.measuring.entity.PointAlarm;
import com.chinags.device.plan.dao.PlanDao;
import com.chinags.device.plan.dao.SubCentersPlanDao;
import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.entity.SubCentersPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报警测点信息service
 * @Author : Mark_Wang
 * @Date : 2020/3/5
 **/
@Service
public class PointAlarmService {
    @Autowired
    private PointAlarmDao pointAlarmDao;
    @Autowired
    private PlanDao planDao;
    @Autowired
    private SubCentersPlanDao subCentersPlanDao;

    /**
     * 保存测点报警信息
     * @param pointAlarm
     */
    public void save(PointAlarm pointAlarm) {
        pointAlarmDao.save(pointAlarm);
    }

    /**
     * 根据测点id和期数查询测点报警信息
     * @param pid
     * @param dateNum
     * @return
     */
    public PointAlarm getByPointIdAndDateNum(String pid, int dateNum) {
        return pointAlarmDao.getByPointIdAndDateNum(pid, dateNum);
    }

    /**
     * 查询测点报警信息
     * @param pointAlarm
     * @return
     */
    public PageResult<PointAlarm> getPointAlarmsByPointAlarm(PointAlarm pointAlarm) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(pointAlarm.getOrderBy())) {
            pageable = PageRequest.of(pointAlarm.getPageNo(), pointAlarm.getPageSize(), Sort.Direction.DESC,pointAlarm.getOrderBy());
        } else {
            pageable = PageRequest.of(pointAlarm.getPageNo(), pointAlarm.getPageSize(), Sort.Direction.DESC,"createDate");
        }
        Page<PointAlarm> page = pointAlarmDao.findAll(new Specification<PointAlarm>() {
            @Override
            public Predicate toPredicate(Root<PointAlarm> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotEmpty(pointAlarm.getSuch())) {
                    predicates.add(cb.like(root.get("such"),"%" + pointAlarm.getSuch() + "%"));
                }
                /*if (schedule.getYear() != 0) {
                    predicates.add(cb.equal(root.get("year").as(Integer.class), schedule.getYear()));
                }*/
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<PointAlarm> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 根据id修改测点报警处理状态
     * @param id
     */
    public void updateStatusById(String id) {
        pointAlarmDao.updateStatusById(id);
    }

    /**
     * 根据id获取测点报警信息
     * @param id
     * @return
     */
    public PointAlarm selectById(String id) {
        return pointAlarmDao.selectById(id);
    }

    /***
     * 获取全部信息
     * @return
     */
    public List<PointAlarm> getPointAlarmsByPointAlarmTo() {
        return pointAlarmDao.selectAll();
    }

    public List<Map<String,Object>> selectByCount() {
        return pointAlarmDao.selectBycount();
    }

    public Map<String,Object> getPointAlarmsByPidTo(String wordId) {
        List<String> dataId =  pointAlarmDao.getPointAlarmsByPidTo(wordId);
        List<String> dataIdTo = pointAlarmDao.getDataId(dataId.get(0));
        List<String> list = pointAlarmDao.getdataIdTo(dataIdTo.get(0));
        Plan plan = planDao.getPlan(list.get(0));
        Map<String,Object> map = new HashMap<>();
        if(null != plan){
            map.put("PLAN_NAME",plan.getPlanName());
        }
        if(null != plan){
            map.put("PLAN_CODE",plan.getPlanCode());
        }
        return map;

    }

    public Map<String, Object> getPointAlarmsByPidToK(String wordId) {
        List<String> dataId =  pointAlarmDao.getPointAlarmsByPidTo(wordId);
        List<String> dataIdTo = pointAlarmDao.getDataId(dataId.get(0));
        List<String> list = pointAlarmDao.getdataIdToZ(dataIdTo.get(0));
        SubCentersPlan plan = subCentersPlanDao.getPlanSheng(list.get(0));
        Map<String,Object> map = new HashMap<>();
        if(null != plan){
            map.put("PLAN_NAME",plan.getPlanName());
        }
        if(null != plan){
            map.put("PLAN_CODE",plan.getPlanCode());
        }
        return map;
    }

    public List<PointAlarm> selectPointAlarm(String cedian, String id) {
        return pointAlarmDao.selectPointAlarm(cedian,id);
    }

    public List<PointAlarm> selectPointAlarmLb(String id) {
        return pointAlarmDao.selectPointAlarmLb(id);
    }
}
