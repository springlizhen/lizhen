package com.jeesite.modules.comm.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.msg.entity.MsgInner;
import com.jeesite.modules.msg.entity.MsgInnerRecord;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Controller
@RequestMapping(value = "${adminPath}/comm")
public class CommController extends BaseController {

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if(id!=null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("id", id);
            Result data = OALoginUtiles.authGet(request,dataMap,"/auth/comm/form");
            return  (Map) JSON.parse(data.getData());
        }
        return null;
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/comm/commList";
    }
    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        model.addAttribute("comm",o);
        return "modules/comm/commForm";
    }

    /**
     * Json数据参数例子
     * {
     *  count:20,
     *  pageNo:1,
     *  pageSize:20,
     *  list:[]
     * }
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/auth/comm/pageList");
        return data.getData();
    }

    /**
     * 保存消息
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = OALoginUtiles.authPost(request,RequestParameter.requestParamMapPost(request),"/auth/saveAndUpdate");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存消息成功！"));
        }else {
            return renderResult(Global.FALSE, text("保存消息失败！"));
        }
    }

    /**
     * 删除消息
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Map map, HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request,RequestParameter.requestParamMap(request),"/auth/comm/delete");
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除消息成功！"));
        else
            return renderResult(Global.FALSE, text("删除消息失败！"));
    }
}
