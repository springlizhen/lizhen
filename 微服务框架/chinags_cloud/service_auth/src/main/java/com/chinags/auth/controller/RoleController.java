package com.chinags.auth.controller;

import com.chinags.auth.dao.RoleDataScopeDao;
import com.chinags.auth.entity.Menu;
import com.chinags.auth.entity.Role;
import com.chinags.auth.entity.RoleDataScope;
import com.chinags.auth.service.MenuService;
import com.chinags.auth.service.RoleDataScopeService;
import com.chinags.auth.service.RoleService;
import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.ResultMap;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tools.ant.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api("角色管理controller")
@RestController
@CrossOrigin
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    /**
     * 获取左侧栏菜单
     * @return
     */
    @ApiOperation("获取角色")
    @RequestMapping(value = "/allList", method = RequestMethod.GET)
    public Result roleList(){
        List<Role> roles;
        try {
            Role post = new Role();
            roles = roleService.findAll(post);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        if(roles==null) {
            roles = new ArrayList<>();
        }
        return new Result(true, StatusCode.OK, "获取成功",roles);
    }

    /**
     * 获取角色信息
     * @return
     */
    @ApiOperation("获取角色信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result roleForm(String roleCode){
        Role role = new Role();
        try {
            if(StringUtils.isNotEmpty(roleCode)) {
                role = roleService.getRoleByRoleCode(roleCode);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",role);
    }

    /**
     * 角色树结构
     * @return
     */
    @ApiOperation("角色树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String systemname){
        List list = ListUtils.newArrayList();
        List<Role> roles;
        try {
            Role role = new Role();
            roles = roleService.findAll(role);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Role r : roles) {
            HashMap map = MapUtils.newHashMap();
            map.put("id", r.getId());
            map.put("pId", "0");
            map.put("name", r.getRoleName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    /**
     * 分页获取角色信息
     * @return
     */
    @ApiOperation("分页获取角色信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result postListPage(Role post){
        PageResult<Role> page;
        try {
            //类似查询全部
//            List<RoleDataScope> list = roleDataScopeService.findAll(new RoleDataScope());
            page = roleService.find(post);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    @ApiOperation("角色名称是否存在")
    @RequestMapping(value = "/checkRoleName", method = RequestMethod.GET)
    public Result checkRoleName(String roleName){
        Boolean b;
        try{
            b = roleService.findCountRolename(roleName);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",b);
    }

    @ApiOperation("保存角色")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Role role) {
        String menuCodes = role.getRoleMenuListJson();
        Role roleParent = roleService.getRoleByRoleCode(role.getRoleCode());
        if(role.getOp().equals("add")){
            if(roleParent!=null){
                return new Result(true, StatusCode.ERROR, "角色编码已存在");
            }
            role.setStatus("0");
        }
        switch (role.getOp()){
            case "edit":
                role.setMenuSet(roleParent.getMenuSet());
                role.setStatus(roleParent.getStatus());
                break;
            case "auth":
                role = roleParent;
                roleService.selectUserRole(role.getRoleCode());
            default:
                Set<Menu> menus = menuService.inMenuCode(menuCodes);
                role.setMenuSet(menus);
        }
        role.setCorpCode("0");
        role.setCorpName("Jeesite");
        role.setCreateBy(getLoginUser().getUsercode());
        role.setUpdateBy(getLoginUser().getUsercode());
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        roleService.save(role);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    /**
     * 角色菜单树结构
     * @return
     */
    @ApiOperation("角色树结构")
    @RequestMapping(value = "/menutreeData", method = RequestMethod.GET)
    public Result menutreeData(String roleCode, String systemname){
        HashMap map = MapUtils.newHashMap();
        try {
            Role role = roleService.getRoleByRoleCode(roleCode);
            if(role!=null) {
                map.put("roleMenuList", role.getMenuSet());
            }
            Menu menu = new Menu();
//            menu.setSysCode(systemname);
            List<Menu> menus = menuService.findAllS(menu);
            ResultMap<Menu> projectResultMap = new ResultMap<>();
            projectResultMap.setEntityList(menus);
            Map<String, List<Menu>> sysCode = projectResultMap.resultList(true, "sysCode");
            HashMap<String,List> defaults = MapUtils.newHashMap();
            for (String key : sysCode.keySet()){
                List mlist = ListUtils.newArrayList();
                for (Menu m : sysCode.get(key)) {
                    HashMap rmap = MapUtils.newHashMap();
                    rmap.put("id", m.getId());
                    rmap.put("pId", m.getParentCode());
                    rmap.put("name", m.getMenuName());
                    rmap.put("title", m.getMenuName());
                    mlist.add(rmap);
                }
                defaults.put(key, mlist);
            }
//            List<Map.Entry<String, List>> list = new ArrayList<>(defaults.entrySet());
//            list.sort((o1, o2) -> o2.getValue().size() - o1.getValue().size());
            map.put("menuMap", defaults);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        return new Result(true, StatusCode.OK, "获取成功",map);
    }

    @ApiOperation("删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String userCode) {
        Role role = roleService.delete(userCode);
        if(role!=null) {
            return new Result(true, StatusCode.OK, "删除角色\""+role.getRoleName()+"\"成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "删除角色\""+role.getRoleName()+"\"失败");
        }
    }

    @ApiOperation("停用角色")
    @ResponseBody
    @RequestMapping(value = "disable", method = RequestMethod.GET)
    public Result disable(String id) {
        Role role = roleService.status(id,"2");
        if(role!=null) {
            return new Result(true, StatusCode.OK, "停用角色\""+role.getRoleName()+"\"成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "停用角色\""+role.getRoleName()+"\"失败");
        }
    }

    @ApiOperation("启用角色")
    @ResponseBody
    @RequestMapping(value = "enable", method = RequestMethod.GET)
    public Result enable(String id) {
        Role role = roleService.status(id,"0");
        if(role!=null) {
            return new Result(true, StatusCode.OK, "启用角色\""+role.getRoleName()+"\"成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "启用角色\""+role.getRoleName()+"\"失败");
        }
    }

    @ApiOperation("保存角色用户")
    @RequestMapping(value = "/saveAuthUser", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAuthUser(@RequestBody Role role) {
        roleService.saveAuthUser(role);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除角色用户")
    @RequestMapping(value = "/deleteAuthUser", method = RequestMethod.GET)
    @ResponseBody
    public Result deleteAuthUser(String roleCode, String userRoleString) {
        roleService.deleteAuthUser(roleCode,userRoleString);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
