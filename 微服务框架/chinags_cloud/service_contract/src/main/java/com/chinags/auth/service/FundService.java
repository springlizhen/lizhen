package com.chinags.auth.service;

import com.chinags.auth.dao.FileDao;
import com.chinags.auth.dao.FundDao;
import com.chinags.auth.entity.File;

import com.chinags.auth.entity.Fund;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FundService {
    @Autowired
    private FundDao fundDao;

    public Result saveFund(Fund fund) {
        //Boolean scontract = fundDao.existsById(contract.getId());
        //Contract selectContract = fundDao.getContractById(contract.getId());

        //if (!scontract) {
            fundDao.save(fund);
            return new Result(true, StatusCode.OK, "保存成功");

        }
    public Fund selectFundById(String id){
        return fundDao.selectFundById(id);

    }
    public void deleteFundById(String uuid){
        fundDao.deleteFundById(uuid);

    }








    public PageResult<Fund> selectFile(Fund fund){
        PageRequest pageable;
        if(fund.getOrderBy()==null||fund.getOrderBy().equals("")){
            pageable = PageRequest.of(fund.getPageNo(), fund.getPageSize());
        }else{
            pageable = PageRequest.of(fund.getPageNo(), fund.getPageSize(), fund.getDesc(), fund.getOrderBy());
        }
        Page<Fund> list=  fundDao.findAll(new Specification<Fund>() {
            @Override
            public Predicate toPredicate(Root<Fund> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (fund.getId()!= null ) {
                    predicates.add(cb.like(root.get("id").as(String.class),  fund.getId() ));
                }

                if (fund.getFund() != null && !fund.getFund().equals("")) {
                    predicates.add(cb.like(root.get("fund").as(String.class), "%" + fund.getFund() + "%"));
                }

                if (fund.getFundType() != null && !fund.getFundType().equals("")) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("fundType").as(String.class),  fund.getFundType()));
                }

                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        },pageable);
        PageResult<Fund> result=PageResult.getPageResult(list);

        return result;

    }




}
