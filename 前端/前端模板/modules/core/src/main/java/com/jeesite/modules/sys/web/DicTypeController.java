package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.DictType;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DictionaryUtils;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/sys/dicType")
public class DicTypeController extends BaseController {

    /**
     * 获取字典
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if(id!=null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("id", id);
            Result data = OALoginUtiles.authGet(request,dataMap,"/dicType/form");
            return (Map) JSON.parse(data.getData());
        }
        return null;
    }

    /**
     * 字典列表
     */
    @RequestMapping(value = "list")
    public String list(Model model, HttpServletRequest request) {
        String sys_status = DictionaryUtils.setDicData("sys_status", request);
        String sys_yes_no = DictionaryUtils.setDicData("sys_yes_no", request);
        model.addAttribute("sys_status", sys_status);
        model.addAttribute("sys_yes_no", sys_yes_no);
        List sys_yes_no_list = DictionaryUtils.setDicDataList("sys_yes_no", request);
        model.addAttribute("sys_yes_no_list", sys_yes_no_list);
        List sys_search_status = DictionaryUtils.setDicDataList("sys_search_status", request);
        model.addAttribute("sys_search_status", sys_search_status);
        return "modules/sys/dictTypeList";
    }

    /**
     * 查询字典数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicType/pageList");
        return data.getData();
    }

    /**
     * 查看编辑字典
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, Map map) {
        // 创建并初始化下一个节点信息
        JSONObject dicType = (JSONObject) map.get("map");
        model.addAttribute("dicType",dicType);
        List sys_yes_no = DictionaryUtils.setDicDataList("sys_yes_no", request);
        model.addAttribute("sys_yes_no",sys_yes_no);
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/auth/comm/treeData");
        model.addAttribute("sysCode",DictionaryUtils.setMap((JSONArray) JSON.parse(data.getData())));
        return "modules/sys/dictTypeForm";
    }

    /**
     * 保存字典
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMap(request),"/dicType/saveAndUpdate");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 停用字典
     */
    @RequestMapping(value = "disable")
    @ResponseBody
    public String disable(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicType/disable");
        if(data.getCode()==20000){
            return renderResult(Global.TRUE, data.getMessage());
        }else{
            return renderResult(Global.FALSE, data.getMessage());
        }
    }

    /**
     * 启用字典
     */
    @RequestMapping(value = "enable")
    @ResponseBody
    public String enable(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicType/enable");
        return renderResult(Global.TRUE, data.getMessage());
    }

    /**
     * 删除字典
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicType/delete");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 验证字典类型
     */
    @RequestMapping({"checkDictType"})
    @ResponseBody
    public String checkDictType(String oldDictType, String dictType, HttpServletRequest request) {
        DictType a;
        (a = new DictType()).setDictType(dictType);
        if (dictType != null && dictType.equals(oldDictType)) {
            return "true";
        } else {
            Result data = OALoginUtiles.authGet(request,RequestParameter.requestParamMap(request),"/dicType/checkDictType");
            return dictType != null && data.getData().equals("true") ? "true" : "false";
        }
    }

    @RequestMapping(value = "selectDicType")
    @ResponseBody
    public String selectContract(HttpServletRequest request, HttpServletResponse response) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicType/selectDicType");
        return data.getData();
    }

    /**
     * 查询字典数据
     */
    @RequestMapping(value = "listDataDic")
    @ResponseBody
    public String listDataDic(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/dicType/pageList");
        return data.getData();
    }

    /**
     * 查看编辑字典
     */
    @RequestMapping(value = "formDic")
    public String formDic(HttpServletRequest request, Model model, Map map) {
        // 创建并初始化下一个节点信息
        JSONObject dicType = (JSONObject) map.get("map");
        model.addAttribute("dicType",dicType);
        List sys_yes_no = DictionaryUtils.setDicDataList("sys_yes_no", request);
        model.addAttribute("sys_yes_no",sys_yes_no);
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/auth/comm/treeData");
        model.addAttribute("sysCode",DictionaryUtils.setMap((JSONArray) JSON.parse(data.getData())));
        return "modules/sys/plan/dictTypeForm";
    }

    /**
     * 字典列表
     */
    @RequestMapping(value = "listDic")
    public String listDic(Model model, HttpServletRequest request) {
        String sys_status = DictionaryUtils.setDicData("sys_status", request);
        String sys_yes_no = DictionaryUtils.setDicData("sys_yes_no", request);
        model.addAttribute("sys_status", sys_status);
        model.addAttribute("sys_yes_no", sys_yes_no);
        List sys_yes_no_list = DictionaryUtils.setDicDataList("sys_yes_no", request);
        model.addAttribute("sys_yes_no_list", sys_yes_no_list);
        List sys_search_status = DictionaryUtils.setDicDataList("sys_search_status", request);
        model.addAttribute("sys_search_status", sys_search_status);
        return "modules/sys/plan/dictTypeList";
    }
}
