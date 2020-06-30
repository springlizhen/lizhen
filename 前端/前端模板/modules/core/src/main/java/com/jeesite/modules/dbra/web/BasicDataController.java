package com.jeesite.modules.dbra.web;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static com.jeesite.common.web.BaseController.text;
import static com.jeesite.common.web.http.ServletUtils.renderResult;

/**
 * 元数据表信息查询接口
 * @author Mark_Wang
 * @date 2019/7/16
 **/
@Controller
@RequestMapping(value = "${adminPath}/basic")
public class BasicDataController {

    /**
     * 数据库
     */
    @RequestMapping(value = {"menu", ""})
    public String menu(HttpServletRequest request, Model model) {
        Result result = DbraLoginUtiles.dataGet(request,null,"/db");
        model.addAttribute("dbs", JSON.parseArray(result.getData()));
        return "modules/dbra/basicDataMenu";
    }

    @RequestMapping(value = "db")
    @ResponseBody
    public String dbList(HttpServletRequest request, HttpServletResponse response) {
        Result result = DbraLoginUtiles.dataGet(request,null,"/db");
        if (result.getCode() == 20000) {
            return result.getData();
        } else {
            return renderResult(Global.FALSE, text(result.getMessage()));
        }
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, Model model, String dbId) {
        model.addAttribute("dbId", dbId);
        if (StringUtils.isNotEmpty(dbId)) {
            Result data = DbraLoginUtiles.dataGet(request,null,"/db/findTable/" + dbId);
            model.addAttribute("tbs", JSON.parseArray(data.getData()));
        } else {
            model.addAttribute("tbs", null);
        }
        return "modules/dbra/basicDataList";
    }

    @CrossOrigin
    @RequestMapping(value = {"web", ""})
    public String web() {
        return "modules/dbra/WebUpload";
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
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model, String dbId, String tbName) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String, String> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if((entry.getValue().length>0) && !entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        String pageNo = dataMap.get("pageNo");
        String pageSize = dataMap.get("pageSize");
        Result data;
        if (StringUtils.isNotEmpty(pageNo) && StringUtils.isNotEmpty(pageSize)) {
            System.out.println("有页数");
            data = DbraLoginUtiles.dataPost(request,null,"/bd/find/"+dbId+"/"+tbName+"/"+pageNo+"/"+pageSize);
        } else {
            data = DbraLoginUtiles.dataPost(request,null,"/bd/find/"+dbId+"/"+tbName+"/1/20");
        }
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model,Map map, String dbId, String tbName, String id) {
        if (StringUtils.isEmpty(dbId) || StringUtils.isEmpty(tbName)) {
            return renderResult(Global.FALSE, text("请选择数据库和数据表"));
        }
        model.addAttribute("dbId", dbId);
        model.addAttribute("tbName", tbName);
        if (StringUtils.isNotEmpty(id)) {
            Result result = DbraLoginUtiles.dataGet(request,null,"/bd/findById/"+dbId+"/"+tbName + "/" + id);
            System.out.println(JSON.toJSONString(result.getData()));
            model.addAttribute("cols", JSON.parseArray(result.getData()));
        } else {
            Result dbResult = DbraLoginUtiles.dataGet(request,null,"/bd/findColComments/"+dbId+"/"+tbName);
            model.addAttribute("cols", JSON.parseArray(dbResult.getData()));
        }
        return "modules/dbra/basicDataForm";
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "cols")
    @ResponseBody
    public String cols(HttpServletRequest request, String dbId, String tbName) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/bd/findColComments/"+dbId+"/"+tbName);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, data.getMessage());
        }
    }

    /**
     * 查看数据库下的表信息
     * @param request
     * @param dbId
     * @return
     */
    @RequestMapping(value = "findTable")
    @ResponseBody
    public String findTable(HttpServletRequest request, String dbId) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/db/findTable/" + dbId);
        if(data.getCode()==20000)
            return data.getData();
        else
            return renderResult(Global.FALSE, data.getMessage());
    }

    @RequestMapping(value = "save")
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/bd/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public String delete(Map map, HttpServletRequest request, String dbId, String tbName, String id) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/bd/del/" + dbId + "/" + tbName + "/" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }
}
