package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DictionaryUtils;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.SystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/sys/gisdeskapproval")
public class GisDeskApprovalController extends BaseController {

    /**
     * 获取字典
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if(id!=null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("id", id);
            Result data = null;
            try {
                data = OALoginUtiles.sysGet(request,"gisdesk",dataMap,"/sgeLayerApproval/form");
            } catch (SystemException e) {
                e.printStackTrace();
            }
            return (Map) JSON.parse(data.getData());
        }
        return null;
    }

    /**
     * 字典列表
     */
    @RequestMapping(value = "form")
    public String form(Model model, HttpServletRequest request, Map map) {
        // 创建并初始化下一个节点信息
        JSONObject gisdesk = (JSONObject) map.get("map");
        model.addAttribute("gisdesk",gisdesk);
        return "modules/gisdesk/gisDeskApprovalForm";
    }

    /**
     * 字典列表
     */
    @RequestMapping(value = "list")
    public String list(Model model, HttpServletRequest request) {
        return "modules/gisdesk/gisDeskApprovalList";
    }

    /**
     * 查询数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "gisdesk", RequestParameter.requestParamMap(request),"/sgeLayerApproval/findList");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "gisdesk", RequestParameter.requestParamMap(request),"/sgeLayerApproval/delete");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 保存
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request, "gisdesk", RequestParameter.requestParamMap(request),"/sgeLayerApproval/save");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
}
