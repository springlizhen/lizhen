package com.chinags.auth.service;

import com.chinags.auth.dao.EmployeeDao;
import com.chinags.auth.dao.OfficeDao;
import com.chinags.auth.entity.Office;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.service.BaseService;
import com.chinags.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Zhang
 */
@Service
public class OfficeService extends BaseService<Office>{

    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 获取左侧菜单栏
     * @param office 查询对象
     * @return Office集合
     */
    public List<Office> findAll(Office office){
        //菜单状态 正常
        office.setStatus("0");
        Sort sort = new Sort(Sort.Direction.ASC, "treeLevel","treeSort");
        Example<Office> ex = Example.of(office);
        return officeDao.findAll(ex, sort);
    }

    /**
     * 获取树形结构
     * @param excludeCode 机构code
     * @return Office集合
     */
    public List<Office> treeData(String excludeCode){
        return officeDao.findAll((Specification<Office>) (root, query, cb) -> getPredicate(excludeCode, query, cb, root.get("parentCodes"), root.get("officeCode"), root.get("status"), root.get("treeLevel"), root.get("treeSort"), root));
    }

    /**
     * 获取机构信息
     * @param officeLevel 查询对象
     * @return Office集合
     */
    public List<Map<String,Object>> findOffice(String officeLevel){
        return officeDao.getOffice(officeLevel);
    }

