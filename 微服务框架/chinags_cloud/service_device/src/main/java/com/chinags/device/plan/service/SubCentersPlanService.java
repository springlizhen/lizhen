package com.chinags.device.plan.service;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.utils.MouthZhCn;
import com.chinags.common.utils.ResultKeyList;
import com.chinags.common.utils.ResultMap;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.client.DicDataClient;
import com.chinags.device.plan.dao.PlanDao;
import com.chinags.device.plan.dao.ProjectDao;
import com.chinags.device.plan.dao.SubCentersPlanDao;
import com.chinags.device.plan.entity.*;
import jdk.nashorn.internal.runtime.ListAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SubCentersPlanService {
    @Autowired
    private SubCentersPlanDao subCentersPlanDao;

    @Autowired
    private PlanDao planDao;

    @Autowired
    private EnginneringService enginneringService;

    @Autowired
    private DicDataClient dicDataClient;

    @Autowired
    private ProjectDao projectDao;
    /**
     * 获得计划信息 by id
     *
     * @param id
     * @return
     */
    public SubCentersPlan getOne(String id) {

        SubCentersPlan subCentersPlan = subCentersPlanDao.getById(id);
        List<String> planIds = planDao.getPlanIds(id);
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : planIds) {

            stringBuffer.append(s);
            stringBuffer.append(",");
        }
        if(stringBuffer.length()>0) {
            subCentersPlan.setPlanIds(stringBuffer.substring(0, stringBuffer.length() - 1));
        }
        return subCentersPlan;
    }

    /**
     * 查询全部列表
     *
     * @return field分页数据
     */
    public PageResult find(SubCentersPlan subCentersPlan,List<String> deceiveIds) {
        PageRequest pageable;
        if (subCentersPlan.getOrderBy() == null || "".equals(subCentersPlan.getOrderBy())) {
            pageable = PageRequest.of(subCentersPlan.getPageNo(), subCentersPlan.getPageSize(), Sort.Direction.DESC, "createDate");
        } else {
            pageable = PageRequest.of(subCentersPlan.getPageNo(), subCentersPlan.getPageSize(), subCentersPlan.getDesc(), subCentersPlan.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(subCentersPlanDao.findAll(
                (Specification<SubCentersPlan>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    Predicate[] pre = new Predicate[predicates.size()];
                    if (subCentersPlan.getPlanName() != null && !"".equals(subCentersPlan.getPlanName())) {
                        predicates.add(cb.like(root.get("planName").as(String.class), "%" + subCentersPlan.getPlanName() + "%"));
                    }
                    if (subCentersPlan.getPlanCode() != null && !"".equals(subCentersPlan.getPlanCode())) {
                        predicates.add(cb.like(root.get("planCode").as(String.class), "%" + subCentersPlan.getPlanCode() + "%"));
                    }
                    if (subCentersPlan.getPlanType() != null && !"".equals(subCentersPlan.getPlanType())) {
                        predicates.add(cb.like(root.get("planType").as(String.class), "%" + subCentersPlan.getPlanType() + "%"));
                    }
                    if (subCentersPlan.getPlanYear() != null && !"".equals(subCentersPlan.getPlanYear())) {
                        predicates.add(cb.like(root.get("planYear").as(String.class), "%" + subCentersPlan.getPlanYear() + "%"));
                    }
                    if (subCentersPlan.getPlanReport() != null && !"".equals(subCentersPlan.getPlanReport())) {
                        predicates.add(cb.equal(root.get("planReport").as(String.class), subCentersPlan.getPlanReport()));
                    }
                    //开发不打开
                    if (subCentersPlan.getStationId() !=null && !"".equals(subCentersPlan.getStationId())) {
                        predicates.add(cb.equal(root.get("stationId").as(String.class), subCentersPlan.getStationId()));
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
                    predicates.add(cb.notEqual(root.get("status").as(String.class), SubCentersPlan.STATUS_DELETE));
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                }, pageable));
    }

    /**
     * 保存分局中心计划
     *
     * @param subCentersPlan 分局中心计划数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(SubCentersPlan subCentersPlan) {
        subCentersPlanDao.save(subCentersPlan);
        planDao.saveClearPlanId(subCentersPlan.getId());
        if (subCentersPlan.getPlanIds() != null) {
            planDao.savePlanStatus(subCentersPlan.getPlanIds().split(","), subCentersPlan.getId(), subCentersPlan.getPlanName());
        }
    }

    /**
     * 删除分局中心计划
     *
     * @param id
     * @return
     */
    public SubCentersPlan delete(String id) {
        SubCentersPlan subCentersPlan = subCentersPlanDao.getById(id);
        subCentersPlan.setStatus(Plan.STATUS_DELETE);
        return subCentersPlanDao.save(subCentersPlan);
    }

    public PageResult<TableData> findCL(String planId, String orgId) {
        List<TableData> tableDataList = ListUtils.newArrayList();
        //获取所有通过的计划
        List<Plan> planParent= planDao.getPlanParent(planId);
        /*if(StringUtils.isEmpty(orgId)){
            planParent = planDao.getPlanParent(planId);
        }else{
            planParent = planDao.getPlanOrgParent(planId, orgId);
        }*/

        //计划遍历
        for (int x = 0; x < planParent.size(); x++) {
            //管理站对应工程和项目
            List<TableData> planTables = ListUtils.newArrayList();
            //获取计划对应工程
            List<Enginnering> engList = enginneringService.findAll(planParent.get(x).getId());
            //创建数据获取工具类对象
            ResultKeyList<Enginnering> resultKeyList = new ResultKeyList();
            //初始化数据提取工具类
            ResultMap<Project> projectResultMap = new ResultMap<>();
            //获取工程对应项目
            List<Project> projects = projectDao.getByEnginneringIdIn(resultKeyList.getStrings(engList,"id"));
            //放入集合对象
            projectResultMap.setEntityList(projects);
            if(orgId!=null){
                projects = projectResultMap.resultLists(true, "orgId", orgId);
                projectResultMap.setEntityList(projects);
            }
            //筛选出存在的类型
            Set<String> projectType = projectResultMap.valTypeList("projectType");
            //筛选出存在的类型名称
            Object[] projectTypeName = projectResultMap.valTypeList("projectTypeName").toArray();
            //根据项目类型帅选ListMap
            Map<String, List<Project>> projectsMap = projectResultMap.resultList( true, "projectType");
            //管理站计划总金额
            double moneyPlan = 0;
            StringBuffer nameAll = new StringBuffer();
            int z = 1;
            for (int i = 0; i < engList.size(); i++) {
                //管理站对应工程
                List<TableData> tableList = ListUtils.newArrayList();
                //工程价格
                Double money = new Double(0);
                StringBuffer name = new StringBuffer();
                //计数器
                int j = 1;
                //工程中类型遍历
                for (String s : projectType) {
                    //集合对象重置
                    projectResultMap.setEntityList(projectsMap.get(s));
                    //指定项目
                    List<Project> enginneringId = projectResultMap.resultLists(true, "enginneringId", engList.get(i).getId());
                    //添加项目
                    double tableProject = projectAllList(enginneringId, j, String.valueOf(projectTypeName[j-1]), tableList);
                    //项目价格统计
                    money += tableProject;
                    //是否存在项目
                    if(enginneringId.size()>0) {
                        //特殊字段拼接
                        name.append(j +"+");
                        j++;
                    }
                }
                if(tableList.size()>0) {
                    //工程
                    TableData tableData = new TableData(MouthZhCn.toChinese(String.valueOf(i + 1)), engList.get(i).getEnginneringName(), String.valueOf(money), engList.get(i).getRemarks(), name.length() > 0 ? name.substring(0, name.length() - 1) : "");
                    planTables.add(tableData);
                    //工程子项
                    planTables.addAll(tableList);
                    //工程价格统计
                    moneyPlan += money;
                    nameAll.append(MouthZhCn.toChinese(String.valueOf(i + 1)) + "+");
                    z++;
                }
            }
            if(planTables.size()>0) {
                //添加管理站计划内容
                TableData tablePlan = new TableData(null, planParent.get(x).getPlanName(), String.valueOf(moneyPlan), planParent.get(x).getRemarks(), nameAll.length() > 0 ? nameAll.substring(0, nameAll.length() - 1) : "");
                tableDataList.add(tablePlan);
                //添加管理站对应工程和项目
                tableDataList.addAll(planTables);
            }
        }
        return new PageResult(tableDataList);
    }

    /**
     * 项目分配添加，计算
     * @param projects
     * @param j
     * @param planType
     * @param tableList
     * @return
     */
    private static double projectAllList(List<Project> projects, int j, String planType, List<TableData> tableList) {
        double money = 0;
        List<TableData> list = ListUtils.newArrayList();
        for (int i = 0; i < projects.size(); i++) {
            String sort = j+"."+(i+1);
            TableData tableData = new TableData(sort, projects.get(i).getProjectName(), projects.get(i).getProjectUnit(), String.valueOf(Double.valueOf(projects.get(i).getProjectQuantity()!=null?projects.get(i).getProjectQuantity():"0")), String.valueOf(Double.valueOf(projects.get(i).getProjectPrice()!=null?projects.get(i).getProjectPrice():"0")), String.valueOf(Double.valueOf(projects.get(i).getProjectPriceall()!=null?projects.get(i).getProjectPriceall():"0")));
            list.add(tableData);
            if (projects.get(i).getProjectPriceall() != null) {
                Double aDouble = Double.valueOf(projects.get(i).getProjectPriceall());
                money += aDouble;
            }
        }
        //当前项目类型类型有无项目
        if(list.size()>0){
            TableData tableData = new TableData(String.valueOf(j), planType, String.valueOf(money), null, null);
            tableList.add(tableData);
            tableList.addAll(list);
        }
        return money;
    }

    /**
     * 分局审核状态修改
     * @param id
     * @return
     */
    public void savePlanStatus(String id, String status) {
        subCentersPlanDao.savePlanStatus(id, status);
    }

    /**
     * 分局上报状态修改
     * @param id
     * @return
     */
    public void savePlanReport(String id, String status) {
        subCentersPlanDao.savePlanReport(id, status);
    }

}