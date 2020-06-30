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
@RequestMapping(value = "${adminPath}/daml")
public class DamlController extends BaseController{

    /**
     *
     */
    @RequestMapping(value = "index")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/sys/daml";
    }

    /**
     *
     */
    @RequestMapping(value = "type")
    @ResponseBody
    public String type(HttpServletRequest request, HttpServletResponse response) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/file/findType");
        return result.getData();
    }

    /**
     *
     */
    @RequestMapping(value = "findByPage")
    @ResponseBody
    public String ztfl(HttpServletRequest request, HttpServletResponse response, Model model,String token,String userName,String type) {
        Result result = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request),"/front/file/find/" + type);
        return result.getData();
    }

    /**
     *
     */
    @RequestMapping(value = "xq")
    public String apifw(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/file/findById/"+id);
        model.addAttribute("object", JSONObject.parse(result.getData()));
        model.addAttribute("id",id);
        return "modules/sys/damu-xq";
    }
}
