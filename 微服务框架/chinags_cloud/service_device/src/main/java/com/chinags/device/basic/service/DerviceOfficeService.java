package com.chinags.device.basic.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.service.BaseService;
import com.chinags.device.basic.dao.DerviceOfficeDao;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.client.UserClient;
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
import java.util.Map;

/**
 * @author Zhang
 */

@Service
public class DerviceOfficeService extends BaseService<DerviceOffice>{

    @Autowired
    private DerviceOfficeDao derviceOfficeDao;
    @Autowired
    private UserClient userClient;


    /**
     * 获取左侧菜单栏
     * @param derviceOffice 查询对象
     * @return Office集合
     */
    public List<DerviceOffice> findAll(DerviceOffice derviceOffice){
        //菜单状态 正常
        derviceOffice.setStatus("0");
        Sort sort = new Sort(Sort.Direction.ASC, "treeLevel","treeSort");
        Example<DerviceOffice> ex = Example.of(derviceOffice);
        return derviceOfficeDao.findAll(ex, sort);
    }

    /**
     * 获取树形结构
     * @param excludeCode 机构code
     * @return Office集合
     */
    public List<DerviceOffice> treeData(String excludeCode){
        return derviceOfficeDao.findAll((Specification<DerviceOffice>) (root, query, cb) -> getPredicate(excludeCode, query, cb, root.get("parentCodes"), root.get("officeCode"), root.get("status"), root.get("treeLevel"), root.get("treeSort"), root));
    }

    /**
     * 查询列表
     * @return office分页数据
     */
    public PageResult find(DerviceOffice office) {
        PageRequest pageable;
        if(office.getOrderBy()==null|| "".equals(office.getOrderBy())) {
            pageable = PageRequest.of(office.getPageNo(), office.getPageSize(), Sort.Direction.ASC, "treeSort");
        } else {
            pageable = PageRequest.of(office.getPageNo(), office.getPageSize(), office.getDesc(), office.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(derviceOfficeDao.findAll(
                (Specification<DerviceOffice>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (office.getOfficeCode() != null && !"".equals(office.getOfficeCode())) {
                        predicates.add(cb.like(root.get("officeCode").as(String.class), "%" + office.getOfficeCode() + "%"));
                    }
                    if (office.getViewCode() != null && !"".equals(office.getViewCode())) {
                        predicates.add(cb.like(root.get("viewCode").as(String.class), "%" + office.getViewCode() + "%"));
                    }
                    if (office.getOfficeName() != null && !"".equals(office.getOfficeName())) {
                        predicates.add(cb.like(root.get("officeName").as(String.class), "%" + office.getOfficeName() + "%"));
                    }
                    if (office.getFullName() != null && !"".equals(office.getFullName())) {
                        predicates.add(cb.like(root.get("fullName").as(String.class), "%" + office.getFullName() + "%"));
                    }
                    if (office.getParentCode() != null && !office.getParentCode().equals("")) {
                        predicates.add(cb.equal(root.get("parentCode").as(String.class), office.getParentCode()));
                    }
                    if (office.getStatus() != null && !"".equals(office.getStatus())) {
                        predicates.add(cb.equal(root.get("status").as(String.class), office.getStatus()));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), DerviceOffice.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 保存机构
     * @param office 机构数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(DerviceOffice office){
        DerviceOffice officeParent = getAreaByOfficeCode(office.getParentCode());
        if(officeParent!=null) {
            office.setParentCodes(officeParent.getParentCodes() + officeParent.getOfficeCode() + ",");
            office.setTreeSorts(officeParent.getTreeSorts() + String.format("%10d", office.getTreeSort()).replace(" ", "0") + ",");
            office.setTreeNames(officeParent.getTreeNames() + "/" + office.getOfficeName());
            if (office.getIsNewRecord()) {
                office.setTreeLeaf("1");
                office.setTreeLevel(officeParent.getTreeLevel() + 1);
                office.setStatus("0");
                officeParent.setTreeLeaf("0");
                derviceOfficeDao.save(officeParent);
            }
        }else{
            office.setParentCode("0");
            office.setParentCodes("0,");
            office.setTreeSorts(String.format("%10d", office.getTreeSort()).replace(" ", "0") + ",");
            office.setTreeNames(office.getOfficeName());
            if (office.getIsNewRecord()) {
                office.setTreeLeaf("1");
                office.setTreeLevel(0);
                office.setStatus("0");
            }
        }
        if(!office.getIsNewRecord()){
            DerviceOffice o = this.getAreaByOfficeCode(office.getOfficeCode());
            office.setTreeLeaf(o.getTreeLeaf());
            office.setTreeLevel(o.getTreeLevel());
            office.setStatus(o.getStatus());
        }
        derviceOfficeDao.save(office);
    }

    /**
     * 删除机构
     * @param office office实体
     */
    public Boolean delete(DerviceOffice office){
        int count = derviceOfficeDao.getParentCodesCount("%"+office.getOfficeCode()+",%");
        if(count > 0) {
            return false;
        }
        DerviceOffice o = getAreaByOfficeCode(office.getOfficeCode());
        o.setStatus(DerviceOffice.STATUS_DELETE);
        derviceOfficeDao.save(o);
        return true;
    }

    /**
     * 批量保存
     * @param offices office集合
     */
    public void updateTree(List<DerviceOffice> offices){
        derviceOfficeDao.saveAll(offices);
    }

    /**
     * 获得机构信息
     * @param areaCode office code
     * @return 机构信息
     */
    public DerviceOffice getAreaByOfficeCode(String areaCode){
        return derviceOfficeDao.getAreaByOfficeCode(areaCode);
    }

    /**
     * 停用区域
     * @param id office code
     */
    public Result disable(String id) {
        int count = derviceOfficeDao.getStopParentCodesCount(id);
        if(count>0){
            return new Result(true, StatusCode.ERROR, "该区域包含未停用的子区域");
        }
        DerviceOffice office = derviceOfficeDao.getAreaByOfficeCode(id);
        office.setStatus("2");
        derviceOfficeDao.save(office);
        return new Result(true, StatusCode.OK, "停用区域"+office.getOfficeName()+"成功");
    }

    /**
     * 启用区域
     * @param id office code
     */
    public String enable(String id) {
        DerviceOffice office = derviceOfficeDao.getAreaByOfficeCode(id);
        office.setStatus("0");
        derviceOfficeDao.save(office);
        return "启用区域"+office.getOfficeName()+"成功";
    }


    public DerviceOffice selectByoffice(String deviceClass) {
        return  derviceOfficeDao.selectByoffice(deviceClass);
    }
}
