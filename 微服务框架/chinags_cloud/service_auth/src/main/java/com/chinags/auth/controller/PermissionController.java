package com.chinags.auth.controller;

import com.chinags.auth.entity.Menu;
import com.chinags.auth.service.MenuService;
import com.chinags.auth.service.RoleDataScopeService;
import com.chinags.auth.service.RoleService;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.lang.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限控制层
 *
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("权限控制层controller")
@RestController
@CrossOrigin
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleDataScopeService roleDataScopeService;

    /**
     * 获取用户菜单数据权限
     * @param userCode
     * @return
     */
    @ApiOperation("获取用户菜单数据权限")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public Result menuDataList(String userCode){
        try {
            String[] strings = menuService.menuDataList(userCode, null);
            return new Result(true, StatusCode.OK, "获取成功",strings);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "用户code不可为空");
        }
    }

    /**
     * 指定用户是否有菜单数据权限
     * @param userCode
     * @param permission 权限标识
     * @return
     */
    @ApiOperation("指定用户是否有菜单数据权限")
    @RequestMapping(value = "/presence", method = RequestMethod.GET)
    @ResponseBody
    public Result menuDataPresence(String userCode, String permission){
        if (StringUtils.isEmpty(permission)) {
            return new Result(false, StatusCode.ERROR, "权限标识不可为空");
        }
        try {
            boolean presence = menuService.menuPresence(userCode, permission);
            return new Result(true, StatusCode.OK, "获取成功", presence);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "用户code不可为空");
        }
    }

    /**
     * 获取用户部门数据权限
     * @param userCode
     * @param systemname 系统标识
     * @return
     */
    @ApiOperation("获取用户部门数据权限")
    @RequestMapping(value = "/officeDataList", method = RequestMethod.GET)
    @ResponseBody
    public Result officeDataList(String userCode, String systemname){
        if (StringUtils.isEmpty(systemname)) {
            return new Result(false, StatusCode.ERROR, "系统标识不可为空");
        }
        try {
            String[] dataList = roleDataScopeService.officeDataList(userCode, systemname);
            return new Result(true, StatusCode.OK, "获取成功", dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "用户code不可为空");
        }
    }

}
