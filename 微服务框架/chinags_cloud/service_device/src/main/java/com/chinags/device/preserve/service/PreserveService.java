package com.chinags.device.preserve.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.device.maintain.entity.MaintainInfo;
import com.chinags.device.preserve.dao.PreserveDao;
import com.chinags.device.preserve.entity.Preserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lizhen
 * @date 2020/3/3
 */
@Service
public class PreserveService {
    @Autowired
    private PreserveDao preserveDao;
    public PageResult<Preserve> listData(Preserve preserve) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(preserve.getOrderBy())) {
            String orderBy = preserve.getOrderBy().replaceAll("%20", ",");
            String[] str = orderBy.split(",");
            pageable = PageRequest.of(preserve.getPageNo()==null?0:preserve.getPageNo(), preserve.getPageSize()==null?20: preserve.getPageSize(), str[1].equals("desc")? Sort.Direction.DESC:Sort.Direction.ASC, str[0]);
        } else {
            if (preserve.getPageNo() == null || preserve.getPageSize() == null)
                pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"createDate");
            else
                pageable = PageRequest.of(preserve.getPageNo(), preserve.getPageSize(), Sort.Direction.DESC,"createDate");
        }

        Page<Preserve> page = preserveDao.findAll(new Specification<Preserve>() {
            @Override
            public Predicate toPredicate(Root<Preserve> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //根据养护记录查询维护记录
                if(StringUtils.isNotEmpty(preserve.getMaintainId())){
                    List<String> deceiveIds = preserveDao.selectByMaintainId(preserve.getMaintainId());
                    Path<Object> path = root.get("maintainId");
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    if(deceiveIds.size()>0){  //当deceiveIds大于0时，再进行查询，否则报错
                        for(String deceiveId: deceiveIds){
                            in.value(deceiveId);
                        }
                    }else{  //当deceiveIds小于0时，传入一个空串，防止报错
                        in.value("");
                    }
                    predicates.add(cb.and(in));

                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<Preserve> result = PageResult.getPageResult(page);
        return result;
    }

    public List<Preserve> findByScheduleId(String id) {
        return preserveDao.findByScheduleId(id);
    }

    public void save(Preserve preserve) {
        preserveDao.save(preserve);
    }

    public List<Preserve> selectCount() {
        return  preserveDao.selectCount();
    }
}
