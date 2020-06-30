package com.chinags.auth.dao;

import com.chinags.auth.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface EmployeeDao extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {
    /**
     * 通过部门code获取用户
     * @param officeCode
     * @return
     */
    @Query(value = "select  emp.*, u.LOGIN_CODE from t_pub_sys_employee emp join t_pub_sys_user u on emp.emp_code = u.REF_CODE WHERE emp.office_code=:officeCode ORDER BY u.USER_WEIGHT desc",nativeQuery =true)
    List<Map<String, Object>> findEmpByOfficeCode(String officeCode);

    /**
     * 获取全部用户
     * @return
     */
    @Query(value = "select  emp.*, u.LOGIN_CODE from t_pub_sys_employee emp join t_pub_sys_user u on emp.emp_code = u.REF_CODE ORDER BY u.USER_WEIGHT desc",nativeQuery =true)
    List<Map<String, Object>> findEmpMap();

    /**
     * 通过用户code获取用户部门
     * @param userCode
     * @return
     */
    @Query(value = "select  emp.OFFICE_CODE  from t_pub_sys_employee emp join t_pub_sys_user u on emp.EMP_CODE = u.REF_CODE where u.USER_CODE=:userCode",nativeQuery =true)
    String selectByEmpCode(String userCode);
}
