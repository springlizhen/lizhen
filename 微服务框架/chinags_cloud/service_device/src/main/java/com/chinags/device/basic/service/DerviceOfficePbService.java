package com.chinags.device.basic.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;
import com.chinags.device.basic.dao.DerviceOfficePbDao;
import com.chinags.device.basic.entity.DerviceOfficePb;
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
public class DerviceOfficePbService extends BaseService<DerviceOfficePb> {

    @Autowired
    private DerviceOfficePbDao derviceOfficePbDao;

    /**
     * 获取左侧菜单栏
     * @param derviceOfficePb 查询对象
     * @return Office集合
     */
    public List<DerviceOfficePb> findAll(DerviceOfficePb derviceOfficePb){
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        Example<DerviceOfficePb> ex = Example.of(derviceOfficePb);
        return derviceOfficePbDao.findAll(ex, sort);
    }

    /**
     * 获得代码字段信息 by id
     * @param id
     * @return
     */
    public DerviceOfficePb getDerviceOfficePbById(String id){
        return derviceOfficePbDao.getDerviceOfficePbById(id);
    }

    /**
     * 查询全部列表
     * @return field分页数据
     */
    public PageResult find(DerviceOfficePb derviceOfficePb) {
        PageRequest pageable;
        if(derviceOfficePb.getOrderBy()==null|| "".equals(derviceOfficePb.getOrderBy())) {
            pageable = PageRequest.of(derviceOfficePb.getPageNo(), derviceOfficePb.getPageSize(), Sort.Direction.DESC,"updateDate");
        } else {
            pageable = PageRequest.of(derviceOfficePb.getPageNo(), derviceOfficePb.getPageSize(), derviceOfficePb.getDesc(), derviceOfficePb.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(derviceOfficePbDao.findAll(
                (Specification<DerviceOfficePb>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (derviceOfficePb.getName() !=null && !"".equals(derviceOfficePb.getName())) {
                        predicates.add(cb.like(root.get("name").as(String.class), "%" + derviceOfficePb.getName() + "%"));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), DerviceOfficePb.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 保存代码字段
     * @param derviceOfficePb 代码字段数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(DerviceOfficePb derviceOfficePb){
        derviceOfficePbDao.save(derviceOfficePb);
    }

    /**
     * 删除代码字段
     * @param id
     * @return
     */
    public DerviceOfficePb delete(String id) {
        DerviceOfficePb derviceOfficePb = derviceOfficePbDao.getDerviceOfficePbById(id);
        derviceOfficePb.setStatus(DerviceOfficePb.STATUS_DELETE);
        return derviceOfficePbDao.save(derviceOfficePb);
    }

}
