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
 * 工程Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/enginnering")
public class EnginneringController extends BaseController {

    /**
     * 添加工程
     */
    @RequestMapping(value = "addPlanPro")
    public String planPro(Model model, HttpServletRequest request) {
        List type = DictionaryUtils.setDicDataListSelect("sys_plan_type", request);
        model.addAttribute("type", type);
        return "modules/sys/plan/addPlanPro";
    }

    /**
     * 保存工程
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/enginnering/save");
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
    /**
     * 获取工程
     */
    @PostMapping(value = "listDateTo")
    @ResponseBody
    public String listDateTo(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request, "systemhttp", RequestParameter.requestParamMap(request), "/enginnering/listDateTo");
        } catch (SystemException e) {
            e.printStackTrace();
        }

            return data.getData();


    }

    /**
     * 获取养护次数
     */
    @PostMapping(value = "listDateToKTo")
    @ResponseBody
    public String listDateToKTo(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request, "systemhttp", RequestParameter.requestParamMap(request), "/enginnering/listDateToKTo");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();


    }

    /**
     * 工程
     */
    @RequestMapping(value = "findAll")
    @ResponseBody
    public String findAll(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/enginnering/findAll");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return data.getData();
    }

}
