package com.chinags.auth.controller;

import com.chinags.auth.entity.Comm;
import com.chinags.auth.entity.Office;
import com.chinags.auth.service.CommService;
import com.chinags.auth.service.OfficeService;
import com.chinags.auth.service.SysUserService;
import com.chinags.common.controller.SystemBaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.JwtUtil;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("系统管理controller")
@RestController
@CrossOrigin
@RequestMapping("/auth2")
public class AuthSystemController extends SystemBaseController {

    @Autowired
    private CommService commService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取token
     * @return
     */
    @ApiOperation("获取token")
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ResponseBody
    public Result getToken(String appSecretId, String appSecretKey){
        if(StringUtils.isEmpty(appSecretId)||StringUtils.isEmpty(appSecretKey)){
            return new Result(false, StatusCode.ERROR, "请携带密钥和系统名称编码访问！");
        }
        try {
            Comm commByAuthCode = commService.getCommByAuthCode(appSecretId);
            if (appSecretKey.equals(commByAuthCode.getId())){
                Map token = JwtUtil.createJWTSystem(appSecretKey, commByAuthCode.getAuthName(), appSecretId);
                return new Result(true, StatusCode.OK, "获取token成功",token);
            }else{
                return new Result(false, StatusCode.ERROR, "系统编码或秘钥不正确!");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
    }

    /**
     * 获取组织架构
     * @return
     */
    @ApiOperation("获取组织架构")
    @RequestMapping(value = "/getOrg", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrg(){
        try {
            List<Map<String,Object>> offices = officeService.getOrg();
            return new Result(true, StatusCode.OK, "获取组织架构成功",offices);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取组织架构失败");
        }
    }

    /**
     * 获取人员
     * @return
     */
    @ApiOperation("获取人员")
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public Result getUser(){
        try {
            List<Map> users = sysUserService.getUser();
            return new Result(true, StatusCode.OK, "获取人员信息成功",users);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取人员信息失败");
        }
    }
}
