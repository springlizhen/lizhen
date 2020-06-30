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
 * 数据表管理
 * @author Mark_Wang
 * @date 2019/7/8
 **/
@Controller
@RequestMapping(value = "${adminPath}/tb")
public class DbTableController {

    protected Map map = null;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if(StringUtils.isNotEmpty(id)) {
            Result data = DbraLoginUtiles.dataGet(request,null,"/tb/selectById/" + id);
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
        Result dbResult = DbraLoginUtiles.dataGet(request,null,"/db/dbIntb");
        model.addAttribute("dbs", JSON.parseArray(dbResult.getData()));
        Result tbResult = DbraLoginUtiles.dataGet(request, null, "/tb");
        model.addAttribute("tbs", JSON.parseArray(tbResult.getData()));
        return "modules/dbra/dbTableList";
    }
    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model,Map map) {
        Result dbResult = DbraLoginUtiles.dataGet(request,null,"/db");
        model.addAttribute("dbs", JSON.parseArray(dbResult.getData()));
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("tb",data);
        return "modules/dbra/dbTableForm";
    }

    /**
     * 查看详情表单
     */
    @RequestMapping(value = "info")
    public String info(HttpServletRequest request, Model model,Map map) {
        Result dbResult = DbraLoginUtiles.dataGet(request,null,"/db");
        model.addAttribute("dbs", JSON.parseArray(dbResult.getData()));
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("tb",data);
        return "modules/dbra/dbTableInfo";
    }

    /**
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
        String dbId = dataMap.get("dbid");
        String tbId = dataMap.get("tbid");
        Result data;
        if (dbId == null && tbId == null) {
            data = DbraLoginUtiles.dataPost(request, dataMap,"/tb/findAllByPage");
        } else if (dbId != null && tbId == null){
            String pageNo = dataMap.get("pageNo");
            String pageSize = dataMap.get("pageSize");
            if (StringUtils.isNotEmpty(pageNo) && StringUtils.isNotEmpty(pageSize)) {
                data = DbraLoginUtiles.dataGet(request, dataMap,"/tb/findByDbId/" + dbId + "/" + pageNo + "/" + pageSize);
            } else {
                data = DbraLoginUtiles.dataGet(request, dataMap,"/tb/findByDbId/" + dbId + "/1/20");
            }
        } else {
            data = DbraLoginUtiles.dataGet(request, null,"/tb/find/" + tbId);
        }
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 保存消息
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/tb");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 删除消息
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Map map, HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/tb/del/" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    /**
     *  获取数据库下所有的表名
     */
    @RequestMapping(value = "findTableNameByDbId")
    @ResponseBody
    public String findTableNameByDbId(HttpServletRequest request, String dbId) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/db/findDbTableName/" + dbId);
        if(data.getCode()==20000)
            return data.getData();
        else
            return renderResult(Global.FALSE, null);
    }

    /**
     * 获取表下的所有字段名
     */
    @RequestMapping(value = "findColumByTableName")
    @ResponseBody
    public String findColumByTableName(HttpServletRequest request, String dbId, String tableName) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/tb/findColumn/" + dbId + "/" + tableName);
        if(data.getCode()==20000)
            return data.getData();
        else
            return renderResult(Global.FALSE, null);
    }
}
