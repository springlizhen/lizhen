package com.chinags.auth.controller;

import com.chinags.auth.entity.Menu;
import com.chinags.auth.service.MenuService;
import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 菜单类
 *
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("菜单管理controller")
@RestController
@CrossOrigin
@RequestMapping("/menu")
public class MenuController extends BaseController {


    @Autowired
    private MenuService menuService;

    /**
     * 获取左侧栏菜单
     * @return
     */
    @ApiOperation("获取左侧栏菜单")
    @RequestMapping(value = "/leftList", method = RequestMethod.GET)
    public Result menuList(String systemname){
        List<Menu> menus;
        try {
            Menu menu = new Menu();
            menu.setSysCode(systemname);
            String usercode = getLoginUser().getUsercode();
            if(usercode.equals("system")) {
                menus = menuService.findAll(menu);
            }else{
                menus = menuService.menuList(usercode, systemname);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        if(menus==null) {
            menus = new ArrayList<>();
        }
        return new Result(true, StatusCode.OK, "获取成功",menus);
    }

    /**
     * 分页获取菜单信息
     * @return
     */
    @ApiOperation("分页获取菜单信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result menuListPage(Menu menu){
        PageResult<Menu> page;
        try {
            String parentCode = menu.getParentCode()==null?"0":menu.getParentCode();
            menu.setParentCode(parentCode);
//            menu.setSysCode(getLoginUser().getSystemname());
            //类似查询全部
            menu.setPageSize(100000000);
            page = menuService.find(menu);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取菜单信息
     * @return
     */
    @ApiOperation("获取菜单信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result menuForm(String menuCode,String parentCode){
        Menu menu = new Menu();
        try {
            if(StringUtils.isEmpty(parentCode)) {
                menu = menuService.getMenuByMenuCode(menuCode);
            } else {
                Menu m = menuService.getMenuByMenuCode(parentCode);
                menu.setParentCode(m.getMenuCode());
                menu.setTreeNames(m.getMenuName()+"/"+m.getMenuName()+"/"+m.getMenuName());
            }
            menu.setMenuColor(StringUtils.isEmpty(menu.getMenuColor())?"#ddd":menu.getMenuColor());
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",menu);
    }

    /**
     * 菜单树结构
     * @return
     */
    @ApiOperation("菜单树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String id, String systemname){
        List list = ListUtils.newArrayList();
        List<Menu> menus;
        try {
            Menu menu = new Menu();
//            menu.setSysCode(systemname);
            menus = menuService.findAll(menu);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Menu m : menus) {
            if(StringUtils.isNotEmpty(id)){
                if(m.getId().equals(id)||m.getParentCodes().contains(id)) {
                    continue;
                }
            }
            HashMap map = MapUtils.newHashMap();
            map.put("id", m.getId());
            map.put("pId", m.getParentCode());
            map.put("name", m.getMenuName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    @ApiOperation("保存菜单")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Menu menu) {
        String sysCode = menu.getSysCode();
        if(sysCode.equals("bgoa")){
            menu.setSysCode("default");
        }
        menu.setCreateBy(getLoginUser().getUsercode());
        menu.setUpdateBy(getLoginUser().getUsercode());
        menu.setCreateDate(new Date());
        menu.setUpdateDate(new Date());
        this.menuService.save(menu);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除菜单")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(Menu menu) {
        if(menuService.delete(menu)) {
            return new Result(true, StatusCode.OK, "删除成功");
        } else {
            return new Result(true, StatusCode.ERROR, "请先删除子菜单");
        }
    }

//    @ApiOperation("保存排序")
//    @RequestMapping(value = "/updateTreeSort", method = RequestMethod.GET)
//    @ResponseBody
//    public Result save(String[] ids, String[] sorts) {
//        List lists = new ArrayList();
//        for(int i = 0; i < ids.length; i++) {
//            if(StringUtils.isNotEmpty(ids[i])) {
//                Menu menu = new Menu(ids[i]);
//                menu.setTreeSort(sorts[i] == null ? "30" : sorts[i]);
//                lists.add(menu);
//            }
//        }
//        this.menuService.updateTree(lists);
//        return new Result(true, StatusCode.OK, "保存成功");
//    }
}
