package com.jeesite.modules.dbra.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.jeesite.common.web.BaseController.text;
import static com.jeesite.common.web.http.ServletUtils.renderResult;

/**
 * @Author : Mark_wang
 * @Date : 2019-9-23
 **/
@Controller
@RequestMapping(value = "${adminPath}/api/param")
public class ApiParamController {
    /**
     * 查询参数列表
     */
    @RequestMapping(value = "listParam")
    @ResponseBody
    public String listParam(HttpServletRequest request, HttpServletResponse response, Model model, String apiId) {
        Result data = DbraLoginUtiles.dataGet(request, null,"/apiParam/find/" + apiId);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public String delParamById(Map map, HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.dataGet(request, null, "/apiParam/del/" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    /**
     * 保存
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String, String> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if((entry.getValue().length>0) && !entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data =  DbraLoginUtiles.dataPost(request,dataMap,"/apiParam");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 编辑
     * @param request
     * @param model
     * @param map
     * @return
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, Map map, String id, String apiId) {
        model.addAttribute("apiId", apiId);
        if (StringUtils.isEmpty(id)) {
            model.addAttribute("param", null);
        } else {
            Result data = DbraLoginUtiles.dataGet(request, null, "/apiParam/findById/" + id);
            model.addAttribute("param", JSON.parse(data.getData()));
        }
        return "modules/dbra/ApiParamForm";
    }
}
