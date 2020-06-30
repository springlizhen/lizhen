package com.chinags.device.plan.service;

import com.chinags.common.entity.PageResult;
import com.chinags.device.basic.dao.SensorDao;
import com.chinags.device.basic.entity.Sensor;
import com.chinags.device.plan.dao.PlanDao;
import com.chinags.device.plan.entity.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanService {
    @Autowired
    private PlanDao planDao;

    public List<Plan> findAll(Plan plan){
        //测试阶段 注释掉，将会展示所有
        plan.setPlanStatus(Plan.STATUS_DELETE);
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        Example<Plan> ex = Example.of(plan);
        return planDao.findAll(ex,sort);
    }

    public List<Plan> findSub(Plan plan,String usercode,List<String> deceiveIds){
        //测试阶段 注释掉，将会展示所有
        //planId 指的是分中心上报的计划
        if(plan.getStationId()!=null){
            return planDao.findSubs(plan.getPlanId(), Plan.STATUS_DELETE, plan.getStationId());
        }else{
//            return planDao.findSub(plan.getPlanId(), Plan.STATUS_DELETE);
            if(!"".equals(plan.getPlanId()) && null != plan.getPlanId()){
                return planDao.findAt(plan.getPlanId());
            }else{
                List<Plan> planList = new ArrayList<>();
                for(String id:deceiveIds){
                    List<Plan>  list = planDao.selectBystatus(id,Plan.STATUS_DELETE);
                    planList.addAll(list);
                }
                return planList;

            }
        }
    }

    /**
     * 获得计划 by id
     * @param id
     * @return
     */
    public Plan getOne(String id){
        return planDao.getById(id);
    }

    /**
     * 查询全部列表
     * @return field分页数据
     */
    public PageResult find(Plan plan,List<String> deceiveIds) {
        PageRequest pageable;
        if(plan.getOrderBy()==null|| "".equals(plan.getOrderBy())) {
            pageable = PageRequest.of(plan.getPageNo(), plan.getPageSize(), Sort.Direction.DESC,"createDate");
        } else {
            pageable = PageRequest.of(plan.getPageNo(), plan.getPageSize(), plan.getDesc(), plan.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(planDao.findAll(
                (Specification<Plan>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    Predicate[] pre = new Predicate[predicates.size()];
                    if (plan.getPlanName() !=null && !"".equals(plan.getPlanName())) {
                        predicates.add(cb.like(root.get("planName").as(String.class), "%" + plan.getPlanName() + "%"));
                    }
                    //计划代码
                    if (plan.getPlanCode() !=null && !"".equals(plan.getPlanCode())) {
                        predicates.add(cb.like(root.get("planCode").as(String.class), "%" + plan.getPlanCode() + "%"));
                    }
                    //计划类型
                    if (plan.getPlanType() !=null && !"".equals(plan.getPlanType())) {
                        predicates.add(cb.like(root.get("planType").as(String.class), "%" + plan.getPlanType() + "%"));
                    }
                    if (plan.getPlanYear() !=null && !"".equals(plan.getPlanYear())) {
                        predicates.add(cb.like(root.get("planYear").as(String.class), "%" + plan.getPlanYear() + "%"));
                    }
                    if (plan.getPlanOfficeName() !=null && !"".equals(plan.getPlanOfficeName())) {
                        predicates.add(cb.like(root.get("planOfficeName").as(String.class), "%" + plan.getPlanOfficeName() + "%"));
                    }
                    if (plan.getPlanPepName() !=null && !"".equals(plan.getPlanPepName())) {
                        predicates.add(cb.like(root.get("planPepName").as(String.class), "%" + plan.getPlanPepName() + "%"));
                    }
                    if (plan.getPlanStatus() !=null && !"".equals(plan.getPlanStatus())) {
                        predicates.add(cb.equal(root.get("planStatus").as(String.class), plan.getPlanStatus()));
                    }
                    if (plan.getPlanParentId() !=null && !"".equals(plan.getPlanParentId())) {
                        predicates.add(cb.equal(root.get("planParentId").as(String.class), plan.getPlanParentId()));
                    }
                    if (plan.getStationId() !=null && !"".equals(plan.getStationId())) {
                        predicates.add(cb.equal(root.get("stationId").as(String.class), plan.getStationId()));
                    }
                    //计划id
                    if (plan.getPlanId() !=null && !"".equals(plan.getPlanId())) {
                        predicates.add(cb.equal(root.get("planId").as(String.class), plan.getPlanId()));
                    }
                    Path<Object> path = root.get("planOffice");
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    if(deceiveIds.size()>0){  //当deceiveIds大于0时，再进行查询，否则报错
                        for(String deceiveId: deceiveIds){
                            in.value(deceiveId);
                        }
                    }else{  //当deceiveIds小于0时，传入一个空串，防止报错
                        in.value("");
                    }
                    predicates.add(cb.and(in));
                    predicates.add(cb.notEqual(root.get("status").as(String.class), Plan.STATUS_DELETE));
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 保存管理站计划
     * @param plan 管理站计划数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Plan plan){
        planDao.save(plan);
    }

    /**
     * 删除管理站计划
     * @param id
     * @return
     */
    public Plan delete(String id) {
        Plan plan = planDao.getById(id);
        plan.setStatus(Plan.STATUS_DELETE);
        return planDao.save(plan);
    }

    /**
     * 管理站审核状态修改
     * @param id
     * @return
     */
    public void savePlanStatus(String id, String status) {
        planDao.savePlanStatus(id, status);
    }

    /**
     * 管理站上报状态修改
     * @param id
     * @return
     */
    public void savePlanReport(String id, String status) {
        planDao.savePlanReport(id, status);
    }

    public List<Plan> findAllTo(Plan plan) {
        return planDao.findAll();
    }

    public List<Plan> selectByPlan(String status) {
        return planDao.selectByPlan(status);
    }
}
