package com.chinags.auth.service;

import com.chinags.auth.dao.SysUserDao;
import com.chinags.auth.entity.LoginUser;
import com.chinags.auth.entity.SysUser;
import com.chinags.common.utils.AuthorizingRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private SysUserDao sysUserDao;

    public boolean isLogin(LoginUser loginUser){
        SysUser sysUser = sysUserDao.getSysUserByLoginCode(loginUser.getUsername());
        if(sysUser!=null){
            return AuthorizingRealm.validatePassword(loginUser.getPassword(),sysUser.getPassword());
        }else{
            return false;
        }
    }

    public SysUser getSysUserByLoginCode(String loginCode){
        return sysUserDao.getSysUserByLoginCode(loginCode);
    }
}
