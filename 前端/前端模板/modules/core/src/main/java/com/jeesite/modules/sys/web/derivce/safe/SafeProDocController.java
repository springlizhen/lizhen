package com.jeesite.modules.sys.web.derivce.safe;

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
import java.util.UUID;

/**
 * 安全工程档案controller
 * @author XieWenqing
 * @date 2019/12/5 上午 10:59
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/device/safe")
public class SafeProDocController extends BaseController {
    /**
     * 安全工程档案首页
     */
    @RequestMapping(value = "list")
    public String clerkDocumentFileList(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/sys/derivce/safe/safeProDocList";
    }

    /**
     * 查询安全工程档案list
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model) {
        Result data = OALoginUtiles.safePost(request, RequestParameter.requestParamMap(request), "/safe/listData");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 安全工程档案编辑页
     * @param id  安全工程档案id
     * @return
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        boolean isNewRecord = true;
        if (StringUtils.isNotEmpty(id)) {  //id不为空是修改操作
            isNewRecord = false;
            Result data = DbraLoginUtiles.safeGet(request, null, "/safe/findById/" + id);
            model.addAttribute("safeProDoc", JSON.parse(data.getData()));
        } else {  //id为空是新增操作
            model.addAttribute("id", UUID.randomUUID().toString().replaceAll("-", ""));
            model.addAttribute("safeProDoc", null);
        }
        model.addAttribute("isNewRecord", isNewRecord);
        return "modules/sys/derivce/safe/safeProDocForm";
    }

    /**
     * 保存安全工程档案
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

        Result data = DbraLoginUtiles.safePost(request,dataMap,"/safe/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 安全工程档案删除
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.safeGet(request,null,"/safe/delete/" + id);
        if(data.getCode()==20000){
            return renderResult(Global.TRUE, text("删除成功！"));
        }else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }
}
