package com.chinags.device.threshold.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.device.basic.dao.DeviceDao;
import com.chinags.device.threshold.dao.ThresholdValDao;
import com.chinags.device.threshold.entity.ThresholdVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author XieWenqing
 * @date 2019/12/10 上午 11:31
 */
@Service
public class ThresholdValService {
    @Autowired
    private ThresholdValDao thresholdValDao;
    @Autowired
    private DeviceDao deviceDao;

    /**
     * 根据id查询工程安全阈值设置信息
     */
    public ThresholdVal findById(String id) {
        return thresholdValDao.selectById(id);
    }

    /**
     * 根据设备id查询工程安全阈值设置信息
     */
    public ThresholdVal selectByDeceiveId(String deceiveId) {
        return thresholdValDao.selectByDeceiveId(deceiveId);
    }

    /**
     * 更改安全阈值使用状态
     */
    public void updateStatusById(String id, String status, String updateBy, Date updateDate){
        thresholdValDao.updateStatusById(id, status, updateBy, updateDate);
    }

    /**
     * 保存工程安全阈值设置信息
     */
    public void save(ThresholdVal thresholdVal) {
        thresholdValDao.save(thresholdVal);
    }

    /**
     * 根据条件分页查询养护记录
     * @return
     */
    public PageResult<ThresholdVal> listDate(ThresholdVal thresholdVal) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(thresholdVal.getOrderBy())) {
            String orderBy = thresholdVal.getOrderBy().replaceAll("%20", ",");
            String[] str = orderBy.split(",");
            pageable = PageRequest.of(thresholdVal.getPageNo()==null?0:thresholdVal.getPageNo()-1, thresholdVal.getPageSize()==null?20: thresholdVal.getPageSize(), str[1].equals("desc")? Sort.Direction.DESC:Sort.Direction.ASC, str[0]);
        } else {
            if (thresholdVal.getPageNo() == null || thresholdVal.getPageSize() == null)
                pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"createDate");
            else
                pageable = PageRequest.of(thresholdVal.getPageNo()-1, thresholdVal.getPageSize(), Sort.Direction.DESC,"createDate");
        }
        Page<ThresholdVal> page = thresholdValDao.findAll(new Specification<ThresholdVal>() {
            @Override
            public Predicate toPredicate(Root<ThresholdVal> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //只展示isShow值为1的数据
                predicates.add(cb.equal(root.get("isShow"), "1"));
                if(StringUtils.isNotEmpty(thresholdVal.getMeasurePoint())){
                    predicates.add(cb.equal(root.get("measurePoint"), thresholdVal.getMeasurePoint()));
                }
                if(StringUtils.isNotEmpty(thresholdVal.getDeceiveCode())){
                    predicates.add(cb.like(root.get("deceiveCode").as(String.class), "%" + thresholdVal.getDeceiveCode() + "%"));
                }
                if(StringUtils.isNotEmpty(thresholdVal.getRemarks())){
                    predicates.add(cb.like(root.get("remarks").as(String.class), "%" + thresholdVal.getRemarks() + "%"));
                }
                //根据机构查询设备
                if(StringUtils.isNotEmpty(thresholdVal.getOrgId())){
                    //根据机构id查询设备idList
                    List<String> deceiveIds = deviceDao.selectByOrgId(thresholdVal.getOrgId());
                    Path<Object> path = root.get("deceiveId");
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
                //检查是否有
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<ThresholdVal> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 更新安全阈值信息，是否可以在列表中显示
     * @param isShow 是否显示，0否1是
     * @param deceiveId 设备id
     */
    public void updateIsShowByDeceiveId(String isShow, String deceiveId){
        thresholdValDao.updateIsShowByDeceiveId(isShow, deceiveId);
    }

}
