package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/service")
public class ServiceController extends BaseController{

    /**
     * 申请记录
     */
    @RequestMapping(value = "record")
    public String record(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 当前用户对象信息
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User user = (User)request.getSession().getAttribute(cookie);
        model.addAttribute("user", user);
        return "modules/sys/record";
    }

    @RequestMapping(value = "recordList")
    @ResponseBody
    public String recordList(HttpServletRequest request, HttpServletResponse response, Model model,String token,String userName) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/sa/findByUser");
        return result.getData();
    }

    @RequestMapping(value = "applyService")
    @ResponseBody
    public Result applyService(HttpServletRequest request) {
        String path = "/service/save";
        Map<String, Object> dataMap = RequestParameter.requestParamMap(request);
        // 当前用户对象信息
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        User user = (User)request.getSession().getAttribute(cookie);
        dataMap.put("applicant", user.getUserName());
        dataMap.put("createDate", new Date());
        return OALoginUtiles.dataPost(request, dataMap, path);
    }
}
