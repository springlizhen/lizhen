package com.chinags.auth.controller;

import com.chinags.auth.entity.Menu;
import com.chinags.auth.entity.Role;
import com.chinags.auth.entity.RoleDataScope;
import com.chinags.auth.service.RoleDataScopeService;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Api("角色数据权限管理controller")
@RestController
@CrossOrigin
@RequestMapping("/roleData")
public class RoleDataScopeController extends BaseController {

    @Autowired
    private RoleDataScopeService roleDataScopeService;

    /**
     * 获取角色数据权限
     * @return
     */
    @ApiOperation("获取角色")
    @RequestMapping(value = "/allList", method = RequestMethod.GET)
    public Result roleList(RoleDataScope roleDataScope){
        List<RoleDataScope> roleDataScopes;
        try {
            roleDataScopes = roleDataScopeService.findAll(roleDataScope);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        if(roleDataScopes==null) {
            roleDataScopes = new ArrayList<>();
        }
        return new Result(true, StatusCode.OK, "获取成功",roleDataScopes);
    }

    @ApiOperation("保存角色")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody RoleDataScope roleDataScope) {
        roleDataScopeService.save(roleDataScope);
        return new Result(true, StatusCode.OK, "保存成功");
    }
}
