package com.chinags.phone.controller;

import com.chinags.common.entity.LoginUser;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.phone.client.AuthClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("计划登录管理controller")
@RestController
@CrossOrigin
@RequestMapping("/phoneLogin")
public class LoginPhoneController {

    @Autowired
    private AuthClient authClient;

    @ApiOperation("登录")
    @GetMapping("/login")
    public Result phoneLogin(LoginUser loginUser) {
        return authClient.phoneLogin(loginUser);
    }

    @ApiOperation("根据等级查询机构列表")
    @GetMapping("/selectOfficeListByLevel")
    public Result selectOfficeListByLevel(String officeLevel) {
        Result result = null;
        try {
            result = authClient.officeList(officeLevel);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return result;
    }

}
