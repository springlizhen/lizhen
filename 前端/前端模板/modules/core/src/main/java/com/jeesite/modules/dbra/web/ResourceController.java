package com.jeesite.modules.dbra.web;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import com.jeesite.modules.sys.utils.OALoginUtiles;
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
 * 本地服务配置共享接口
 * @author Mark_Wang
 * @date 2019/7/17
 **/
@Controller
@RequestMapping(value = "${adminPath}/re")
public class ResourceController {

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, Model model) {
        return "modules/dbra/ResourceList";
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
        Result data = DbraLoginUtiles.dataPost(request, dataMap,"/resource/findByPage");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 共享配置
     */
    @RequestMapping(value = "api")
    public String api(HttpServletRequest request, Model model,String dbId, String tbId, String reId) {
        Result bdData = DbraLoginUtiles.dataGet(request,null,"/bd/findColCommentsByTbId/" + tbId);
        Result reApi = DbraLoginUtiles.dataGet(request, null, "/reapi/reId/" + reId);

        model.addAttribute("dbId", dbId);
        model.addAttribute("tbId", tbId);
        model.addAttribute("reId", reId);
        model.addAttribute("reApi", JSON.parse(reApi.getData()));
        model.addAttribute("bs", JSON.parseArray(bdData.getData()));
        return "modules/dbra/ResourceApiForm";
    }

    /**
     * 详情
     */
    @RequestMapping(value = "info")
    public String info(HttpServletRequest request, Model model, String id) {
        Result data = DbraLoginUtiles.dataGet(request, null, "/resource/findById/" + id);
        model.addAttribute("resource", JSON.parse(data.getData()));
        Result reApi = DbraLoginUtiles.dataGet(request, null, "/reapi/reId/" + id);
        model.addAttribute("reApi", JSON.parse(reApi.getData()));
        return "modules/dbra/ResourceInfo";
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, String tbId, String dbId, String id) {
        model.addAttribute("tbId", tbId);
        model.addAttribute("dbId", dbId);
        Result data = DbraLoginUtiles.dataGet(request, null, "/resource/findById/" + id);
        model.addAttribute("resource", JSON.parse(data.getData()));
        Result theme = DbraLoginUtiles.dataGet(request, null, "/theme");
        model.addAttribute("themes", JSON.parseArray(theme.getData()));
        return "modules/dbra/ResourceForm";
    }

    /**
     * 保存共享消息
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String, String> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if((entry.getValue().length>0) && !entry.getValue()[0].equals(""))
                if (entry.getKey().equals("columns")) {
                    String cols = "";
                    String[] columns = entry.getValue();
                    for (int i=0; i<columns.length; i++) {
                        cols+=columns[i];
                        if (i < columns.length - 1) {
                            cols+="|";
                        }
                    }
                    dataMap.put("columns", cols);
                } else if (entry.getKey().equals("whereCol")) {
                    String wcols = "";
                    String[] wcolumns = entry.getValue();
                    for (int i=0; i<wcolumns.length; i++) {
                        wcols+=wcolumns[i];
                        if (i < wcolumns.length - 1) {
                            wcols+="|";
                        }
                    }
                    dataMap.put("whereCol", wcols);
                }
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/reapi/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 根据选择字段获取生成sql
     */
    @RequestMapping(value = "findSql")
    @ResponseBody
    public String findSql(HttpServletRequest request, String tbId, String cols) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/reapi/sql/" + tbId + "/" + cols);
        if(data.getCode()==20000)
            return data.getData();
        else
            return renderResult(Global.FALSE, null);
    }

    /**
     * 根据id修改开放状态
     */
    @RequestMapping(value = "upStatus")
    @ResponseBody
    public String updateStatusById(HttpServletRequest request, String id, String status) {
        Result data = DbraLoginUtiles.dataGet(request, null, "/resource/updateStatus/" + id + "/" + status);
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping(value = "testApi")
    @ResponseBody
    public String testApi(HttpServletRequest request, String dbId, String sql) {
        Map<String, String> dataMap = MapUtils.newHashMap();
        dataMap.put("dbId", dbId);
        dataMap.put("sql", sql);
        Result data = DbraLoginUtiles.dataPost(request, dataMap,"/reapi/testApi");
        if(data.getCode()==20000)
            return data.getData();
        else
            return renderResult(Global.FALSE, null);
    }

    @RequestMapping(value = "testCount")
    @ResponseBody
    public String testCount(HttpServletRequest request, String tbId) {
        Result data = DbraLoginUtiles.dataGet(request, null,"/reapi/test/" + tbId);
        if(data.getCode()==20000)
            return data.getData();
        else
            return renderResult(Global.FALSE, null);
    }
}
