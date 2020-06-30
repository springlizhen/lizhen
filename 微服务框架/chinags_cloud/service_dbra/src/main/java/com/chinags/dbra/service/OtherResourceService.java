package com.chinags.dbra.service;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.dbra.dao.OtherResourceDao;
import com.chinags.dbra.entity.OtherResource;
import com.chinags.dbra.entity.Resource;
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
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/17
 **/
@Service
public class OtherResourceService {
    @Autowired
    private OtherResourceDao otherResourceDao;

    /**
     * 分页查询所有的第三方服务
     * @param entity
     * @return
     */
    public PageResult<OtherResource> findAllByPage(BaseEntity entity, String serviceClass, String name) {
        PageRequest pageable;
        Sort sort;
        if (StringUtils.isNotEmpty(entity.getOrderBy())) {
            sort = new Sort(Sort.Direction.DESC,entity.getOrderBy());
        } else {
            sort = new Sort(Sort.Direction.DESC,"updateDate");
        }
        pageable = PageRequest.of(entity.getPageNo(), entity.getPageSize(), sort);
        Page<OtherResource> page = otherResourceDao.findAll(new Specification<OtherResource>() {
            @Override
            public Predicate toPredicate(Root<OtherResource> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotEmpty(serviceClass)) {
                    predicates.add(cb.equal(root.get("serviceClass").as(String.class), serviceClass));
                }
                if (StringUtils.isNotEmpty(name)) {
                    predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        return PageResult.getPageResult(page);
    }

    /**
     * 保存第三方服务
     * @param otherResource
     * @return
     */
    public OtherResource save(OtherResource otherResource) {
        return otherResourceDao.save(otherResource);
    }

    /**
     * 判断id是否存在
     */
    public boolean checkSave(String id) {
        return otherResourceDao.existsById(id);
    }

    /**
     * 根据id修改状态
     * @param status
     * @param id
     * @return
     */
    public void updateStatusById(String status, String id) {
        otherResourceDao.updateStatusById(status, id);
    }

    /**
     * 根据条件分页查询
     * @param otherResource
     * @return
     */
    public PageResult<OtherResource> findByEntity(OtherResource otherResource) {
        PageRequest pageable = PageRequest.of(otherResource.getPageNo(), otherResource.getPageSize());
        Page<OtherResource> page = otherResourceDao.findAll(new Specification<OtherResource>() {
            @Override
            public Predicate toPredicate(Root<OtherResource> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotEmpty(otherResource.getName())) {
                    predicates.add(cb.like(root.get("name"),"%" + otherResource.getName() + "%"));
                }
                if (StringUtils.isNotEmpty(otherResource.getServiceClass())) {
                    predicates.add(cb.equal(root.get("serviceClass").as(String.class), otherResource.getServiceClass()));
                }
                if (StringUtils.isNotEmpty(otherResource.getAccessType())) {
                    predicates.add(cb.equal(root.get("accessType").as(String.class), otherResource.getAccessType()));
                }
                if (StringUtils.isNotEmpty(otherResource.getStatus())) {
                    predicates.add(cb.equal(root.get("status").as(String.class), otherResource.getStatus()));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<OtherResource> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 获取去重的状态
     * @return
     */
    public List<String> getStatus() {
        return otherResourceDao.getStatus();
    }

    /**
     * 获取去重的服务分类
     * @return
     */
    public List<String> getServiceClasses() {
        return otherResourceDao.getServiceClasses();
    }

    /**
     * 获取去重的接入类型
     * @return
     */
    public List<String> getAccessType() {
        return otherResourceDao.getAccessTypes();
    }

    /**
     * 根据id查询第三方服务
     * @param id
     * @return
     */
    public OtherResource getById(String id) {
        return otherResourceDao.getById(id);
    }

    /**
     * 根据id删除第三方服务
     * @param id
     */
    public void deleteById(String id) {
        otherResourceDao.deleteById(id);
    }

    /**
     * 增加接口调用次数
     * @param id
     */
    public void addCallNum(String id) {
        otherResourceDao.addCallNum(id);
    }

    /**
     * 增加应用连接数
     * @param id
     */
    public void addConnNum(String id) {
        otherResourceDao.addConnNum(id);
    }

}
