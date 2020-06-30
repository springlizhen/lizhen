package com.jeesite.modules.comm.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Controller
@RequestMapping(value = "${adminPath}/item")
public class ItemController extends BaseController {

    protected Map map = null;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) {
        if (id != null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("id", id);
            Result data = OALoginUtiles.dataGet(request, dataMap, "/item/selectItem");
            map = (Map) JSON.parse(data.getData());
            return map;
        } else {
            Map dataMap = MapUtils.newHashMap();
            Result data = OALoginUtiles.dataPost(request, dataMap, "/item/select");
            map = (Map) JSON.parse(data.getData());
            return map;

        }
    }

    private List approveType(boolean flag) {
        List applyTypeList = ListUtils.newArrayList();
        Map map = Maps.newHashMap();
        if (flag) {
            map.put("id", 0);
            map.put("name", "待审核");
            applyTypeList.add(map);
        }

        map = Maps.newHashMap();
        map.put("id", 1);
        map.put("name", "通过");
        applyTypeList.add(map);

        map = Maps.newHashMap();
        map.put("id", 2);
        map.put("name", "驳回");
        applyTypeList.add(map);

        return applyTypeList;
    }
    private List contralApprove(boolean flag) {
        List applyTypeList = ListUtils.newArrayList();
        Map map = Maps.newHashMap();
        if (flag) {
            map.put("id", 3);
            map.put("name", "待审核");
            applyTypeList.add(map);
        }

        map = Maps.newHashMap();
        map.put("id", 4);
        map.put("name", "通过");
        applyTypeList.add(map);

        map = Maps.newHashMap();
        map.put("id", 5);
        map.put("name", "驳回");
        applyTypeList.add(map);

        return applyTypeList;
    }
    private List subApprove(boolean flag) {
        List applyTypeList = ListUtils.newArrayList();
        Map map = Maps.newHashMap();
        if (flag) {
            map.put("id", 0);
            map.put("name", "待审核");
            applyTypeList.add(map);
        }

        map = Maps.newHashMap();
        map.put("id", 1);
        map.put("name", "通过");
        applyTypeList.add(map);

        map = Maps.newHashMap();
        map.put("id", 2);
        map.put("name", "驳回");
        applyTypeList.add(map);

        return applyTypeList;
    }

    @RequestMapping(value = {"file", ""})
    public String file(HttpServletRequest request, Model model, Map map) {
        Map o = model.asMap();
        JSONObject object = (JSONObject) o.get("map");
        JSONArray array = object.getJSONArray("list");
        List<Map<String, Object>> data = JSON.parseObject(array.toJSONString(), List.class);
        model.addAttribute("comm", data);
        return "modules/item/commFile";
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/item/commList";
    }

    @RequestMapping(value = {"centrallist", ""})
    public String centralList(Model model) {
        model.addAttribute("approveType", this.approveType(true));
        return "modules/item/commcentral";
    }

    @RequestMapping(value = {"sublist", ""})
    public String subList(Model model) {
        model.addAttribute("approveType", this.approveType(true));
        return "modules/item/commsub";
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm", data);
        return "modules/item/commForm";
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
    public String listData(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> dataParam = request.getParameterMap();
        Map<String, Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if (entry.getValue().length > 0 && !entry.getValue()[0].equals(""))
                if (entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(), "createDate desc");
                else
                    dataMap.put(entry.getKey(), (entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataPost(request, dataMap, "/item/select");
        return data.getData();
    }

    @Transactional(readOnly = false)
    public void saveMessage(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm", data);
        //保存发送留言对象
        // 保存上传附件
        FileUploadUtils.saveFileUpload((String) o.get("itemUpload"), "itemUpload");
    }

    @RequestMapping(value = "view")
    public String view(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm", data);
        return "modules/item/commView";
    }


    @RequestMapping(value = "approve")
    @ResponseBody
    public String Approve(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map<String, String[]> dataParam = request.getParameterMap();
        Map<String, Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            dataMap.put(entry.getKey(), (entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataGet(request, dataMap, "/item/approval");
        if (data.getCode() == 20000) {
            return renderResult(Global.TRUE, text("保存消息成功！"));
        } else
            return renderResult(Global.FALSE, text("保存消息失败！"));
    }


    @RequestMapping(value = "formApprove")
    public String formApprove(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm", data);
        model.addAttribute("approveType", this.approveType(true));
        // FileUploadUtils.saveFileUpload((String)o.get("id"), "item_file");


        return "modules/item/commApprove";
    }
    @RequestMapping(value = "centralApprove")
    public String centralApprove(HttpServletRequest request, Model model, Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm", data);
        model.addAttribute("approveType", this.approveType(true));
        return "modules/item/central";
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

    @RequestMapping(value = "update")
    @ResponseBody
    public String update(HttpServletRequest request, Model model, Map map) {
        Map<String, Object> dataMap = MapUtils.newHashMap();
        JSONObject o = (JSONObject) map.get("map");
        String id = (String) o.get("id");
        dataMap.put("id", id);
        Result data = OALoginUtiles.dataGet(request, dataMap, "/item/update");

        if (data.getCode() == 20000)
            return renderResult(Global.TRUE, text("提交成功！"));
         else
            return renderResult(Global.FALSE, text("提交失败！"));
    }




    /**
     * 保存消息
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataPost(request,dataMap,"/item/save");
        FileUploadUtils.saveFileUpload((String)dataMap.get("itemUpload"), "itemUpload");

        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("保存消息成功！"));
        else
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

        Result data = OALoginUtiles.dataGet(request,dataMap,"/item/delete");
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除消息成功！"));
        else
            return renderResult(Global.FALSE, text("删除消息失败！"));
    }
}
