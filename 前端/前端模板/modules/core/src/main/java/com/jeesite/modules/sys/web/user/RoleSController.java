package com.jeesite.modules.sys.web.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DictionaryUtils;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/role")
public class RoleSController extends BaseController {

    @ModelAttribute
    public Map get(String roleCode, HttpServletRequest request) {
        if(roleCode!=null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("roleCode", roleCode);
            Result data = OALoginUtiles.authGet(request,dataMap,"/role/form");
            return (Map) JSON.parse(data.getData());
        }
        return null;
    }

    @RequestMapping(value = "treeData")
    @ResponseBody
    public String treeData(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/role/treeData");
        return data.getData();
    }

    @RequestMapping(value = "index")
	public String index(Model model, HttpServletRequest request) {
        model.addAttribute("role",null);
		return "modules/sys/role/roleList";
	}

    @RequestMapping(value = "formAuthDataScope")
    public String formAuthDataScope(Model model, HttpServletRequest request, Map map) {
        JSONObject role = (JSONObject) map.get("map");
        model.addAttribute("role",role);
        model.addAttribute("dataType", DictionaryUtils.setMap(null,null,"未设置","全部数据","自定义数据","本部门数据"));
        Map maprp = new HashMap();
        maprp.put("roleCode", role.get("roleCode"));
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/auth/comm/treeData");
        model.addAttribute("sysCode",DictionaryUtils.setMap((JSONArray) JSON.parse(data.getData())));
        return "modules/sys/role/roleFormAuthDataScope";
    }

	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/role/pageList");
		return data.getData();
	}

	@RequestMapping(value = "form")
	public String form(String op, Model model, HttpServletRequest request, Map map) {
		JSONObject role = (JSONObject) map.get("map");
		model.addAttribute("op",op);
		model.addAttribute("role",role);
        String menuname = DictionaryUtils.setDicData("sys_menu_sys_code", request);
        model.addAttribute("menuname", menuname);
		return "modules/sys/role/roleForm";
	}

    @RequestMapping(value = "formAuthUser")
    public String formAuthUser(Model model, HttpServletRequest request, Map map) {
        JSONObject role = (JSONObject) map.get("map");
        model.addAttribute("role",role);
        return "modules/sys/role/roleFormAuthUser";
    }

    @RequestMapping(value = "userSelect")
    public String userSelect(Model model, HttpServletRequest request, Map map) {
        JSONObject role = (JSONObject) map.get("map");
        model.addAttribute("role",role);
        return "modules/sys/role/userSelect";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response) {
        Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMapPost(request),"/role/saveAndUpdate");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 角色名称验证
     * @param oldRoleName
     * @param roleName
     * @param request
     * @return
     */
    @RequestMapping({"checkRoleName"})
    @ResponseBody
    public String checkRoleName(String oldRoleName, String roleName, HttpServletRequest request) {
        if (roleName != null && roleName.equals(oldRoleName)) {
            return "true";
        } else {
            Result data = OALoginUtiles.authGet(request,RequestParameter.requestParamMap(request),"/role/checkRoleName");
            return roleName != null && data.getData().equals("true") ? "true" : "false";
        }
    }

    /**
     * 角色菜单树
     * @param request
     * @return
     */
    @RequestMapping({"menutreeData"})
    @ResponseBody
    public String menutreeData(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/role/menutreeData");
        return data.getData();
    }

    /**
     * 停用角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "disable")
    public String disable(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request),"/role/disable");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 启用角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "enable")
    public String enable(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request),"/role/enable");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 删除角色
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request),"/role/delete");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping(value = "roleOfficeDate")
    @ResponseBody
    public String roleOfficeDate(HttpServletRequest request){
        Result roleData = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/roleData/allList");
        return roleData.getData();
    }

    @RequestMapping(value = "saveAuthDataScope")
    @ResponseBody
    public String saveAuthDataScope(HttpServletRequest request){
        Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMapPost(request),"/roleData/saveAndUpdate");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 保存角色用户
     * @param request
     * @return
     */
    @RequestMapping(value = "saveAuthUser")
    @ResponseBody
    public String saveAuthUser(HttpServletRequest request){
        Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMapPost(request),"/role/saveAuthUser");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 删除角色用户
     * @param request
     * @return
     */
    @RequestMapping(value = "deleteAuthUser")
    @ResponseBody
    public String deleteAuthUser(HttpServletRequest request){
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/role/deleteAuthUser");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
}
