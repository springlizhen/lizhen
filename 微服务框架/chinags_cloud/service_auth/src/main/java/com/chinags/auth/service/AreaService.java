package com.chinags.auth.service;

import com.chinags.auth.dao.AreaDao;
import com.chinags.auth.entity.Area;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.service.BaseService;
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

/**
 * @author Zhang
 */
@Service
public class AreaService extends BaseService<Area>{

    @Autowired
    private AreaDao areaDao;

    /**
     * 获取左侧菜单栏
     * @param area 区域实体
     * @return Area集合
     */
    public List<Area> findAll(Area area){
        //菜单状态 正常
        area.setStatus("0");
        Sort sort = new Sort(Sort.Direction.ASC, "treeLevel","treeSort");
        Example<Area> ex = Example.of(area);
        return areaDao.findAll(ex, sort);
    }

    /**
     * 获取树形结构
     * @param excludeCode 区域code
     * @return Area集合
     */
    public List<Area> treeData(String excludeCode){
        return areaDao.findAll((Specification<Area>) (root, query, cb) -> {
            return getPredicate(excludeCode, query, cb, root.get("parentCodes"), root.get("areaCode"), root.get("status"), root.get("treeLevel"), root.get("treeSort"), root);
        });
    }

    /**
     * 查询全部列表
     * @return
     */
    public PageResult<Area> find(Area area) {
        PageRequest pageable;
        if(area.getOrderBy()==null||area.getOrderBy().equals("")) {
            pageable = PageRequest.of(area.getPageNo(), area.getPageSize(), area.getDesc(), "treeSort");
        } else {
            pageable = PageRequest.of(area.getPageNo(), area.getPageSize(), area.getDesc(), area.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        PageResult areas =  PageResult.getPageResult(areaDao.findAll(
                (Specification<Area>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (area.getAreaCode() != null && !area.getAreaCode().equals("")) {
                        predicates.add(cb.like(root.get("areaCode").as(String.class), "%" + area.getAreaCode() + "%"));
                    }
                    if (area.getAreaName() != null && !area.getAreaName().equals("")) {
                        predicates.add(cb.like(root.get("areaName").as(String.class), "%" + area.getAreaName() + "%"));
                    }
                    if (area.getParentCode() != null && !area.getParentCode().equals("")) {
                        predicates.add(cb.equal(root.get("parentCode").as(String.class), area.getParentCode()));
                    }
                    if (area.getStatus() != null && !area.getStatus().equals("")) {
                        predicates.add(cb.equal(root.get("status").as(String.class), area.getStatus()));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), Area.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
        return areas;
    }

    /**
     * 保存机构
     * @param area
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Area area){
        Area areaParent = getAreaByAreaCode(area.getParentCode());
        if(areaParent!=null) {
            area.setParentCodes(areaParent.getParentCodes() + areaParent.getAreaCode() + ",");
            area.setTreeSorts(areaParent.getTreeSorts() + String.format("%10d", area.getTreeSort()).replace(" ", "0") + ",");
            area.setTreeNames(areaParent.getTreeNames() + "/" + area.getAreaName());
            if (area.getIsNewRecord()) {
                area.setTreeLeaf("1");
                area.setTreeLevel(areaParent.getTreeLevel() + 1);
                area.setStatus("0");
                areaParent.setTreeLeaf("0");
                areaDao.save(areaParent);
            }
        }else{
            area.setParentCode("0");
            area.setParentCodes("0,");
            area.setTreeSorts(String.format("%10d", area.getTreeSort()).replace(" ", "0") + ",");
            area.setTreeNames(area.getAreaName());
            area.setTreeLeaf("1");
            area.setTreeLevel(0);
            if (area.getIsNewRecord()) {
                area.setStatus("0");
            }
        }
        if(!area.getIsNewRecord()){
            Area a = this.getAreaByAreaCode(area.getAreaCode());
            area.setTreeLeaf(a.getTreeLeaf());
            area.setTreeLevel(a.getTreeLevel());
            area.setStatus(a.getStatus());
        }
        areaDao.save(area);
    }

    /**
     * 删除机构
     * @param area
     */
    public Boolean delete(Area area){
        int count = areaDao.getParentCodesCount("%"+area.getAreaCode()+",%");
        if(count > 0) {
            return false;
        }
        Area a = getAreaByAreaCode(area.getAreaCode());
        a.setStatus(Area.STATUS_DELETE);
        areaDao.save(a);
        return true;
    }

    public void updateTree(List<Area> areas){
        areaDao.saveAll(areas);
    }

    /**
     * 获得机构信息
     * @param areaCode
     * @return
     */
    public Area getAreaByAreaCode(String areaCode){
        return areaDao.getAreaByAreaCode(areaCode);
    }

    /**
     * 停用区域
     * @param id
     */
    public Result disable(String id) {
        int count = areaDao.getParentCodesCount(id);
        if(count>0){
            return new Result(true, StatusCode.ERROR, "该区域包含未停用的子区域");
        }
        Area area = areaDao.getAreaByAreaCode(id);
        area.setStatus("2");
        areaDao.save(area);
        return new Result(true, StatusCode.OK, "停用区域"+area.getAreaName()+"成功");
    }

    /**
     * 启用区域
     * @param id
     */
    public String enable(String id) {
        Area area = areaDao.getAreaByAreaCode(id);
        area.setStatus("0");
        areaDao.save(area);
        return "启用区域"+area.getAreaName()+"成功";
    }
}
