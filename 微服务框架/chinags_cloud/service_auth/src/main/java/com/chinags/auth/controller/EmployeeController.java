package com.chinags.auth.controller;

import com.chinags.auth.entity.Employee;
import com.chinags.auth.service.EmployeeService;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author XieWenqing
 * @date 2019/11/22 上午 12:50
 */
@Api("用户管理controller")
@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 获取全部用户
     * @return
     */
    @ApiOperation("获取全部用户")
    @RequestMapping(value = "/findEmpMap", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> findEmpMap(){
        return  employeeService.findEmpMap();
    }

    /**
     * 通过部门查找用户
     * @return
     */
    @ApiOperation("获取全部用户")
    @RequestMapping(value = "/findEmpByOfficeCode", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> findEmpByOfficeCode(String officeCode){
        return  employeeService.findEmpByOfficeCode(officeCode);
    }



}
