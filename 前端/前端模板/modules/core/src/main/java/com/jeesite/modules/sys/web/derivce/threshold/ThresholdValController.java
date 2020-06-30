package com.jeesite.modules.sys.web.derivce.threshold;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工程安全阈值设置controller
 * @author XieWenqing
 * @date 2019/12/10 上午 11:35
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/device/threshold")
public class ThresholdValController extends BaseController {
    /**
     * 工程安全阈值设置首页
     */
    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/sys/derivce/threshold/thresholdValList";
    }


    /**
     * 工程安全阈值设置list
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/threshold/listData");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if (data.getCode()==20000) {
            return data.getData();
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 工程安全阈值编辑页
     * @param id  工程安全阈值记录id
     * @return
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        boolean isNewRecord = true;
        Result data = null;
        if (StringUtils.isNotEmpty(id)) {  //id不为空是修改操作
            isNewRecord = false;
            try {
                data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/threshold/findById/" + id);
            }catch (Exception e){
                e.printStackTrace();
            }
            model.addAttribute("thresholdVal", JSON.parse(data.getData()));
        } else {  //id为空是新增操作
            model.addAttribute("thresholdVal", null);
        }
        //获取数据字典
        List<DictData> measure_point = DictionaryUtils.setDicDataListSelect("measure_point", request);
        List<DictData> is_use = DictionaryUtils.setDicDataListSelect("is_use", request);
        model.addAttribute("measure_point",measure_point);
        model.addAttribute("is_use",is_use);
        model.addAttribute("isNewRecord", isNewRecord);
        return "modules/sys/derivce/threshold/thresholdValForm";
    }

    /**
     * 保存工程安全阈值
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/threshold/save");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        }
        else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
    /**
     * 停用阈值
     */
    @RequestMapping(value = "changeStatus")
    @ResponseBody
    public String changeStatus(HttpServletRequest request,  String id, String status) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/threshold/changeStatus/" + id + "/" + status);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(data.getCode()==20000){
            return renderResult(Global.TRUE, data.getMessage());
        }else{
            return renderResult(Global.FALSE, data.getMessage());
        }
    }


}
