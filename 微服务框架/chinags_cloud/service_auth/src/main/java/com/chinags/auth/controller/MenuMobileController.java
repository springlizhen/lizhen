package com.chinags.auth.controller;

import com.chinags.auth.entity.Menu;
import com.chinags.auth.service.MenuService;
import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.LoginUser;
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
 * 菜单类（移动端）
 *
 * @author suijinchi
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("移动端菜单管理controller")
@RestController
@CrossOrigin
@RequestMapping("/menuMobile")
public class MenuMobileController extends BaseController {


    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单列表
     * @return
     */
    @ApiOperation("获取菜单列表")
    @RequestMapping(value = "/getMenuList", method = RequestMethod.GET)
    public Result menuList(String usercode){
        List list = ListUtils.newArrayList();
        List<Menu> menus;
        try {
            String systemname = "mobileapp";
            Menu menu = new Menu();
            menu.setSysCode(systemname);
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
        for (Menu m : menus) {
            HashMap map = MapUtils.newHashMap();
            map.put("menuCode", m.getMenuCode());
            map.put("menuName", m.getMenuName());
            map.put("parentName", m.getParentName());
            map.put("parentCode", m.getParentCode());
            map.put("treeLevel", m.getTreeLevel());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }

}
