package com.chinags.device.maintain.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.device.basic.dao.DeviceDao;
import com.chinags.device.maintain.dao.MaintainInfoDao;
import com.chinags.device.maintain.entity.MaintainInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XieWenqing
 * @date 2019/12/6 下午 4:43
 */
@Service
public class MaintainInfoService {
    @Autowired
    private MaintainInfoDao maintainInfoDao;
    @Autowired
    private DeviceDao deviceDao;

    /**
     * 保存养护记录
     */
    public void save(MaintainInfo maintainInfo) {
        maintainInfoDao.save(maintainInfo);
    }

    /**
     * 根据id查询养护记录
     */
    public MaintainInfo findById(String id) {
        return maintainInfoDao.selectById(id);
    }

    /**
     * 根据id删除安全工程档案
     */
    public void deleteById(String id) {
        maintainInfoDao.deleteById(id);
    }

    /**
     * 根据条件分页查询养护记录
     * @return
     */
    public PageResult<MaintainInfo> listDate(MaintainInfo maintainInfo) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(maintainInfo.getOrderBy())) {
            String orderBy = maintainInfo.getOrderBy().replaceAll("%20", ",");
            String[] str = orderBy.split(",");
            pageable = PageRequest.of(maintainInfo.getPageNo()==null?0:maintainInfo.getPageNo(), maintainInfo.getPageSize()==null?20: maintainInfo.getPageSize(), str[1].equals("desc")? Sort.Direction.DESC:Sort.Direction.ASC, str[0]);
        } else {
            if (maintainInfo.getPageNo() == null || maintainInfo.getPageSize() == null)
                pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"createDate");
            else
                pageable = PageRequest.of(maintainInfo.getPageNo(), maintainInfo.getPageSize(), Sort.Direction.DESC,"createDate");
        }

        Page<MaintainInfo> page = maintainInfoDao.findAll(new Specification<MaintainInfo>() {
            @Override
            public Predicate toPredicate(Root<MaintainInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotEmpty(maintainInfo.getDeceiveName())){
                    predicates.add(cb.like(root.get("deceiveName").as(String.class), "%" + maintainInfo.getDeceiveName() + "%"));
                }
                if(StringUtils.isNotEmpty(maintainInfo.getRemarks())){
                    predicates.add(cb.like(root.get("remarks").as(String.class), "%" + maintainInfo.getRemarks() + "%"));
                }
                //根据机构查询设备
                if(StringUtils.isNotEmpty(maintainInfo.getOrgId())){
                    //根据机构id查询设备idList
                    List<String> deceiveIds = deviceDao.selectByOrgId(maintainInfo.getOrgId());
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
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<MaintainInfo> result = PageResult.getPageResult(page);
        return result;
    }


    public MaintainInfo selectBymaitainId(String id) {
        return maintainInfoDao.selectBymaitainId(id);
    }

    public void updateBycount(String id,Integer count) {
        maintainInfoDao.updateBycount(id, count);
    }

    public List<MaintainInfo> listDataTo() {
        return maintainInfoDao.listDataTo();
    }

    public List<MaintainInfo> selectBymaintainDate(String startDate, String endDate) {
        return maintainInfoDao.selectBymaintainDate(startDate,endDate);
    }

    public List<MaintainInfo> selectBymaintainDateTo() {
        return maintainInfoDao.selectBymaintainDateTo();
    }

    public List<MaintainInfo> listDataToK() {
        return maintainInfoDao.listDataToK();
    }

    public Integer listCount(String id) {
        return maintainInfoDao.listCount(id);
    }
}
