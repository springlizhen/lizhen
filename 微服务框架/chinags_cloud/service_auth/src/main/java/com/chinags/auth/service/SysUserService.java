package com.chinags.auth.service;

import com.chinags.auth.dao.OfficeDao;
import com.chinags.auth.dao.SysUserDao;
import com.chinags.auth.entity.*;
import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.PasswordUtils;
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

@Service
public class SysUserService {
    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private OfficeDao officeDao;

    /**
     * 查询全部列表
     * @return
     */
    public List<SysUser> findAll() {
        return sysUserDao.findAll();
    }

    /**
     * 查询全部列表
     * @return
     */
    public List<Map> findUsers(String officeCode) {
        return sysUserDao.getOfficeUser(officeCode);
    }

    /**
     * 获得用户信息 by loginCode
     * @param loginCode
     * @return
     */
    public SysUser getSysUserByLoginCode(String loginCode){
        return sysUserDao.getSysUserByLoginCode(loginCode);
    }

    /**
     * 获得用户信息 by loginCode
     * @param userCode
     * @return
     */
    public SysUser getSysUserByUserCode(String userCode){
        return sysUserDao.getSysUserByUserCode(userCode);
    }

    /**
     * 查询全部列表
     * @return office分页数据
     */
    public PageResult find(SysUser sysUser) {
        PageRequest pageable;
        if(sysUser.getOrderBy()==null|| "".equals(sysUser.getOrderBy())) {
            pageable = PageRequest.of(sysUser.getPageNo(), sysUser.getPageSize(), Sort.Direction.ASC,"userWeight","createDate");
        } else {
            pageable = PageRequest.of(sysUser.getPageNo(), sysUser.getPageSize(), sysUser.getDesc(), sysUser.getOrderBy(),"userWeight");
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(sysUserDao.findAll(
                (Specification<SysUser>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(cb.notEqual(root.get("loginCode").as(String.class), "system"));
                    predicates.add(cb.notEqual(root.get("loginCode").as(String.class), "admin"));
                    if (sysUser.getLoginCode() !=null && !"".equals(sysUser.getLoginCode())) {
                        predicates.add(cb.like(root.get("loginCode").as(String.class), "%" + sysUser.getLoginCode() + "%"));
                    }
                    if (sysUser.getUserName() !=null && !"".equals(sysUser.getUserName())) {
                        predicates.add(cb.like(root.get("userName").as(String.class), "%" + sysUser.getUserName() + "%"));
                    }
                    if (sysUser.getEmail() !=null && !"".equals(sysUser.getEmail())) {
                        predicates.add(cb.like(root.get("email").as(String.class), "%" + sysUser.getEmail() + "%"));
                    }
                    if (sysUser.getPhone() !=null && !"".equals(sysUser.getPhone())) {
                        predicates.add(cb.like(root.get("phone").as(String.class), "%" + sysUser.getPhone() + "%"));
                    }
                    if (sysUser.getMobile() !=null && !"".equals(sysUser.getMobile())) {
                        predicates.add(cb.like(root.get("mobile").as(String.class), "%" + sysUser.getMobile() + "%"));
                    }
                    if (sysUser.getRefName() !=null && !"".equals(sysUser.getRefName())) {
                        predicates.add(cb.like(root.get("refName").as(String.class), "%" + sysUser.getRefName() + "%"));
                    }
                    if (sysUser.getStatus() != null && !sysUser.getStatus().equals("")) {
                        predicates.add(cb.equal(root.get("status").as(String.class), sysUser.getStatus()));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), SysUser.STATUS_DELETE));

                    Join<SysUser, Employee> join = root.join("employee", JoinType.INNER);
                    SetJoin<Employee, Post> setJoin = join.joinSet("postSet", JoinType.LEFT);
                    if (sysUser.getUserRoleString()!=null&&sysUser.getUserRoleString().length()>0) {
                        SetJoin<Employee, Role> setRole = root.joinSet("roleSet", JoinType.LEFT);
                        predicates.add(cb.equal(setRole.get("roleCode").as(String.class), sysUser.getUserRoleString()));
                    }
                    if(sysUser.getEmployee()!=null) {
                        if (StringUtils.isNotEmpty(sysUser.getEmployee().getEmpName())) {
                            predicates.add(cb.like(join.get("empName").as(String.class), "%" + sysUser.getEmployee().getEmpName() + "%"));
                        }
                        if (StringUtils.isNotEmpty(sysUser.getEmployee().getOfficeCode())) {
                            CriteriaBuilder.In<String> in = cb.in(join.get("officeCode"));
                            in.value(sysUser.getEmployee().getOfficeCode());
                            for (Office f : officeDao.getAreaByParentCodesLike("%"+sysUser.getEmployee().getOfficeCode()+"%")) {
                                in.value(f.getOfficeCode());
                            }
                            predicates.add(in);
                        }
                        if (StringUtils.isNotEmpty(sysUser.getEmployee().getCompanyCode())) {
                            predicates.add(cb.like(join.get("companyCode").as(String.class), "%" + sysUser.getEmployee().getCompanyCode() + "%"));
                        }
                        if (sysUser.getEmployee().getPostCode()!=null&&sysUser.getEmployee().getPostCode().length()>0) {
                            predicates.add(cb.equal(setJoin.get("postCode").as(String.class), sysUser.getEmployee().getPostCode()));
                        }
                    }
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    query.distinct(true);
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 保存用户
     * @param sysUser 用户数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(SysUser sysUser){
        sysUserDao.save(sysUser);
    }

    /**
     * 重置密码
     * @param userCode 用户code
     */
    @Transactional(rollbackOn = Exception.class)
    public void resetpwd(String userCode){
        String pw =  Global.getPassWord();
        sysUserDao.resetpwd(PasswordUtils.encryptPassword(pw), userCode);
    }

    /**
     * 修改密码
     * @param userCode 用户code
     */
    @Transactional(rollbackOn = Exception.class)
    public void editpwd(String pw, String userCode){
        sysUserDao.resetpwd(pw, userCode);
    }

    /**
     * 账号是否重复
     * @param loginCode 登录账号
     * @return
     */
    public Boolean findCountUserCode(String loginCode){
        return sysUserDao.findCountLoginCode(loginCode)==0;
    }

    /**
     * 删除用户
     * @param userCode 用户code
     * @return
     */
    public SysUser delete(String userCode) {
        SysUser sysUser = sysUserDao.getSysUserByUserCode(userCode);
        Employee e = sysUser.getEmployee();
        e.setStatus(Employee.STATUS_DELETE);
        sysUser.setEmployee(e);
        sysUser.setStatus("1");
        return sysUserDao.save(sysUser);
    }

    /**
     * 用户状态
     * @param userCode 用户code
     * @return
     */
    public SysUser status(String userCode,String status) {
        SysUser sysUser = sysUserDao.getSysUserByUserCode(userCode);
        sysUser.setStatus(status);
        return sysUserDao.save(sysUser);
    }

    public List<Map> getUser() {
        return sysUserDao.getUser();
    }

    public List<String> selectByName(String sendUserName) {
        return sysUserDao.getByname(sendUserName);
    }
}
