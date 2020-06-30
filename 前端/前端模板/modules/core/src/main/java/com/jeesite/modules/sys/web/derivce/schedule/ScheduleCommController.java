package com.jeesite.modules.sys.web.derivce.schedule;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : Mark_wang
 * @Date : 2019-9-16
 **/
@Controller
@RequestMapping(value = "${adminPath}/sys/device/scheduleComm")
public class ScheduleCommController extends BaseController {
    /**
     * 列表数据
     */
    @RequestMapping(value = "listData")
    public String listData(HttpServletRequest request, Model model, String scheduleId) {
        Result data = OALoginUtiles.dataGet(request, null, "/scheduleComm/findByScheduleId/" + scheduleId);
        model.addAttribute("scheduleComms", JSON.parseArray(data.getData()));
        model.addAttribute("scheduleId", scheduleId);
        return "modules/sys/derivce/schedule/scheduleCommInfo";
    }

    /**
     * 保存
     */
    @PostMapping(value = "saves")
    @ResponseBody
    public String saves(HttpServletRequest request) {
        Map<String,String[]> dataParam = request.getParameterMap();
        List<Map> dataList = new ArrayList<>();
        int size = 0;
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            size = entry.getValue().length;
            break;
        }
        for (int i=0; i<size; i++) {
            Map<String, String> dataMap = MapUtils.newHashMap();
            for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
                if((entry.getValue().length>0) && !entry.getValue()[i].equals("")) {
                    dataMap.put(entry.getKey(),(entry.getValue()[i]));
                    System.out.println(entry.getKey()+ "====" + entry.getValue()[i]);
                }
            }
            dataList.add(dataMap);
        }
        Result data = DbraLoginUtiles.dataPost(request,dataList,"/scheduleComm/saves");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/scheduleComm/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "file")
    @ResponseBody
    public String file(HttpServletRequest request, String scheduleId) {
        Result data = DbraLoginUtiles.dataGet(request,null,"/scheduleComm/upStatusByScheduleId/" + scheduleId);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("归档成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    /**
     * 详情
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, String scheduleId) {
        model.addAttribute("scheduleId", scheduleId);
        return "modules/sys/derivce/schedule/scheduleCommForm";
    }

}
