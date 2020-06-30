package com.chinags.auth.dao;

import com.chinags.auth.entity.Menu;
import com.chinags.auth.entity.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
public interface MenuDao extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {

    public Menu getMenuByMenuCode(String menuCode);

    public Set<Menu> findByMenuCodeIn(String[] menuCode);

    @Query(value = "select count(*) from t_pub_sys_menu where parent_codes LIKE :parentCodes", nativeQuery = true)
    public Integer getParentCodesCount(@Param("parentCodes") String parentCodes);

    @Query(value = "select * from t_pub_sys_menu where parent_codes LIKE %:parentCodes% and sys_code = :sysCode", nativeQuery = true)
    public List<Menu> getParentCodesMenu(@Param("parentCodes") String parentCodes, @Param("sysCode") String sysCode);

    @Query(value = "select u from Menu u where isShow='1' and menuType='1' and status='0' and sysCode=:sysCode and menuCode in (:menuCodes)")
    public List<Menu> getRoleLeftMenu(String[] menuCodes, String sysCode, Sort sort);

    @Query(value = "select u from Menu u where isShow='1' and menuType='2' and status='0' and sysCode=:sysCode and menuCode in (:menuCodes)")
    public List<Menu> getRoleLeftMenuPermission(String[] menuCodes, String sysCode, Sort sort);

    @Query(value = "select distinct menu_code from t_pub_sys_role_menu where role_code in (:roleCodes)", nativeQuery = true)
    public List<String> getRoleMenu(String[] roleCodes);

    @Query(value = "select distinct permission from Menu where menuCode in (:menuCodes)")
    public List<String> getRoleMenuPermission(String[] menuCodes);
}
