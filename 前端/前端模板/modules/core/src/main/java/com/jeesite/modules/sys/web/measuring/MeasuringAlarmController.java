package com.jeesite.modules.sys.web.measuring;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.config.Global;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.SystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

import static com.jeesite.common.web.BaseController.text;
import static com.jeesite.common.web.http.ServletUtils.renderResult;

/**
 * @Author : Mark_Wang
 * @Date : 2020/3/6
 **/
@Controller
@RequestMapping(value = "${adminPath}/sys/alarmmeasure")
public class MeasuringAlarmController {
    /**
     * 测点报警信息列表
     */
    @RequestMapping(value = "list")
    public String list(Model model, HttpServletRequest request) {
        return "modules/sys/measuring/measuringAlarmList";
    }

    /**
     * 测点报警信息列表数据获取
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(Model model, HttpServletRequest request) {
        Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/alarm/find");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
    /**
     * 获取所有的测点报警信息
     */
    @RequestMapping(value = "findAll")
    @ResponseBody
    public String findAll(Model model, HttpServletRequest request) {
        Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/alarm/findAll");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
    /**
     * 获取测点的排序报警信息
     */
    @RequestMapping(value = "pid")
    @ResponseBody
    public String pid(Model model, HttpServletRequest request) {
        Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/alarm/pid");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
    /**
     * 获取测点的排序报警信息
     */
    @RequestMapping(value = "pidTo")
    @ResponseBody
    public String pidTo( HttpServletRequest request,String workId,String sendUserName) {
        Result data = OALoginUtiles.dataGet(request, null, "/alarm/pidTo/" + workId+"/"+sendUserName);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
    /**
     * 获取测点的排序报警信息
     */
    @RequestMapping(value = "pidToK")
    @ResponseBody
    public String pidToK( HttpServletRequest request,String workId,String sendUserName) {
        Result data = OALoginUtiles.dataGet(request, null, "/alarm/pidToK/" + workId+"/"+sendUserName);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
    /**
     * 获取养护次数
     */
    @PostMapping(value = "listDateToK")
    @ResponseBody
    public String listDateToK(HttpServletRequest request,String date) {
        Result data = null;
        data = OALoginUtiles.dataGet(request, null, "/alarm/listDateToK/" + date);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }


    }

    /**
     * 根据id修改测点报警信息处理状态
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "status")
    @ResponseBody
    public String updateStatus(HttpServletRequest request, String id) {
        Result data = OALoginUtiles.dataGet(request, null, "/alarm/status/" + id);
        if (data.getCode() == 20000) {
            return renderResult(Global.TRUE, text("成功"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 根据id获取测点报警信息详情
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "find")
    public String findById(Model model, HttpServletRequest request, String id) {
        Result data = OALoginUtiles.dataGet(request, null, "/alarm/find/" + id);
        model.addAttribute("alarm", JSON.parse(data.getData()));
        return "modules/sys/measuring/measuringAlarmInfo";
    }


}
