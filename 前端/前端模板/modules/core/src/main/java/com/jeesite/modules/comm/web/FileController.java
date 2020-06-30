package com.jeesite.modules.comm.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Controller
@RequestMapping(value = "${adminPath}/file")
public class FileController extends BaseController {

    protected Map map = null;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if (id != null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("id", id);
            Result data = OALoginUtiles.dataGet(request, dataMap, "/file/selectFile");
            map = (Map) JSON.parse(data.getData());
            return map;
        } else {
            Map dataMap = MapUtils.newHashMap();
            Result data = OALoginUtiles.dataPost(request, dataMap, "/file/select");
            map = (Map) JSON.parse(data.getData());
            return map;

        }
    }



    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        //JSONObject o = (JSONObject) map.get("map");
        //Map data = o;
        //model.addAttribute("comm", data);
        return "modules/file/commList";
    }
    @RequestMapping(value = "view")
    public String view(HttpServletRequest request, Model model,Map map) {
        return "modules/file/commView";
    }
    @RequestMapping(value = "formFile")
    public String formFile(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm", data);
        return "modules/file/commfile";
    }
    /*
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm", data);
        return "modules/file/commForm";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataPost(request,dataMap,"/file/saveFile");
        FileUploadUtils.saveFileUpload((String)dataMap.get("itemUpload"), "itemUpload");

        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存消息成功！"));
        }else
            return renderResult(Global.FALSE, text("保存消息失败！"));
    }

    /**
     * Json数据参数例子
     * {
     * count:20,
     * pageNo:1,
     * pageSize:20,
     * list:[]
     * }
     * 查询列表数据
     */

    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, Model model,HttpServletResponse response) {
        JSONObject o = (JSONObject) map.get("map");
        Map d = o;
        model.addAttribute("comm", d);
        Map<String, String[]> dataParam = request.getParameterMap();
        Map<String, Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if (entry.getValue().length > 0 && !entry.getValue()[0].equals(""))
                if (entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(), "uploadTime desc");
                else
                    dataMap.put(entry.getKey(), (entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataPost(request, dataMap, "/file/select");
        return data.getData();
    }
}




