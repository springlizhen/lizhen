package com.chinags.auth.service;

import com.chinags.auth.dao.RoleDao;
import com.chinags.auth.entity.Office;
import com.chinags.auth.entity.Post;
import com.chinags.auth.entity.Role;
import com.chinags.common.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuService menuService;

    /**
     * 查询全部列表
     * @return
     */
    public List<Role> findAll(Role role) {
        role.setStatus("0");
        Sort sort = new Sort(Sort.Direction.ASC, "roleSort");
        Example<Role> ex = Example.of(role);
        return roleDao.findAll(ex, sort);
    }

    /**
     * 获得角色信息 by loginCode
     * @param roleCode
     * @return
     */
    public Role getRoleByRoleCode(String roleCode){
        return roleDao.getRoleByRoleCode(roleCode);
    }

    public Set<Role> inRoleCode(String roleCodes){
        return roleDao.findByRoleCodeIn(roleCodes.split(","));
    }

    /**
     * 查询全部列表
     * @return office分页数据
     */
    public PageResult find(Role role) {
        PageRequest pageable;
        if(role.getOrderBy()==null|| "".equals(role.getOrderBy())) {
            pageable = PageRequest.of(role.getPageNo(), role.getPageSize(), Sort.Direction.ASC, "roleSort", "createDate");
        } else {
            pageable = PageRequest.of(role.getPageNo(), role.getPageSize(), role.getDesc(), role.getOrderBy(), "roleSort");
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(roleDao.findAll(
                (Specification<Role>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (role.getRoleName() != null && !"".equals(role.getRoleName())) {
                        predicates.add(cb.like(root.get("roleName").as(String.class), "%" + role.getRoleName() + "%"));
                    }
                    if (role.getRoleCode() != null && !"".equals(role.getRoleCode())) {
                        predicates.add(cb.like(root.get("roleCode").as(String.class), "%" + role.getRoleCode() + "%"));
                    }
                    if (role.getUserType() != null && !"".equals(role.getUserType())) {
                        predicates.add(cb.equal(root.get("userType").as(String.class), role.getUserType()));
                    }
                    if (role.getIsSys() != null && !"".equals(role.getIsSys())) {
                        predicates.add(cb.equal(root.get("isSys").as(String.class), role.getIsSys()));
                    }
                    if (role.getStatus() != null && !"".equals(role.getStatus())) {
                        predicates.add(cb.equal(root.get("status").as(String.class), role.getStatus()));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), Office.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 角色名称是否重复
     * @param roleName 角色名称
     * @return
     */
    public Boolean findCountRolename(String roleName){
        return roleDao.findCountRolename(roleName)==0;
    }

    /**
     * 保存角色
     * @param role 角色数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Role role){
        roleDao.save(role);
    }

    /**
     * 删除角色
     * @param roleCode 角色code
     * @return
     */
    public Role delete(String roleCode) {
        Role role = getRoleByRoleCode(roleCode);
        role.setStatus("1");
        return roleDao.save(role);
    }

    /**
     * 角色状态
     * @param userCode 角色code
     * @return
     */
    public Role status(String userCode,String status) {
        Role role = getRoleByRoleCode(userCode);
        role.setStatus(status);
        return roleDao.save(role);
    }

    /**
     * 保存角色用户
     * @param role
     */
    @Transactional(rollbackOn = Exception.class)
    public void saveAuthUser(Role role) {
        String[] userCodes = role.getUserRoleString().split(",");
        List<String> uCode = roleDao.selectUserRole(role.getRoleCode());
        for (String userCode : userCodes) {
            if(!uCode.contains(userCode)) {
                roleDao.insertUserRole(userCode, role.getRoleCode());
            }
        }
    }

    /**
     * 删除角色用户
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteAuthUser(String roleCode, String userRoleString){
        roleDao.deleteUserRole(roleCode, userRoleString.split(","));
    }

    /**
     * 更新redis角色菜单权限
     * @param roleCode
     * @throws Exception
     */
    public void selectUserRole(String roleCode){
        List<String> strings = roleDao.selectUserRole(roleCode);
        for (String userCode : strings) {
            try {
                menuService.deleteRedis(userCode);
            } catch (Exception e) {
            }
        }
    }
}
