package com.chinags.device.check.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;
import com.chinags.device.check.dao.StandardDao;
import com.chinags.device.check.entity.Standard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
public class StandardService extends BaseService<Standard>{

    @Autowired
    private StandardDao standardDao;



    public void save(Standard standard){
         standardDao.save(standard);

    }
    public Standard selectStandard(String id){
        return standardDao.getStandardById(id);

    }



    public void delete(Standard standard ){
        standardDao.delete(standard);
    }

    public PageResult find(Standard standard) {
        PageRequest pageable;
        if(standard.getOrderBy()==null|| "".equals(standard.getOrderBy())) {
            pageable = PageRequest.of(standard.getPageNo(), standard.getPageSize(), Sort.Direction.ASC,"createDate");
        } else {
            pageable = PageRequest.of(standard.getPageNo(), standard.getPageSize(), standard.getDesc(), standard.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(standardDao.findAll(
                (Specification<Standard>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (standard.getId() !=null && !"".equals(standard.getId())) {
                        predicates.add(cb.like(root.get("id").as(String.class), "%" + standard.getId() + "%"));
                    }
                    if (standard.getStandardId() !=null && !"".equals(standard.getStandardId())) {
                        predicates.add(cb.like(root.get("standardId").as(String.class), "%" + standard.getStandardId() + "%"));
                    }
                    if (standard.getStandardName() !=null && !"".equals(standard.getStandardName())) {
                        predicates.add(cb.like(root.get("standardName").as(String.class), "%" + standard.getStandardName() + "%"));
                    }
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }


}
