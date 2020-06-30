package com.chinags.auth.service;

import com.chinags.auth.dao.DicTypeDao;
import com.chinags.auth.entity.DicType;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DicTypeService {

    @Autowired
    private DicTypeDao dicTypeDao;

    /**
     * 获取字典
     * @param dicType 字典实体
     * @return Area集合
     */
    public List<DicType> findAll(DicType dicType){
        //菜单状态 正常
        dicType.setStatus("0");
        Sort sort = new Sort(Sort.Direction.ASC, "dictType","updateDate");
        Example<DicType> ex = Example.of(dicType);
        return dicTypeDao.findAll(ex, sort);
    }

    /**
     * 查询全部列表
     * @return
     */
    public PageResult<DicType> find(DicType dicType) {
        PageRequest pageable;
        if(dicType.getOrderBy()==null||dicType.getOrderBy().equals("")) {
            pageable = PageRequest.of(dicType.getPageNo(), dicType.getPageSize());
        } else {
            pageable = PageRequest.of(dicType.getPageNo(), dicType.getPageSize(), dicType.getDesc(), dicType.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        PageResult areas =  PageResult.getPageResult(dicTypeDao.findAll(
                (Specification<DicType>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (dicType.getDictName() != null && !dicType.getDictName().equals("")) {
                        predicates.add(cb.like(root.get("dictName").as(String.class), "%" + dicType.getDictName() + "%"));
                    }
                    if (dicType.getDictType() != null && !dicType.getDictType().equals("")) {
                        predicates.add(cb.like(root.get("dictType").as(String.class), "%" + dicType.getDictType() + "%"));
                    }
                    if (dicType.getIsSys() != null && !dicType.getIsSys().equals("")) {
                        predicates.add(cb.equal(root.get("isSys").as(String.class), dicType.getIsSys()));
                    }
                    if (dicType.getStatus() != null && !dicType.getStatus().equals("")) {
                        predicates.add(cb.equal(root.get("status").as(String.class), dicType.getStatus()));
                    }
                    if (dicType.getSysCode() != null && !dicType.getSysCode().equals("")) {
                        predicates.add(cb.equal(root.get("sysCode").as(String.class), dicType.getSysCode()));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), BaseEntity.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
        return areas;
    }

    /**
     * 保存字典
     * @param dicType
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(DicType dicType){
        dicTypeDao.save(dicType);
    }

    /**
     * 删除字典
     * @param dicType
     */
    public Boolean delete(DicType dicType){
        DicType d = getById(dicType.getId());
        d.setStatus(BaseEntity.STATUS_DELETE);
        dicTypeDao.save(d);
        return true;
    }

    /**
     * 获得字典信息
     * @param id
     * @return
     */
    public DicType getById(String id){
        return dicTypeDao.getById(id);
    }

    /**
     * 获得字典信息
     * @param dicType
     * @return
     */
    public DicType getByDictType(String dicType){
        return dicTypeDao.getByDictType(dicType);
    }

    /**
     * 停用字典
     * @param id
     */
    public Result disable(String id) {
        DicType dicType = dicTypeDao.getById(id);
        dicType.setStatus("2");
        dicTypeDao.save(dicType);
        return new Result(true, StatusCode.OK, "停用字典"+dicType.getDictName()+"成功");
    }

    /**
     * 启用区域
     * @param id
     */
    public String enable(String id) {
        DicType dicType = dicTypeDao.getById(id);
        dicType.setStatus("0");
        dicTypeDao.save(dicType);
        return "启用字典"+dicType.getDictName()+"成功";
    }
}
