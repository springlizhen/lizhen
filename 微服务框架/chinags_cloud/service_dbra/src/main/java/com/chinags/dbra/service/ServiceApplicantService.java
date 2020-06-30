package com.chinags.dbra.service;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.dbra.dao.ServiceApplicantDao;
import com.chinags.dbra.entity.ServiceApplicant;
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
import java.util.Date;
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/22
 **/
@Service
public class ServiceApplicantService {

    @Autowired
    private ServiceApplicantDao serviceApplicantDao;

    /**
     * 根据条件分页查询所有的注册服务
     * @param serviceApplicant
     * @return
     */
    public PageResult<ServiceApplicant> findByPage(ServiceApplicant serviceApplicant) {
        Sort sort = new Sort(Sort.Direction.DESC,"updateDate");
        PageRequest pageable = PageRequest.of(serviceApplicant.getPageNo(), serviceApplicant.getPageSize(), sort);
        Page<ServiceApplicant> page = serviceApplicantDao.findAll(new Specification<ServiceApplicant>() {
            @Override
            public Predicate toPredicate(Root<ServiceApplicant> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (com.chinags.common.lang.StringUtils.isNotEmpty(serviceApplicant.getName())) {
                    predicates.add(cb.like(root.get("name"),"%" + serviceApplicant.getName() + "%"));
                }
                if (com.chinags.common.lang.StringUtils.isNotEmpty(serviceApplicant.getStatus())) {
                    predicates.add(cb.like(root.get("status").as(String.class), "%" + serviceApplicant.getStatus() + "%"));
                }
                if (com.chinags.common.lang.StringUtils.isNotEmpty(serviceApplicant.getApplicant())) {
                    predicates.add(cb.like(root.get("applicant").as(String.class), "%" + serviceApplicant.getApplicant() + "%"));
                }
                if (com.chinags.common.lang.StringUtils.isNotEmpty(serviceApplicant.getCreateBy())) {
                    predicates.add(cb.like(root.get("createBy").as(String.class), "%" + serviceApplicant.getCreateBy() + "%"));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<ServiceApplicant> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 根据id修改状态
     * @param id
     * @param status
     */
    public void updateStatusById(String id, String status, String updateBy, Date updateDate) {
        serviceApplicantDao.updateStatusById(id, status, updateBy, updateDate);
    }

    /**
     * 根据createBy和resourceId查询服务注册
     * @param createBy
     * @param resourceId
     * @return
     */
    public ServiceApplicant findByCreateByAndResourceId(String createBy, String resourceId) {
        return serviceApplicantDao.findByCreateByAndResourceId(createBy, resourceId);
    }

    /**
     * 根据id查询服务注册信息
     * @param id
     * @return
     */
    public ServiceApplicant findById(String id) {
        return serviceApplicantDao.getById(id);
    }

    /**
     * 保存服务注册信息
     * @param serviceApplicant
     * @return
     */
    public ServiceApplicant save(ServiceApplicant serviceApplicant) {
        return serviceApplicantDao.save(serviceApplicant);
    }

    /**
     * 用户查询自己的申请服务
     * @param createBy
     * @return
     */
    public List<ServiceApplicant> findByUser(String createBy) {
        return serviceApplicantDao.findByUser(createBy);
    }

    /**
     * 根据分类查询申请服务
     * @param type
     * @return
     */
    public PageResult<ServiceApplicant> findByType(BaseEntity entity, String type) {
        Sort sort = new Sort(Sort.Direction.DESC,"updateDate");
        PageRequest pageable = PageRequest.of(entity.getPageNo(), entity.getPageSize(), sort);
        Page<ServiceApplicant> page = serviceApplicantDao.findAll(new Specification<ServiceApplicant>() {
            @Override
            public Predicate toPredicate(Root<ServiceApplicant> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (com.chinags.common.lang.StringUtils.isNotEmpty(type)) {
                    predicates.add(cb.equal(root.get("type"), type));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<ServiceApplicant> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 根据用户和分类查询审核通过的服务信息
     * @param createBy
     * @param type
     * @return
     */
    public List<ServiceApplicant> findByUserAndType(String createBy, String type) {
        return serviceApplicantDao.findByUserAndType(createBy, type);
    }

}
