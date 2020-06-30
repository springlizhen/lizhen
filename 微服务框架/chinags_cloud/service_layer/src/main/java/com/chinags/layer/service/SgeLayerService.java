package com.chinags.layer.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;
import com.chinags.layer.dao.SgeLayerDao;
import com.chinags.layer.entity.SgeLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
public class SgeLayerService extends BaseService<SgeLayer>{

    @Autowired
    private SgeLayerDao sgeLayerDao;

    /**
     * 保存
     * @param sgeLayer
     */
    public void save(SgeLayer sgeLayer){
        sgeLayerDao.save(sgeLayer);

    }

    /**
     * 读取
     * @param id
     * @return
     */
    public SgeLayer getSgeLayer(String id){
        return sgeLayerDao.getSgeLayerById(id);

    }

    /**
     * 删除
     * @param sgeLayer
     */
    public void delete(SgeLayer sgeLayer){
        sgeLayerDao.delete(sgeLayer);
    }

    public PageResult findList(SgeLayer sgeLayer) {
        PageRequest pageable;
        if(sgeLayer.getOrderBy()==null|| "".equals(sgeLayer.getOrderBy())) {
            pageable = PageRequest.of(sgeLayer.getPageNo(), sgeLayer.getPageSize(), Sort.Direction.ASC,"createDate");
        } else {
            pageable = PageRequest.of(sgeLayer.getPageNo(), sgeLayer.getPageSize(), sgeLayer.getDesc(), sgeLayer.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(sgeLayerDao.findAll(
                (Specification<SgeLayer>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (sgeLayer.getName() !=null && !"".equals(sgeLayer.getName())) {
                        predicates.add(cb.like(root.get("name").as(String.class), "%" + sgeLayer.getId() + "%"));
                    }
                    if (sgeLayer.getType() !=null && !"".equals(sgeLayer.getType())) {
                        predicates.add(cb.like(root.get("type").as(String.class), "%" + sgeLayer.getType() + "%"));
                    }
                    if (sgeLayer.getUrl() !=null && !"".equals(sgeLayer.getUrl())) {
                        predicates.add(cb.like(root.get("url").as(String.class), "%" + sgeLayer.getUrl() + "%"));
                    }
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }


}
