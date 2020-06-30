package com.chinags.auth.service;

import com.chinags.auth.dao.EmployeeDao;
import com.chinags.auth.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 通过部门查找用户
     * @param officeCode
     * @return
     */
    public List<Map<String, Object>> findEmpByOfficeCode(String officeCode){
        return employeeDao.findEmpByOfficeCode(officeCode);
    }

    /**
     * 查找Employee用户及其登录名
     * @return
     */
    public List<Map<String, Object>> findEmpMap(){
        return employeeDao.findEmpMap();
    }




}
