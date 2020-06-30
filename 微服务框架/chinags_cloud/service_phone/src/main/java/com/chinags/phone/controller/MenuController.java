package com.chinags.phone.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.LoginUser;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.phone.client.AuthClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("模块菜单管理controller")
@RestController
@CrossOrigin
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Autowired
    private AuthClient authClient;

    @ApiOperation("查询登录用户模块菜单列表")
    @GetMapping("/selectMenuList")
    public Result selectMenuList() {
        Result result = null;
        try {
            LoginUser loginUser = getLoginUser();
            result = authClient.getMenuList(loginUser.getUsercode());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return result;
    }

}
