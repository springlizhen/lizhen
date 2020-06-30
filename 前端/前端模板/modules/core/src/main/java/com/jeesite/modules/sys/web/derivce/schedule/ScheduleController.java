package com.jeesite.modules.sys.web.derivce.schedule;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @Author : Mark_wang
 * @Date : 2019-9-10
 **/
@Controller
@RequestMapping(value = "${adminPath}/sys/device/schedule")
public class ScheduleController  extends BaseController {
    /**
     * 列表
     */
    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, Model model) {
        Result data = OALoginUtiles.dataGet(request, null, "/schedule/find/year");
        System.out.println(data.getData());
        System.out.println(JSON.parseArray(data.getData()));
        model.addAttribute("years", JSON.parseArray(data.getData()));
        return "modules/sys/derivce/schedule/scheduleList";
    }

    /**
     * 列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model) {
        Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/schedule/find");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public String del(HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/schedule/del/" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    /**
     * 详情
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, String id) {
        if (StringUtils.isEmpty(id)) {
            model.addAttribute("schedule", null);
        } else {
            Result data = DbraLoginUtiles.dataGet(request,null,"/schedule/findById/" + id);
            model.addAttribute("schedule", JSON.parse(data.getData()));
        }
        // 清理添加了项目但是没有保存的进度的项目脏数据
        DbraLoginUtiles.dataGet(request, null, "/schedulePro/deleteOldByCreateBy");
        return "modules/sys/derivce/schedule/scheduleForm";
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/schedule/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

}
