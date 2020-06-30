package com.jeesite.modules.sys.web.derivce.maintain;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
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
import javax.servlet.http.HttpServletResponse;

/**
 * 养护记录controller
 * @author XieWenqing
 * @date 2019/12/6 下午 5:46
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/device/maintain")
public class MaintainInfoController extends BaseController {
    /**
     * 养护记录首页
     */
    @RequestMapping(value = "list")
    public String clerkDocumentFileList(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/sys/derivce/maintain/maintainInfoList";
    }
    /**
     * 查询养护记录list
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/maintain/listData");
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
     * 查询养护记录数量
     */
    @RequestMapping(value = "count")
    @ResponseBody
    public String count(HttpServletRequest request, HttpServletResponse response, Model model) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/maintain/listDataTo");
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
     * 养护记录编辑页
     * @param id  养护记录id
     * @return
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        boolean isNewRecord = true;
        Result data = null;
        if (StringUtils.isNotEmpty(id)) {  //id不为空是修改操作
            isNewRecord = false;
            try {
                data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/maintain/findById/" + id);
            }catch (Exception e){
                e.printStackTrace();
            }
            model.addAttribute("maintainInfo", JSON.parse(data.getData()));
        } else {  //id为空是新增操作
            model.addAttribute("maintainInfo", null);
        }
        model.addAttribute("isNewRecord", isNewRecord);
        return "modules/sys/derivce/maintain/maintainInfoForm";
    }
    /**
     * 养护记录编辑页
     * @param id  养护记录id
     * @return
     */
    @RequestMapping(value = "preserveForm")
    public String preserveForm(HttpServletRequest request, HttpServletResponse response, Model model,String id) {
        boolean isNewRecord = true;
        Result data = null;
        //id为空是新增操作
            try {
            data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/maintain/findById/"+id);
            }catch (Exception e){
                e.printStackTrace();
            }
            model.addAttribute("preserve",  JSON.parse(data.getData()));


//        model.addAttribute("isNewRecord", isNewRecord);
        return "modules/sys/derivce/preserve/preserveInfoForm";
    }
    /**
     * 维护养护记录
     * @param
     * @return
     */
    @RequestMapping(value = "preserve")
    @ResponseBody
    public String preserve(HttpServletRequest request, HttpServletResponse response, Model model) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request, "systemhttp", RequestParameter.requestParamMap(request), "/preserve/findByScheduleId");
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }

    }

    /**
     * 维护记录首页
     */
    @RequestMapping(value = "listTo")
    public String clerkDocumentFileListTo(HttpServletRequest request, HttpServletResponse response, Model model,String id) {
             model.addAttribute("id", id);
            return "modules/sys/derivce/preserve/preserveList";
    }

    /**
     * 保存养护记录
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request),"/maintain/save");
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
     * 保存维护记录
     */
    @PostMapping(value = "saveTo")
    @ResponseBody
    public String saveTo(HttpServletRequest request) {
        Result data = null;
        try {
         data = OALoginUtiles.sysPost(request,"systemhttp", RequestParameter.requestParamMap(request), "/maintain/saveTo");
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if (data.getCode() == 20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 删除养护记录
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(HttpServletRequest request, String id) {
        Result data = null;
        try {
            data = OALoginUtiles.sysGet(request,"systemhttp", RequestParameter.requestParamMap(request),"/maintain/delete/" + id);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(data.getCode()==20000){
            return renderResult(Global.TRUE, text("删除成功！"));
        }else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }


    
}
