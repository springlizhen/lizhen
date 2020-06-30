package com.chinags.auth.dao;

import com.chinags.auth.entity.RoleDataScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RoleDataScopeDao extends JpaRepository<RoleDataScope, String>, JpaSpecificationExecutor<RoleDataScope> {

    @Modifying
    @Transactional
    @Query(value = "delete from t_pub_sys_role_data_scope where role_code=:roleCode and auth_code=:authCode", nativeQuery = true)
    public void deleteByRoleCode(String roleCode, String authCode);

    @Query(value = "select distinct ctrl_data from t_pub_sys_role_data_scope where role_code in (:roleCodes) and auth_code=:authCode", nativeQuery = true)
    public List<String> findOffice(String[] roleCodes, String authCode);

    @Query(value = "select count(*) from t_pub_sys_role_data_scope where role_code in (:roleCodes) and role_type=:roleType and auth_code=:authCode", nativeQuery = true)
    public Long findOfficeIsAll(String[] roleCodes, String roleType, String authCode);
}
