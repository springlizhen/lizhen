package com.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.lang.StringUtils;
//import com.jeesite.common.utils.CreateJsonFile;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.JsonToXml;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.dom4j.Element;
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
@RequestMapping(value = "${adminPath}/sjml")
public class SjmlController extends BaseController{

    /**
     *
     */
    @RequestMapping(value = "index")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        model.addAttribute("id",id);
        return "modules/sys/sjml";
    }
    /**
     *
     */
    @RequestMapping(value = "findByPage")
    @ResponseBody
    public String ztfl(HttpServletRequest request, HttpServletResponse response, Model model,String id) {
        String path = "/front/re/findByPage/";
        if (StringUtils.isNotEmpty(id)) {
            path += id;
        }else{
            path += "00";
        }
        Result result = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request),path);
        return result.getData();
    }

    /**
     *
     */
    @RequestMapping(value = "apifw")
    public String apifw(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/sys/sjml-Apifw";
    }

    /**
     *
     */
    @RequestMapping(value = "jbxx")
    public String jbxx(HttpServletRequest request, HttpServletResponse response, Model model, String id, String mouth, String name) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/re/selectById/"+id);
        model.addAttribute("object", JSONObject.parse(result.getData()));
        model.addAttribute("id", id);
        model.addAttribute("mouth", mouth);
        model.addAttribute("name", name);
        return "modules/sys/sjml-jbxx";
    }

    /**
     *
     */
    @RequestMapping(value = "sjx")
    public String sjx(HttpServletRequest request, HttpServletResponse response, Model model, String id, String mouth, String name) {
        model.addAttribute("id",id);
        model.addAttribute("name", name);
        model.addAttribute("mouth", mouth);
        return "modules/sys/sjml-sjx";
    }

    /**
     *
     */
    @RequestMapping(value = "sjxData")
    @ResponseBody
    public String sjxData(HttpServletRequest request, HttpServletResponse response, Model model,String id) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/api/col/"+id);
        return result.getData();
    }

    /**
     *
     */
    @RequestMapping(value = "sjyl")
    public String sjyl(HttpServletRequest request, HttpServletResponse response, Model model, String id, String mouth, String name) {

        Result sjx = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/api/col/"+id);
        model.addAttribute("sjxObject",JSONObject.parse(sjx.getData()));

        Result sj = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/re/"+id+"/preview");
        model.addAttribute("sjObject",JSONObject.parse(sj.getData()));

        model.addAttribute("id",id);
        model.addAttribute("name", name);
        model.addAttribute("mouth", mouth);
        return "modules/sys/sjml-sjyl";
    }
    /**
     *
     */
    @RequestMapping(value = "wjxz")
    public String wjxz(HttpServletRequest request, HttpServletResponse response, Model model, String id, String mouth, String name) {
        model.addAttribute("id",id);
        model.addAttribute("name", name);
        model.addAttribute("mouth", mouth);
        Result result = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request),"/front/re/"+id+"/all_data");
        model.addAttribute("jsonData", JSONObject.parse(result.getData()));
//        XMLSerializer xmlSerializer = new XMLSerializer();
//        xmlSerializer.setTypeHintsEnabled(false); // 去除 节点中type类型
//        String xmlStr = xmlSerializer.write(JSONSerializer.toJSON(result.getData()));
//        model.addAttribute("xmlStr", xmlStr);
        return "modules/sys/sjml-wjxz";
    }

    /**
     *
     */
    @RequestMapping(value = "APIfw")
    public String APIfw(HttpServletRequest request, HttpServletResponse response, Model model, String id, String mouth, String name) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/re/find/"+id);
        JSONObject resourceApi = JSONObject.parseObject(result.getData());
        model.addAttribute("resourceApi", resourceApi);
        Result tyParams = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/apiParam/find/ty/"+resourceApi.getString("id"));
        Result syParams = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/apiParam/find/sy/"+resourceApi.getString("id"));
        model.addAttribute("tyParams",JSONObject.parse(tyParams.getData()));
        model.addAttribute("syParams",JSONObject.parse(syParams.getData()));
        model.addAttribute("id",id);
        model.addAttribute("name", name);
        model.addAttribute("mouth", mouth);
        return "modules/sys/sjml-APIfw";
    }

    @RequestMapping(value = "sjmlData")
    @ResponseBody
    public String sjmlData(HttpServletRequest request, HttpServletResponse response, Model model,String token,String userName) {
        Result result = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request),"/front/findByPage");
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
        dataMap.put("createBy", user.getUserName());
        request.setAttribute("applicant", user.getUserName());
        return OALoginUtiles.dataPost(request, dataMap, path);
    }
}
