package com.chinags.auth.dao;

import com.chinags.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    public Role getRoleByRoleCode(String rolecode);

    public Set<Role> findByRoleCodeIn(String[] roleCode);

    @Query(value = "SELECT count(*) FROM t_pub_sys_role WHERE role_name=:roleName", nativeQuery = true)
    public Long findCountRolename(String roleName);

    @Modifying
    @Query(value = "INSERT INTO t_pub_sys_user_role(user_code,role_code) VALUES(?1,?2)", nativeQuery = true)
    public Integer insertUserRole(String userCode, String roleCode);
    @Query(value = "SELECT user_code FROM t_pub_sys_user_role WHERE role_code = :roleCode", nativeQuery = true)
    public List<String> selectUserRole(String roleCode);
    @Modifying
    @Query(value = "delete from t_pub_sys_user_role where role_code=:roleCode and user_code in (:userCodes)", nativeQuery = true)
    public Integer deleteUserRole(String roleCode, String[] userCodes);

    @Query(value = "SELECT role_code FROM t_pub_sys_user_role WHERE user_code = :userCode", nativeQuery = true)
    public List<String> getUserCodeRole(String userCode);
}
