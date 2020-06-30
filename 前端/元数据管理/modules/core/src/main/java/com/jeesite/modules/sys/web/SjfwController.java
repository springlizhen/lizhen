package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "${adminPath}/sjfw")
public class SjfwController extends BaseController{


    /**
     *
     */
    @RequestMapping(value = "index")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/sys/sjfw";
    }

    /**
     *
     */
    @RequestMapping(value = "serviceClass")
    @ResponseBody
    public String serviceClass(HttpServletRequest request, HttpServletResponse response) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/ore/findServiceClass");
        return result.getData();
    }

    /**
     *
     */
    @RequestMapping(value = "findByPage")
    @ResponseBody
    public String ztfl(HttpServletRequest request, HttpServletResponse response, Model model,String token,String userName, String serviceClass, String name) {
        Result result = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request),"/front/ore/findAll/" + serviceClass + "/" + name);
        return result.getData();
    }

    /**
     *
     */
    @RequestMapping(value = "fwms")
    public String apifw(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/ore/findById/"+id);
        model.addAttribute("object", JSONObject.parse(result.getData()));
        model.addAttribute("id",id);
        return "modules/sys/sjfw-fwms";
    }

    /**
     *
     */
    @RequestMapping(value = "paramAll")
    @ResponseBody
    public String paramAll(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/param/"+id);
        return result.getData();
    }
}
