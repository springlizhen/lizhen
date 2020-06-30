package com.chinags.auth.service;

import com.chinags.auth.dao.CommDao;
import com.chinags.auth.entity.Comm;
import com.chinags.common.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommService {
    @Autowired
    private CommDao commDao;

    /**
     * 查询全部列表
     * @return
     */
    public PageResult<Comm> findAll() {
        return PageResult.getPageResult(commDao.findAll());
    }

    /**
     * 查询全部列表
     * @return
     */
    public List<Comm> findAll(Comm comm){
        Example<Comm> ex = Example.of(comm);
        return commDao.findAll(ex);
    }

    /**
     * 查询全部列表
     * @return
     */
    public PageResult<Comm> find(Comm comm) {
        PageRequest pageable;
        if(comm.getOrderBy()==null||comm.getOrderBy().equals("")) {
            pageable = PageRequest.of(comm.getPageNo(), comm.getPageSize());
        } else{
            pageable = PageRequest.of(comm.getPageNo(), comm.getPageSize(), comm.getDesc(), comm.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        PageResult<Comm> comms =  PageResult.getPageResult(commDao.findAll(
                new Specification<Comm>() {
                    @Override
                    public Predicate toPredicate(Root<Comm> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        List<Predicate> predicates = new ArrayList<>();
                        if (comm.getAuthName() != null && !comm.getAuthName().equals("")) {
                            predicates.add(cb.like(root.get("authName").as(String.class), "%" + comm.getAuthName() + "%"));
                        }
                        if (comm.getAuthCode() != null && !comm.getAuthCode().equals("")) {
                            predicates.add(cb.like(root.get("authCode").as(String.class), "%" + comm.getAuthCode() + "%"));
                        }

                        Predicate[] pre = new Predicate[predicates.size()];
                        query.where(predicates.toArray(pre));
                        return cb.and(predicates.toArray(pre));
                    }
                },pageable));
        return comms;
    }

    /**
     * 获得用户信息 by loginCode
     * @param id
     * @return
     */
    public Comm getCommById(String id){
        return commDao.getCommById(id);
    }

    /**
     * 获得用户信息 by loginCode
     * @param authCode
     * @return
     */
    public Comm getCommByAuthCode(String authCode){
        return commDao.getCommByAuthCode(authCode);
    }

    public void save(Comm comm) {
        commDao.save(comm);
    }

    public void delete(String id) {
        commDao.deleteById(id);
    }
}
