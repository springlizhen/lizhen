//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.msg.entity.content.BaseMsgContent;
import com.jeesite.modules.sys.entity.Menu;
import com.jeesite.modules.sys.entity.Module;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.service.MenuService;
import com.jeesite.modules.sys.service.ModuleService;
import com.jeesite.modules.sys.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "${adminPath}/menus")
public class MenusController extends BaseController {

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String menuCode, String parentCode, HttpServletRequest request) {
        if(menuCode!=null || parentCode!=null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("menuCode", menuCode);
            dataMap.put("parentCode", parentCode);
            Result data = OALoginUtiles.authGet(request,dataMap,"/menu/form");
            return (Map) JSON.parse(data.getData());
        }
        return null;
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
        return "themes/default/modules/sys/menuList";
    }

    @RequestMapping({"listData"})
    @ResponseBody
    public String listData(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/menu/pageList");
        return data.getData();
    }

    @RequestMapping({"form"})
    public String form(HttpServletRequest request, Model model,Map map) {
        JSONObject menu = (JSONObject) map.get("map");
        model.addAttribute("menu",menu);
        model.addAttribute("menuType", DictionaryUtils.setMap(1L,null,"菜单","权限"));
        model.addAttribute("isShow", DictionaryUtils.setMap(null,null,"隐藏","显示"));
        model.addAttribute("moduleCodes", DictionaryUtils.setMapKey(new String[]{"core","filemanager","cms"},"核心模块","文件管理","内容管理"));
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/auth/comm/treeData");

        model.addAttribute("sysCode",DictionaryUtils.setMap((JSONArray) JSON.parse(data.getData())));
        return "themes/default/modules/sys/menuForm";
    }

    @RequestMapping({"treeData"})
    @ResponseBody
    public String treeData(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/menu/treeData");
        return data.getData();
    }

    @PostMapping({"save"})
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMap(request),"/menu/saveAndUpdate");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
//
    @RequestMapping({"delete"})
    @ResponseBody
    public String delete(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/menu/delete");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping({"logout"})
    @ResponseBody
    public String logout(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/menu/delete");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }



}
