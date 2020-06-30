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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.jeesite.common.web.BaseController.text;
import static com.jeesite.common.web.http.ServletUtils.renderResult;

/**
 * @author Mark_Wang
 * @date 2019/7/22
 **/
@Controller
@RequestMapping(value = "${adminPath}/reg")
public class RegistractionController {

    protected Map map = null;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if(StringUtils.isNotEmpty(id)) {
            Result data = DbraLoginUtiles.dataGet(request,null,"/sa/findById/" + id);
            map = (Map) JSON.parse(data.getData());
            return map;
        }
        return null;
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, Model model) {
        return "modules/dbra/RegistrationList";
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
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,String> dataMap = MapUtils.newHashMap();
        String key = null;
        String value = null;
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else if (entry.getKey().equals("key"))
                    key = (entry.getValue()[0]);
                else if (entry.getKey().equals("value"))
                    value = (entry.getValue()[0]);
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value))
            dataMap.put(key, value);
        Result data = DbraLoginUtiles.dataPost(request, dataMap,"/sa/findByPage");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 审批
     * @param request
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "ups")
    @ResponseBody
    public String updateStatusById(HttpServletRequest request, String id, String status) {
        Result data = DbraLoginUtiles.dataGet(request, null, "/sa/ups/" + id + "/" + status);
        if (data.getCode() == 20000) {
            return renderResult(Global.TRUE, text("成功"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping(value = "info")
    public String info(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("reg",data);
        return "modules/dbra/RegistrationInfo";
    }

}
