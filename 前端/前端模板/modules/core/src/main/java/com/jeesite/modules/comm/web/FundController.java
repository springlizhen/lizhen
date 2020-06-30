package com.jeesite.modules.comm.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
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
@RequestMapping(value = "${adminPath}/fund")
public class FundController extends BaseController {

    protected Map map = null;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if(id!=null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("id", id);
            Result data = OALoginUtiles.dataGet(request,dataMap,"/fund/select");
            map = (Map) JSON.parse(data.getData());
            return map;
        }else{
            Map dataMap = MapUtils.newHashMap();
            Result data = OALoginUtiles.dataGet(request,dataMap,"/fund/select");
            map = (Map) JSON.parse(data.getData());
            return map;

        }
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/fund/commList";
    }
    @RequestMapping(value = "formFund")
    public String formFund(HttpServletRequest request, Model model,Map map) {
        String id=request.getParameter("id");
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        model.addAttribute("id",id);

        return "modules/contract/fundList";
    }
    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        return "modules/contract/commForm";
    }


    /**
     * Json数据参数例子
     * {
     *  count:20,
     *  pageNo:1,
     *  pageSize:20,
     *  list:[]
     * }
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(String id,HttpServletRequest request, HttpServletResponse response, Model model,Map map) {
        Map a=map;
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        if(id == null || id.equals("")){
            return null;
        }
        Result data = OALoginUtiles.dataGet(request,dataMap,"/fund/select");
        return data.getData();
    }
   /* @RequestMapping(value = "form")
    public String formData(HttpServletRequest request, Model model,Map map,int id) {
        JSONObject o = (JSONObject) map.get("map");
        JSONArray array= (JSONArray) o.get("list");
        JSONObject object = (JSONObject) array.get(id-1);

        Map data = object;
        model.addAttribute("comm",data);
        return "modules/contract/commForm";
    }
    */


    /**
     * 保存消息
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataPost(request,dataMap,"/contract/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存消息成功！"));
        }else
            return renderResult(Global.FALSE, text("保存消息失败！"));
    }

    /**
     * 删除消息
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Map map, HttpServletRequest request) {
        Map<String,Object> dataMap = MapUtils.newHashMap();
        JSONObject o = (JSONObject) map.get("map");
        String id= (String) o.get("id");
        dataMap.put("id", id);

        Result data = OALoginUtiles.dataGet(request,dataMap,"/fund/delete");
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除消息成功！"));
        else
            return renderResult(Global.FALSE, text("删除消息失败！"));
    }
}
