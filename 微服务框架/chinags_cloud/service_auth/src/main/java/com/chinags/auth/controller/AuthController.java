package com.chinags.auth.controller;

import com.chinags.auth.client.UserClient;
import com.chinags.auth.entity.Comm;
import com.chinags.auth.entity.LoginUser;
import com.chinags.auth.entity.Menu;
import com.chinags.auth.entity.SysUser;
import com.chinags.auth.service.AuthService;
import com.chinags.auth.service.CommService;
import com.chinags.auth.service.MenuService;
import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@Api("系统菜单管理controller")
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController extends BaseController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private AuthService authService;

    @Autowired
    private CommService commService;


    /**
     * 获取全部系统属性
     * @return
     */
    @ApiOperation("全部系统信息")
    @RequestMapping(value = "/comm/list", method = RequestMethod.GET)
    @ResponseBody
    public Result commList(Comm comm){
        PageResult<Comm> page;
        try {
            page = commService.findAll();
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    @ApiOperation("保存系统")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Comm comm) {
        comm.setCreateBy(getLoginUser().getUsercode());
        comm.setCreateDate(new Date());
        comm.setUpdateBy(getLoginUser().getUsercode());
        comm.setUpdateDate(new Date());
        comm.setStatus("0");
        this.commService.save(comm);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    /**
     * 获取全部系统属性
     * @return
     */
    @ApiOperation("分页系统信息")
    @RequestMapping(value = "/comm/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result commListPage(Comm comm){
        PageResult<Comm> page;
        try {
            page = commService.find(comm);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取系统属性
     * @return
     */
    @ApiOperation("具体系统信息")
    @RequestMapping(value = "/comm/form", method = RequestMethod.GET)
    public Result commForm(String id, String systemname){
        Comm comm = new Comm();
        try {
            comm = commService.getCommById(id);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",comm);
    }

    /**
     * 删除系统
     * @return
     */
    @ApiOperation("具体系统信息")
    @RequestMapping(value = "/comm/delete", method = RequestMethod.GET)
    public Result delete(String id, String systemname){
        try {
            commService.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "删除失败");
        }
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 系统树结构
     * @return
     */
    @ApiOperation("系统树结构")
    @RequestMapping(value = "/comm/treeData", method = RequestMethod.GET)
    public Result treeData(String id, String systemname){
        List list = ListUtils.newArrayList();
        List<Comm> comms;
        try {
            Comm comm = new Comm();
//            menu.setSysCode(systemname);
            comms = commService.findAll(comm);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Comm c : comms) {
            HashMap map = MapUtils.newHashMap();
            map.put("code", c.getAuthCode());
            map.put("name", c.getAuthName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    /**
     * 调用其他服务
     * @return
     */
    @RequestMapping(value = "/user/hello", method = RequestMethod.GET)
    public Result userhello(){
        return userClient.hello();
    }

    @ApiOperation("login")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody LoginUser loginuser){
        if(getUserAuth("user_menu_add")){

        }
        if(StringUtils.isNotEmpty(loginuser.getUsername())&&StringUtils.isNotEmpty(loginuser.getPassword())){
            if(authService.isLogin(loginuser)){
                SysUser user=authService.getSysUserByLoginCode(loginuser.getUsername());
                String token=JwtUtil.createJWT(user.getUserCode(),user.getUserName());
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("token",token);
                return new Result(true, StatusCode.OK, "登录成功",map);
            }else{
                return new Result(false, StatusCode.ERROR, "登录失败");
            }
        }else{
            return new Result(false, StatusCode.ERROR, "登录失败");
        }
    }
}
