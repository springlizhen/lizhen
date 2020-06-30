package com.jeesite.modules.dbra.web;

import com.alibaba.fastjson.JSON;
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
 * 本地服务提供开放接口
 * @author Mark_Wang
 * @date 2019/7/17
 **/
@Controller
@RequestMapping(value = "${adminPath}/res")
public class ResourceRegController {

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, Model model) {
        Result dbResult = DbraLoginUtiles.dataGet(request,null,"/db/dbIntb");
        model.addAttribute("dbs", JSON.parseArray(dbResult.getData()));
        Result tbResult = DbraLoginUtiles.dataGet(request, null, "/tb");
        model.addAttribute("tbs", JSON.parseArray(tbResult.getData()));
        return "modules/dbra/dbResTableList";
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
        Result data;
        String dbId = dataMap.get("dbId");
        String tbId = dataMap.get("id");
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
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, String tbId, String dbId) {
        model.addAttribute("tbId", tbId);
        model.addAttribute("dbId", dbId);
        model.addAttribute("resource", null);
        Result theme = DbraLoginUtiles.dataGet(request, null, "/theme");
        model.addAttribute("themes", JSON.parseArray(theme.getData()));
        return "modules/dbra/ResourceForm";
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/resource/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
}