    /**
     * 查询列表
     * @return office分页数据
     */
    public PageResult find(Office office) {
        PageRequest pageable;
        if(office.getOrderBy()==null|| "".equals(office.getOrderBy())) {
            pageable = PageRequest.of(office.getPageNo(), office.getPageSize(), office.getDesc(), "treeSort");
        } else {
            pageable = PageRequest.of(office.getPageNo(), office.getPageSize(), office.getDesc(), office.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(officeDao.findAll(
                (Specification<Office>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (office.getOfficeCode() != null && !"".equals(office.getOfficeCode())) {
                        predicates.add(cb.like(root.get("officeCode").as(String.class), "%" + office.getOfficeCode() + "%"));
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
                    predicates.add(cb.notEqual(root.get("status").as(String.class), Office.STATUS_DELETE));
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
    public void save(Office office){
        Office officeParent = getAreaByOfficeCode(office.getParentCode());
        if(officeParent!=null) {
            office.setParentCodes(officeParent.getParentCodes() + officeParent.getOfficeCode() + ",");
            office.setTreeSorts(officeParent.getTreeSorts() + String.format("%10d", office.getTreeSort()).replace(" ", "0") + ",");
            office.setTreeNames(officeParent.getTreeNames() + "/" + office.getOfficeName());
            if (office.getIsNewRecord()) {
                office.setTreeLeaf("1");
                office.setTreeLevel(officeParent.getTreeLevel() + 1);
                office.setStatus("0");
                officeParent.setTreeLeaf("0");
                officeDao.save(officeParent);
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
            Office o = this.getAreaByOfficeCode(office.getOfficeCode());
            office.setTreeLeaf(o.getTreeLeaf());
            office.setTreeLevel(o.getTreeLevel());
            office.setStatus(o.getStatus());
        }
        officeDao.save(office);
    }

    /**
     * 删除机构
     * @param office office实体
     */
    public Boolean delete(Office office){
        int count = officeDao.getParentCodesCount("%"+office.getOfficeCode()+",%");
        if(count > 0) {
            return false;
        }
        Office o = getAreaByOfficeCode(office.getOfficeCode());
        o.setStatus(Office.STATUS_DELETE);
        officeDao.save(o);
        return true;
    }

    /**
     * 批量保存
     * @param offices office集合
     */
    public void updateTree(List<Office> offices){
        officeDao.saveAll(offices);
    }

    /**
     * 获得机构信息
     * @param areaCode office code
     * @return 机构信息
     */
    public Office getAreaByOfficeCode(String areaCode){
        return officeDao.getAreaByOfficeCode(areaCode);
    }

    /**
     * 停用区域
     * @param id office code
     */
    public Result disable(String id) {
        int count = officeDao.getParentCodesCount(id);
        if(count>0){
            return new Result(true, StatusCode.ERROR, "该区域包含未停用的子区域");
        }
        Office office = officeDao.getAreaByOfficeCode(id);
        office.setStatus("2");
        officeDao.save(office);
        return new Result(true, StatusCode.OK, "停用区域"+office.getOfficeName()+"成功");
    }

    /**
     * 启用区域
     * @param id office code
     */
    public String enable(String id) {
        Office office = officeDao.getAreaByOfficeCode(id);
        office.setStatus("0");
        officeDao.save(office);
        return "启用区域"+office.getOfficeName()+"成功";
    }

    /**
     * 获取组织架构
     * @return
     */
    public List<Map<String,Object>> getOrg() {
        return officeDao.getOrg();
    }

    /**
     * 获取机构对应最近的指定机构类型
     * @return
     */
    public Office getOrgOfficeTypeEnd(String id, String type) {
        //获取祖级别以及本级别的code
        String officeIdString = officeDao.getOfficeIdString(id);
        String[] ids = officeIdString.split(",");
        Office office = officeDao.getOfficeTypeString(ids, type);
        return office;
    }

    /**
     * 获取机构对应所有子机构id
     * @return
     */
    public List<String> getOrgOfficeChirldsId(String id, String typeValue) {
        List<String> officeIdString = null;
        if(StringUtils.isNotEmpty(typeValue)){
            officeIdString = officeDao.getOrgOfficeChirldsAndTypeId(id,typeValue);
        }else{
            officeIdString = officeDao.getOrgOfficeChirldsId(id);
        }
        return officeIdString;
    }

    /**
     * 获取机构对应所有子机构id
     * @return
     */
    public List<Office> getOrgOfficeChirlds(String id, String typeValue) {
        List<Office> officeIdString = null;
        if(StringUtils.isNotEmpty(typeValue)){
            officeIdString = officeDao.getOrgOfficeChirldsAndType(id,typeValue);
        }else{
            officeIdString = officeDao.getOrgOfficeChirlds(id);
        }
        return officeIdString;
    }

    /**
     * 根据父级ID和机构等级查询机构列表
     * @param parentCode    父级ID
     * @param officeLevel   机构等级
     * @return
     */
    public List<Office> getListByParentAndLevel(String parentCode, String officeLevel) {
        List<Office> offices = null;
        if(StringUtils.isNotEmpty(parentCode)){
            offices = officeDao.getListByParentAndLevel(parentCode, officeLevel);
        } else {
            offices = officeDao.getListByOfficeLevel(officeLevel);
        }
        return offices;
    }

    /**
     * 根据用户id获取数据
     * @param userCode
     * @return
     */
    public List<Office> officeListTo(String userCode) {
            List<Office> list= new ArrayList<>();
            List<Office> list1 = new ArrayList<>();
            String officeCode = employeeDao.selectByEmpCode(userCode);
            if(!"".equals(officeCode)){
                return list = officeDao.selectByofficeCode(officeCode);

            }else{
                return  list;
            }
    }

    /**
     * 根据编码获取所有的子级编码
     * @param userCode
     * @return
     */
    public List<String> selectByofficeCode(String officeCode) {
        return officeDao.selectByofficeCodeTo(officeCode);
    }


    public List<Office> selectByofficeCodeTo(String value){
        return officeDao.selectByofficeCodeToL(value);
    }

    public List<Office> selectByParentCode(String value) {
        return officeDao.selectByParentCode(value);
    }

    public List<String> selectByorgId(String userCode) {
        return officeDao.selectByorgId(userCode);
    }

    public List<Office> QueryBytreeDate(String userCode) {
        List<Office> list = new ArrayList<>();
        String officeCode = employeeDao.selectByEmpCode(userCode);
        if(!"".equals(officeCode)){
             list = officeDao.QueryBytreeDate(officeCode);
        }
        return  list;
    }

    public List<Office> QueryByofficeCode(String officeCode) {
        return officeDao.QueryByofficeCode(officeCode);
    }

    public Office selectByofficezl(String gt) {
        return officeDao.selectByofficezl(gt);
    }

    public Office selectByoffice(String parcode) {
        return officeDao.selectByoffice(parcode);
    }

    public List<String> getziji(String officeCode) {
        return officeDao.getziji(officeCode);
    }


    public Office getshangji(String officeCode) {
        return officeDao.getshangji(officeCode);
    }

    public List<Office> selectByofficeUserName(String userName) {
        List<Office> list= new ArrayList<>();
        List<Office> list1 = new ArrayList<>();
        String userCode = officeDao.selectByuserName(userName);
        String officeCode = employeeDao.selectByEmpCode(userCode);
        if(!"".equals(officeCode)){
            return list = officeDao.selectByofficeCode(officeCode);

        }else{
            return  list;
        }
    }

    public List<String> getFenzhongxin(String officeName) {
       List<Office> list = officeDao.getFenzhongxin(officeName);
       String officeCode = list.get(0).getOfficeCode();
       List<String> list1 = officeDao.getFenzhongxinT(officeCode);
       return list1;
    }
}
