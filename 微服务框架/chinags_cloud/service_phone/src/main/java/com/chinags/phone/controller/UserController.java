package com.chinags.phone.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.LoginUser;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.phone.client.AuthClient;
import com.chinags.phone.entity.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("用户管理controller")
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private AuthClient authClient;

    @ApiOperation("修改用户密码")
    @PostMapping("/editPassword")
    public Result editPassword(String oldPassword, String password, String confirmPassword) {
        Result result;
        try {
            LoginUser loginUser = getLoginUser();
            result = authClient.editPassword(loginUser.getUsercode(), oldPassword, password, confirmPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "修改失败");
        }
        return result;
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public Result getUserInfo() {
        Result result;
        try {
            LoginUser loginUser = getLoginUser();
            result = authClient.userForm(loginUser.getUsercode());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return result;
    }

    @ApiOperation("修改用户信息")
    @GetMapping("/editUserInfo")
    public Result editUserInfo(String email, String mobile, String phone, String sign) {
        Result result;
        try {
            LoginUser loginUser = getLoginUser();
            result = authClient.editUserInfo(loginUser.getUsercode(), email, mobile, phone, sign, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "修改失败");
        }
        return result;
    }

    @ApiOperation("获取用户头像")
    @GetMapping("/getUserAvatar")
    public Result getUserAvatar() {
        String avatar = "";
        try {
            LoginUser loginUser = getLoginUser();
            avatar = authClient.getUserAvatar(loginUser.getUsercode());
            return new Result(true, StatusCode.OK, avatar);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
    }

    @ApiOperation("上传用户头像")
    @PostMapping("/uploadUserAvatar")
    public Result uploadUserAvatar(String imgBase64) {
        try {
            LoginUser loginUser = getLoginUser();
            Base64 base64 = new Base64();
            base64.setUserName(loginUser.getUsername());
            base64.setUserCode(loginUser.getUsercode());
            base64.setImgBase64(imgBase64);
            return authClient.uploadUserAvatar(base64);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "上传失败");
        }
    }

}
