package com.chinags.layer.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;
import com.chinags.layer.dao.SgeLayerApprovalDao;
import com.chinags.layer.dao.SgeLayerDao;
import com.chinags.layer.entity.SgeLayer;
import com.chinags.layer.entity.SgeLayerApproval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SgeLayerApprovalService extends BaseService<SgeLayer>{

    @Autowired
    private SgeLayerApprovalDao sgeLayerApprovalDao;

    @Autowired
    private SgeLayerDao sgeLayerDao;

    /**
     * 申请保存
     * @param sgeLayerApproval
     */
    public void save(SgeLayerApproval sgeLayerApproval){
        String layerName = sgeLayerDao.getName(sgeLayerApproval.getLayerId());
        sgeLayerApproval.setLayerName(layerName);
        sgeLayerApprovalDao.save(sgeLayerApproval);
    }

    /**
     * 读取
     * @param id
     * @return
     */
    public SgeLayerApproval getSgeLayer(String id){
        return sgeLayerApprovalDao.getSgeLayerApprovalById(id);

    }

    /**
     * 删除
     * @param sgeLayerApproval
     */
    public void delete(SgeLayerApproval sgeLayerApproval){
        sgeLayerApprovalDao.delete(sgeLayerApproval);
    }

    public PageResult findList(SgeLayerApproval sgeLayerApproval) {
        PageRequest pageable;
        if(sgeLayerApproval.getOrderBy()==null|| "".equals(sgeLayerApproval.getOrderBy())) {
            pageable = PageRequest.of(sgeLayerApproval.getPageNo(), sgeLayerApproval.getPageSize(), Sort.Direction.ASC,"createDate");
        } else {
            pageable = PageRequest.of(sgeLayerApproval.getPageNo(), sgeLayerApproval.getPageSize(), sgeLayerApproval.getDesc(), sgeLayerApproval.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(sgeLayerApprovalDao.findAll(
                (Specification<SgeLayerApproval>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (sgeLayerApproval.getLayerName() !=null && !"".equals(sgeLayerApproval.getLayerName())) {
                        predicates.add(cb.like(root.get("layerName").as(String.class), "%" + sgeLayerApproval.getId() + "%"));
                    }
                    if (sgeLayerApproval.getLayerId() !=null && !"".equals(sgeLayerApproval.getLayerId())) {
                        predicates.add(cb.equal(root.get("layerId").as(String.class), sgeLayerApproval.getLayerId()));
                    }
                    if (sgeLayerApproval.getUseStatus() !=null && !"".equals(sgeLayerApproval.getUseStatus())) {
                        predicates.add(cb.like(root.get("userStatus").as(String.class), "%" + sgeLayerApproval.getUseStatus() + "%"));
                    }
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }


}
