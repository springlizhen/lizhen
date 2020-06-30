package com.chinags.common.service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class BaseService<T> {
     protected Predicate getPredicate(String excludeCode, CriteriaQuery<?> query, CriteriaBuilder cb, Path<Object> parentCodes, Path<Object> id, Path<Object> status, Path<Object> treeLevel, Path<Object> treeSort, Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (excludeCode != null && !"".equals(excludeCode)) {
            predicates.add(cb.notLike(parentCodes.as(String.class), "%" + excludeCode + ",%"));
            predicates.add(cb.notEqual(id.as(String.class), excludeCode));
        }
        predicates.add(cb.equal(status.as(String.class), 0));
        Predicate[] pre = new Predicate[predicates.size()];
        query.where(predicates.toArray(pre));
        query.orderBy(cb.asc(treeLevel),cb.asc(treeSort));
        return cb.and(predicates.toArray(pre));
    }
}
