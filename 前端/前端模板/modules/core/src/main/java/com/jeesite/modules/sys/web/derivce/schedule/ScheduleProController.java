package com.jeesite.modules.sys.web.derivce.schedule;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author : Mark_wang
 * @Date : 2019-9-12
 **/
@Controller
@RequestMapping(value = "${adminPath}/sys/device/schedulePro")
public class ScheduleProController extends BaseController {

    /**
     * 列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model, String scheduleId) {
        Result data = OALoginUtiles.dataGet(request, null, "/schedulePro/findByScheduleId/" + scheduleId);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
    /**
     * 详情
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, String id, String scheduleId) {
        model.addAttribute("scheduleId", scheduleId);
        if (StringUtils.isEmpty(id)) {
            model.addAttribute("schedulepro", null);
        } else {
            Result data = DbraLoginUtiles.dataGet(request,null,"/schedulePro/find/" + id);
            model.addAttribute("schedulepro", JSON.parse(data.getData()));
        }
        return "modules/sys/derivce/schedule/scheduleProForm";
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/schedulePro/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
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
        Result data = DbraLoginUtiles.dataGet(request,null,"/schedulePro/del/" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }
}
