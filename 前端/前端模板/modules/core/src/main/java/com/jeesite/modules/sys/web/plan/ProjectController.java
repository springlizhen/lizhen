/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.plan;

import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DictionaryUtils;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.SystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 项目Controller
 * @author ThinkGem
 * @version 2019-10-14
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/project")
public class ProjectController extends BaseController {

    /**
     * 添加项目
     */
    @RequestMapping(value = "addPlanItem")
    public String addPlanItem(Model model, HttpServletRequest request) {
        return "modules/sys/plan/addPlanItem";
    }

    /**
     * 添加项目
     */
    @RequestMapping(value = "addPlanItemMsg")
    public String addPlanItemMsg(Model model, HttpServletRequest request) {
        List type = DictionaryUtils.setDicDataListSelect("sys_plan_item_type", request);
        model.addAttribute("type", type);
        return "modules/sys/plan/addPlanItemMsg";
    }

    /**
     * 查询项目数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/project/pageList");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
    }

    /**
     * 保存项目
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/project/saveAndUpdate");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if (data.getCode()==20000) {
            String s = renderResult(Global.TRUE, text(data.getData()));
            return s;
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 删除项目
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request, "systemhttp", RequestParameter.requestParamMap(request),"/project/delete");
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

}
