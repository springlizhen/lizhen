package com.chinags.auth.service;

import com.chinags.auth.dao.DicDataDao;
import com.chinags.auth.entity.DicData;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
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
public class DicDataService {

    @Autowired
    private DicDataDao dicDataDao;


    /**
     *
     * @param dicData 查询对象
     * @return Office集合
     */
    public List<DicData> findAll(DicData dicData){
        Example<DicData> ex = Example.of(dicData);
        return dicDataDao.findAll(ex);
    }
    /**
     * 查询全部列表
     * @return
     */
    public PageResult<DicData> find(DicData dicType) {
        PageRequest pageable;
        if(dicType.getOrderBy()==null||dicType.getOrderBy().equals("")) {
            pageable = PageRequest.of(dicType.getPageNo(), dicType.getPageSize());
        } else {
            pageable = PageRequest.of(dicType.getPageNo(), dicType.getPageSize(), dicType.getDesc(), dicType.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        PageResult areas =  PageResult.getPageResult(dicDataDao.findAll(
                (Specification<DicData>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (dicType.getDictLabel() != null && !dicType.getDictLabel().equals("")) {
                        predicates.add(cb.like(root.get("dictLabel").as(String.class), "%" + dicType.getDictLabel() + "%"));
                    }
                    if (dicType.getDictValue() != null && !dicType.getDictValue().equals("")) {
                        predicates.add(cb.like(root.get("dictValue").as(String.class), "%" + dicType.getDictValue() + "%"));
                    }
                    if (dicType.getDictType() != null && !dicType.getDictType().equals("")) {
                        predicates.add(cb.equal(root.get("dictType").as(String.class), dicType.getDictType()));
                    }
                    if (dicType.getIsSys() != null && !dicType.getIsSys().equals("")) {
                        predicates.add(cb.equal(root.get("isSys").as(String.class), dicType.getIsSys()));
                    }
                    if (dicType.getStatus() != null && !dicType.getStatus().equals("")) {
                        predicates.add(cb.equal(root.get("status").as(String.class), dicType.getStatus()));
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
     * @param dicData
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(DicData dicData){
        DicData dataParent = getByDictCode(dicData.getParentCode());
        if(dataParent!=null) {
            dicData.setParentCodes(dataParent.getParentCodes() + dataParent.getDictCode() + ",");
            dicData.setTreeSorts(dataParent.getTreeSorts() + String.format("%10d", dicData.getTreeSort()).replace(" ", "0") + ",");
            dicData.setTreeNames(dataParent.getTreeNames() + "/" + dicData.getDictCode());
            if (dicData.getIsNewRecord()) {
                dicData.setTreeLeaf("1");
                dicData.setTreeLevel(dataParent.getTreeLevel() + 1);
                dicData.setStatus("0");
                dataParent.setTreeLeaf("0");
                dicDataDao.save(dataParent);
            }
        }else{
            dicData.setParentCode("0");
            dicData.setParentCodes("0,");
            dicData.setTreeSorts(String.format("%10d", dicData.getTreeSort()).replace(" ", "0") + ",");
            dicData.setTreeNames(dicData.getDictLabel());
            if (dicData.getIsNewRecord()) {
                dicData.setTreeLeaf("1");
                dicData.setTreeLevel(0);
                dicData.setStatus("0");
            }
        }
        if(!dicData.getIsNewRecord()){
            DicData d = this.getByDictCode(dicData.getDictCode());
            dicData.setTreeLeaf(d.getTreeLeaf());
            dicData.setTreeLevel(d.getTreeLevel());
            dicData.setStatus(d.getStatus());
        }
        dicDataDao.save(dicData);
    }

    /**
     * 删除字典
     * @param dicData
     */
    public Boolean delete(DicData dicData){
        DicData d = getByDictCode(dicData.getDictCode());
        d.setStatus(BaseEntity.STATUS_DELETE);
        dicDataDao.save(d);
        return true;
    }

    /**
     * 获得字典信息
     * @param dictCode
     * @return
     */
    public DicData getByDictCode(String dictCode){
        return dicDataDao.getByDictCode(dictCode);
    }

    /**
     * 获得字典信息
     * @param dictType
     * @return
     */
    public List<DicData> getAllByDictType(String dictType){
        return dicDataDao.getAllByDictTypeOrderByTreeSort(dictType);
    }

    /**
     * 获得字典信息
     * @param id
     * @return
     */
    public DicData getById(String id){
        return dicDataDao.getById(id);
    }

    /**
     * 停用字典
     * @param id
     */
    public Result disable(String id) {
        DicData dicData = dicDataDao.getByDictCode(id);
        dicData.setStatus("2");
        dicDataDao.save(dicData);
        return new Result(true, StatusCode.OK, "停用字典"+dicData.getDictLabel()+"成功");
    }

    /**
     * 启用区域
     * @param id
     */
    public String enable(String id) {
        DicData dicData = dicDataDao.getByDictCode(id);
        dicData.setStatus("0");
        dicDataDao.save(dicData);
        return "启用字典"+dicData.getDictLabel()+"成功";
    }
}
