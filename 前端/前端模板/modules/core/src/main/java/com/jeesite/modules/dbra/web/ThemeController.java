package com.jeesite.modules.dbra.web;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
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
 * 主题分类
 * @Author : Mark_wang
 * @Date : 2019-9-19
 **/
@Controller
@RequestMapping(value = "${adminPath}/theme")
public class ThemeController {
    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request) {
        return "modules/dbra/ThemeList";
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
        Result data = DbraLoginUtiles.dataPost(request, dataMap,"/theme/findAllByPage");
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
    public String form(HttpServletRequest request, Model model, String id) {
        Result dbResult = DbraLoginUtiles.dataGet(request,null,"/theme/findById/" + id);
        model.addAttribute("theme", JSON.parse(dbResult.getData()));
        return "modules/dbra/ThemeForm";
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/theme/save");
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
        Result data = DbraLoginUtiles.dataGet(request,null,"/theme/del/" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }
}
