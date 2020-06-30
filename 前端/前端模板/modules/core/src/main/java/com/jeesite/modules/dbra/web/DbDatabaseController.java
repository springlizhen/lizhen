package com.jeesite.modules.dbra.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 数据库管理
 * @author Mark_Wang
 * @date 2019/7/4
 **/
@Controller
@RequestMapping(value = "${adminPath}/db")
public class DbDatabaseController extends BaseController {

    protected Map map = null;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if(StringUtils.isNotEmpty(id)) {
            Result data = DbraLoginUtiles.dataGet(request,null,"/db/find/" + id);
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
        Result result = DbraLoginUtiles.dataGet(request,null,"/db");
        model.addAttribute("dbs", JSON.parseArray(result.getData()));
        return "modules/dbra/dbDatabaseList";
    }
    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("db",data);
        return "modules/dbra/dbDatabaseForm";
    }

    /**
     * 查看详情表单
     */
    @RequestMapping(value = "info")
    public String info(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("db",data);
        return "modules/dbra/dbDatabaseInfo";
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
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,String> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data;
        String dbId = dataMap.get("id");
        if (dbId == null) {
            data = DbraLoginUtiles.dataPost(request, dataMap,"/db/selectAll/");
        } else {
            data = DbraLoginUtiles.dataGet(request, dataMap,"/db/findById/" + dbId);
        }
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/db/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Map map, HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/db/del/" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    /**
     * 测试数据库连接
     */
    @RequestMapping(value = "check")
    @ResponseBody
    public String checkConn(HttpServletRequest request, String address, String user, String pwd, String serviceName) {
        Map<String, String> dataMap = MapUtils.newHashMap();
        dataMap.put("address", address);
        dataMap.put("dbUser", user);
        dataMap.put("dbPwd", pwd);
        dataMap.put("serviceName", serviceName);
        Result data = DbraLoginUtiles.dataPost(request, dataMap, "/db/checkConn");
        if (data.getCode()==20000)
            return renderResult(Global.TRUE, text(data.getMessage()));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

}
