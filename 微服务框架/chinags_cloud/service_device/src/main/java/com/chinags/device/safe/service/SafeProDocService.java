package com.chinags.device.safe.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.device.safe.dao.SafeProDocDao;
import com.chinags.device.safe.entity.SafeProDoc;
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
 * @author XieWenqing
 * @date 2019/12/5 上午 10:45
 */
@Service
public class SafeProDocService {
    @Autowired
    private SafeProDocDao safeProDocDao;

    /**
     * 保存安全工程档案
     */
    public void save(SafeProDoc safeProDoc) {
        safeProDocDao.save(safeProDoc);
    }

    /**
     * 根据id查询安全工程档案
     */
    public SafeProDoc findById(String id) {
        return safeProDocDao.selectById(id);
    }

    /**
     * 根据id删除安全工程档案
     */
    public void deleteById(String id) {
        safeProDocDao.deleteById(id);
    }

    /**
     * 根据id查询是否有该条记录，0否1是
     */
    public Integer count (String id){
        return safeProDocDao.count(id);
    }

    /**
     * 根据条件分页查询安全工程档案
     * @return
     */
    public PageResult<SafeProDoc> listDate(SafeProDoc safeProDoc) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(safeProDoc.getOrderBy())) {
            String orderBy = safeProDoc.getOrderBy().replaceAll("%20", ",");
            String[] str = orderBy.split(",");
            pageable = PageRequest.of(safeProDoc.getPageNo()==null?0:safeProDoc.getPageNo(), safeProDoc.getPageSize()==null?20: safeProDoc.getPageSize(), str[1].equals("desc")?Sort.Direction.DESC:Sort.Direction.ASC, str[0]);
        } else {
            if (safeProDoc.getPageNo() == null || safeProDoc.getPageSize() == null)
                pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"createDate");
            else
                pageable = PageRequest.of(safeProDoc.getPageNo(), safeProDoc.getPageSize(), Sort.Direction.DESC,"createDate");
        }
        Page<SafeProDoc> page = safeProDocDao.findAll(new Specification<SafeProDoc>() {
            @Override
            public Predicate toPredicate(Root<SafeProDoc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotEmpty(safeProDoc.getName())){
                    predicates.add(cb.like(root.get("name").as(String.class), "%" + safeProDoc.getName() + "%"));
                }

                if(StringUtils.isNotEmpty(safeProDoc.getRemarks())){
                    predicates.add(cb.like(root.get("remarks").as(String.class), "%" + safeProDoc.getRemarks() + "%"));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<SafeProDoc> result = PageResult.getPageResult(page);
        return result;
    }


}
