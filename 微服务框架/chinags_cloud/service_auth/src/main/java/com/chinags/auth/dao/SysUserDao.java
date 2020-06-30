package com.chinags.auth.dao;

import com.chinags.auth.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface SysUserDao extends JpaRepository<SysUser, String>, JpaSpecificationExecutor<SysUser> {
    public SysUser getSysUserByLoginCode(String loginCode);

    public SysUser getSysUserByUserCode(String userCode);

    @Modifying
    @Query("update SysUser s set s.password = :password where s.userCode = :userCode")
    public void resetpwd(String password, String userCode);

    @Query(value = "SELECT count(*) FROM t_pub_sys_user WHERE login_code=:loginCode", nativeQuery = true)
    public Long findCountLoginCode(String loginCode);

    @Query("select s.userCode as userCode, s.loginCode as loginCode, s.userName as username, s.email as email, s.mobile as mobile, s.phone as phone, s.sex as sex, e.officeCode as officeCode, e.officeName as officeName, p.postName as post from SysUser s join s.employee e join e.postSet p")
    List<Map> getUser();

    @Query(value = "select wm_concat(user_name) from T_PUB_SYS_USER t where user_code in (:userCode)", nativeQuery = true)
    public String getUserCodeName(@Param("userCode") String[] userCode);

    //@Query(value = "select t.user_code as userCode,t.user_name as userName,e.office_code as op FROM T_PUB_SYS_USER t left join T_PUB_SYS_EMPLOYEE e on t.ref_code = e.emp_code where e.office_code = :officeCode and t.status = '0'", nativeQuery = true)
    @Query("select s.loginCode as loginCode, s.userName as userName, e.officeCode as officeCode from SysUser s join s.employee e where e.officeCode = :officeCode and s.status='0'")
    public List<Map> getOfficeUser(String officeCode);
    @Query(value = "SELECT USER_CODE FROM t_pub_sys_user WHERE USER_NAME=:sendUserName", nativeQuery = true)
    List<String> getByname(String sendUserName);
}
