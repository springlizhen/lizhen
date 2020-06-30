package com.jeesite.modules.dbra.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static com.jeesite.common.web.BaseController.text;
import static com.jeesite.common.web.http.ServletUtils.renderResult;

/**
 * 档案服务接口
 * @Author : Mark_wang
 * @Date : 2019/8/12
 **/
@Controller
@RequestMapping(value = "${adminPath}/filere")
public class FileResourceController {

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, Model model) {
        return "modules/dbra/FileResourceList";
    }

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
        Result data = DbraLoginUtiles.dataPost(request, dataMap, "/file/find");
        if (data.getCode() == 20000) {
            return data.getData();
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
        Result data = DbraLoginUtiles.dataGet(request,null,"/file/del/" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    /**
     * 查看编辑档案API
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, String id, String fileId) {
        Result file = DbraLoginUtiles.dataGet(request, null, "/file/publicFile/" + fileId);
        model.addAttribute("file", JSON.parse(file.getData()));
        Result data = DbraLoginUtiles.dataGet(request, null, "/file/selectById/" + id);
        model.addAttribute("fileRe", JSON.parse(data.getData()));
        return "modules/dbra/FileResourceForm";
    }

    /**
     * 查看详情
     */
    @RequestMapping(value = "info")
    public String info(HttpServletRequest request, Model model, String id) {
        Result data = DbraLoginUtiles.dataGet(request, null, "/file/selectById/" + id);
        model.addAttribute("fileRe", JSON.parse(data.getData()));
        return "modules/dbra/FileResourceInfo";
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
        Result data = DbraLoginUtiles.dataPost(request,dataMap,"/file");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"listFile", ""})
    public String listFile(HttpServletRequest request, Model model) {
        Result data1 = DbraLoginUtiles.dataGet(request, null, "/file/findAll");
        model.addAttribute("files", JSON.parseArray(data1.getData()));
        return "modules/dbra/FileList";
    }

    @RequestMapping(value = "listFileData")
    @ResponseBody
    public String listFileData(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,String> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = DbraLoginUtiles.dataPost(request, dataMap, "/file/public/file");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 根据id修改启用状态
     */
    @RequestMapping(value = "ups")
    @ResponseBody
    public String udpateStatusById(HttpServletRequest request, String id, String status) {
        Result data = DbraLoginUtiles.dataGet(request, null, "/file/updateStatus/" + id + "/" + status);
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

}