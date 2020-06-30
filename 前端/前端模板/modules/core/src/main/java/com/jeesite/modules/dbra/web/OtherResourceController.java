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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static com.jeesite.common.web.BaseController.text;
import static com.jeesite.common.web.http.ServletUtils.renderResult;

/**
 * 第三方服务接口
 * @author Mark_Wang
 * @date 2019/7/18
 **/
@Controller
@RequestMapping(value = "${adminPath}/or")
public class OtherResourceController {

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, Model model) {
        Result statuses = DbraLoginUtiles.dataGet(request, null,"/or/findStatus");
        Result services = DbraLoginUtiles.dataGet(request, null,"/or/findServiceClass");
        Result accesses = DbraLoginUtiles.dataGet(request, null,"/or/findAcessType");
        model.addAttribute("statuses", JSON.parseArray(statuses.getData()));
        model.addAttribute("services", JSON.parseArray(services.getData()));
        model.addAttribute("accesses", JSON.parseArray(accesses.getData()));
        return "modules/dbra/OtherResourceList";
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
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = DbraLoginUtiles.dataPost(request, dataMap,"/or/findByEntity");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 修改资源状态
     * @param request
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "ups")
    @ResponseBody
    public String updateStatusById(HttpServletRequest request, String id, String status) {
        Result data = DbraLoginUtiles.dataGet(request, null, "/or/updateStatusById/" + id + "/" + status);
        if (data.getCode() == 20000) {
            return renderResult(Global.TRUE, text("成功"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, Map map, String id) {
        if (id.equals("0000"))
            DbraLoginUtiles.dataGet(request, null, "/apiParam/delByApiId/" + id);
        Result data = DbraLoginUtiles.dataGet(request,null,"/or/findById/" + id);
        model.addAttribute("othres", JSON.parse(data.getData()));
        return "modules/dbra/OtherResourceForm";
    }

    @RequestMapping(value = "info")
    public String info(HttpServletRequest request, Model model, Map map, String id) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/or/findById/" + id);
        model.addAttribute("othres", JSON.parse(data.getData()));
        return "modules/dbra/OtherResourceInfo";
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public String deleteById(Map map, HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.dataGet(request, null, "/or/del/"+id);
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
        Result data = null;
        if (dataMap.get("check").equalsIgnoreCase("true")) {
            if (dataMap.get("createBy") != null) {
                data = DbraLoginUtiles.dataPost(request,dataMap,"/or/update");
            } else {
                data = DbraLoginUtiles.dataPost(request,dataMap,"/or/save");
            }
            if(data.getCode()==20000) {
                return renderResult(Global.TRUE, text("保存成功！"));
            } else {
                return renderResult(Global.FALSE, text(data.getMessage()));
            }
        } else {
            return null;
        }
    }

}
