package com.chinags.device.check.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;

import com.chinags.device.check.dao.CheckDao;
import com.chinags.device.check.entity.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
public class CheckService extends BaseService<Check>{

    @Autowired
    private CheckDao checkDao;



    public void save(Check check){
        checkDao.save(check);

    }
    public Check selectCheck(String id){
        return checkDao.getCheckById(id);

    }



    public void delete(Check check ){
        checkDao.delete(check);
    }

    public PageResult find(Check check) {
        PageRequest pageable;
        if(check.getOrderBy()==null|| "".equals(check.getOrderBy())) {
            pageable = PageRequest.of(check.getPageNo(), check.getPageSize(), Sort.Direction.DESC,"createDate");
        } else {
            pageable = PageRequest.of(check.getPageNo(), check.getPageSize(), check.getDesc(), check.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(checkDao.findAll(
                (Specification<Check>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (check.getId() !=null && !"".equals(check.getId())) {
                        predicates.add(cb.like(root.get("id").as(String.class), "%" + check.getId() + "%"));
                }
        if (check.getCheckId() !=null && !"".equals(check.getCheckId())) {
            predicates.add(cb.like(root.get("checkId").as(String.class), "%" + check.getCheckId() + "%"));
        }
        if (check.getCheckName() !=null && !"".equals(check.getCheckName())) {
            predicates.add(cb.like(root.get("checkName").as(String.class), "%" + check.getCheckName() + "%"));
        }

        if (check.getId() !=null && !"".equals(check.getId())) {
            predicates.add(cb.like(root.get("id").as(String.class), "%" + check.getId() + "%"));
        }
        if (check.getCheckCenter() !=null && !"".equals(check.getCheckCenter())) {
            predicates.add(cb.like(root.get("checkCenter").as(String.class), "%" + check.getCheckCenter() + "%"));
        }
        if (check.getManageStation() !=null && !"".equals(check.getManageStation())) {
            predicates.add(cb.like(root.get("manageStation").as(String.class), "%" + check.getManageStation() + "%"));
        }
        if (check.getManageOffice() !=null && !"".equals(check.getId())) {
            predicates.add(cb.like(root.get("manageOffice").as(String.class), "%" + check.getManageOffice() + "%"));
        }
        if (check.getLongitude() !=null && !"".equals(check.getLongitude())) {
            predicates.add(cb.like(root.get("longitude").as(String.class), "%" + check.getLongitude() + "%"));
        }
        if (check.getLatitude() !=null && !"".equals(check.getLatitude())) {
            predicates.add(cb.like(root.get("latitude").as(String.class), "%" + check.getLatitude() + "%"));
        }


                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }


}
