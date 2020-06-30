package com.chinags.device.basic.service;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.service.BaseService;
import com.chinags.device.basic.dao.SensorTypeDao;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.entity.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorTypeService extends BaseService<SensorType> {

    @Autowired
    private SensorTypeDao sensorTypeDao;


    /**
     *
     * @param sensorType 查询对象
     * @return Office集合
     */
    public List<SensorType> findAll(SensorType sensorType){
        Example<SensorType> ex = Example.of(sensorType);
        return sensorTypeDao.findAll(ex);
    }
    /**
     * 查询全部列表
     * @return
     */
    public PageResult<SensorType> find(SensorType sensorType) {
        PageRequest pageable;
        pageable = PageRequest.of(sensorType.getPageNo(), sensorType.getPageSize(), sensorType.getDesc(), "treeLevel","sort");
        //数据简单转换，转换为前台框架所需要分页json
        PageResult areas =  PageResult.getPageResult(sensorTypeDao.findAll(
                (Specification<SensorType>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (sensorType.getName() != null && !sensorType.getName().equals("")) {
                        predicates.add(cb.like(root.get("name").as(String.class), "%" + sensorType.getName() + "%"));
                    }
                    if (sensorType.getCode() != null && !sensorType.getCode().equals("")) {
                        predicates.add(cb.like(root.get("code").as(String.class), "%" + sensorType.getCode() + "%"));
                    }
                    if (sensorType.getParentCode() != null && !sensorType.getParentCode().equals("")) {
                        predicates.add(cb.equal(root.get("parentCode").as(String.class), sensorType.getParentCode()));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), BaseEntity.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
        return areas;
    }

    /**
     * 保存类型
     * @param sensorType
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(SensorType sensorType){
        sensorType.setTreeSort(Integer.parseInt(sensorType.getSort()));
        SensorType officeParent = getById(sensorType.getParentCode());
        if(officeParent!=null) {
            sensorType.setParentCodes(officeParent.getParentCodes() + officeParent.getId() + ",");
            sensorType.setTreeSorts(officeParent.getTreeSorts() + String.format("%10d", sensorType.getTreeSort()).replace(" ", "0") + ",");
            sensorType.setTreeNames(officeParent.getTreeNames() + "/" + sensorType.getName());
            if (sensorType.getIsNewRecord()) {
                sensorType.setStatus("0");
            }
            sensorType.setTreeLeaf("1");
            sensorType.setTreeLevel(officeParent.getTreeLevel() + 1);
            officeParent.setTreeLeaf("0");
            sensorTypeDao.save(officeParent);
        }else{
            sensorType.setParentCode("0");
            sensorType.setParentCodes("0,");
            sensorType.setTreeSorts(String.format("%10d", sensorType.getTreeSort()).replace(" ", "0") + ",");
            sensorType.setTreeNames(sensorType.getName());
            sensorType.setTreeLeaf("1");
            sensorType.setTreeLevel(0);
            if (sensorType.getIsNewRecord()) {
                sensorType.setStatus("0");
            }
        }
        String parentCode = null;
        if(!sensorType.getIsNewRecord()){
            SensorType o = this.getById(sensorType.getId());
            sensorType.setStatus(o.getStatus());
            parentCode = o.getParentCode();
        }
        sensorTypeDao.save(sensorType);

        if(parentCode!=null) {
            menuleav(parentCode);
        }

    }

    /**
     * 重置树形结构下级
     * @param id
     */
    private void menuleav(String id){
        Integer count = sensorTypeDao.getStopParentCodesCount(id);
        if(count==0){
            sensorTypeDao.updateApplyNumById(id);
        }
    }

    /**
     * 删除类型
     * @param sensorType
     */
    public Boolean delete(SensorType sensorType){
        SensorType d = getById(sensorType.getId());
        d.setStatus(BaseEntity.STATUS_DELETE);
        sensorTypeDao.save(d);
        return true;
    }

    /**
     * 获得类型信息
     * @param sensorType
     * @return
     */
    public SensorType getByCode(String sensorType){
        return sensorTypeDao.getByCode(sensorType);
    }

    /**
     * 获得类型信息
     * @param id
     * @return
     */
    public SensorType getById(String id){
        return sensorTypeDao.getById(id);
    }

    /**
     * 获取树形结构
     * @param excludeCode 机构code
     * @return Office集合
     */
    public List<SensorType> treeData(String excludeCode){
        return sensorTypeDao.findAll((Specification<SensorType>) (root, query, cb) -> getPredicate(excludeCode, query, cb, root.get("parentCodes"), root.get("id"), root.get("status"), root.get("treeLevel"), root.get("treeSort"), root));
    }
}
