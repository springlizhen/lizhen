package com.chinags.auth.service;

import com.chinags.auth.dao.*;
import com.chinags.auth.entity.Comm;
import com.chinags.auth.entity.Role;
import com.chinags.auth.entity.RoleDataScope;
import com.chinags.auth.entity.SysUser;
import com.chinags.common.redis.RoleDataRedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class RoleDataScopeService {

    public static Lock lock = new ReentrantLock();

    @Autowired
    private RoleDataScopeDao roleDataScopeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private OfficeDao officeDao;

    @Autowired
    private CommDao commDao;

    /**
     * 查询全部列表
     * @return
     */
    public List<RoleDataScope> findAll(RoleDataScope roleDataScope) {
        Example<RoleDataScope> ex = Example.of(roleDataScope);
        return roleDataScopeDao.findAll(ex);
    }

    @Transactional(rollbackOn = Exception.class)
    public void save(RoleDataScope roleDataScope){
        String roleCode = roleDataScope.getRoleCode();
        String authCode = roleDataScope.getAuthCode();
        String roleType = roleDataScope.getRoleType();
        roleDataScopeDao.deleteByRoleCode(roleCode, authCode);
        if(roleDataScope.getRoleDataScopeListJson()!=null&&roleType.equals("2")) {
            String[] roles = roleDataScope.getRoleDataScopeListJson().split(",");
            for (String role : roles) {
                roleDataScope.setId("");
                roleDataScope.setRoleCode(roleCode);
                roleDataScope.setCtrlType("Office");
                roleDataScope.setCtrlPermi("1");
                roleDataScope.setCtrlData(role);
                roleDataScope.setAuthCode(authCode);
                roleDataScope.setRoleType("2");
                roleDataScopeDao.save(roleDataScope);
            }
        }else{
            roleDataScope.setId("");
            roleDataScope.setRoleCode(roleCode);
            roleDataScope.setCtrlType("Office");
            roleDataScope.setCtrlPermi("1");
            roleDataScope.setCtrlData(roleType);
            roleDataScope.setAuthCode(authCode);
            roleDataScope.setRoleType(roleType);
            roleDataScopeDao.save(roleDataScope);
        }

    }

    /**
     * 获取菜单数据权限
     * @param userCode 用户code
     * @return
     * @throws Exception
     */
    public String[] officeDataList(String userCode, String systemname) throws Exception {
        String[] officeCodes = RoleDataRedisUtils.getUserOffice(userCode, systemname);
        if(officeCodes==null){
            if(officeCodes==null) {
                if(lock.tryLock()) {
                    menuPermissionRedis(userCode, systemname);
                    lock.unlock();
                }else {
                    Thread.sleep(100);
                    officeDataList(userCode, systemname);
                }
            }
            return officeDataList(userCode, systemname);
        }
        return officeCodes;
    }

    /**
     * 菜单权限redis添加
     * @param usercode 用户code
     * @param systemname 系统
     * @throws Exception
     */
    public void menuPermissionRedis(String usercode, String systemname) throws Exception {
        List<String> roleMenuPermission = findOffice(usercode, systemname);
        RoleDataRedisUtils.setUserOffice(usercode, roleMenuPermission, systemname);
    }

    /**
     * 部门权限列表
     * @param userCode
     * @param authCode 系统
     * @return
     */
    public List<String> findOffice(String userCode, String authCode) {
        List<String> userCodeRole = roleDao.getUserCodeRole(userCode);
        //获取用户角色
        String[] roleCodes = userCodeRole.toArray(new String[userCodeRole.size()]);
        Long count = roleDataScopeDao.findOfficeIsAll(roleCodes, "1", authCode);
        //全部数据
        if(count>0){
            List<String> office = new ArrayList<>(1);
            office.add("1");
            return office;
        }
        List<String> office = roleDataScopeDao.findOffice(roleCodes, authCode);
        count = roleDataScopeDao.findOfficeIsAll(roleCodes, "3", authCode);
        //是否本部门
        if(count>0){
            SysUser sysUser = sysUserDao.getSysUserByUserCode(userCode);
            List<String> officeParentCodes = officeDao.getOfficeParentCodes(sysUser.getEmployee().getOfficeCode());
            officeParentCodes.add(sysUser.getEmployee().getOfficeCode());
            office.removeAll(officeParentCodes);
            office.addAll(officeParentCodes);
        }
        return office;
    }

    /**
     * 删除菜单redis
     * @param usercode 用户code
     */
    public void deleteRedis(String usercode){
        try {
            List<Comm> all = commDao.findAll();
            for (Comm comm : all) {
                RoleDataRedisUtils.deleteUserMenu(usercode, comm.getAuthCode());
            }
        } catch (Exception e) {
        }
    }
}
