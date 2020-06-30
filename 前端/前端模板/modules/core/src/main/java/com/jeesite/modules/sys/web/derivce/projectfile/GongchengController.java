package com.jeesite.modules.sys.web.derivce.projectfile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.Oa;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import com.jeesite.modules.sys.utils.HttpUtil;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 工程档案管理controller
 * @author XieWenqing
 * @date 2019/11/20 上午 10:51
 */
@Controller
@RequestMapping(value = "${adminPath}/projectfile")
public class GongchengController extends BaseController {
    /**
     * 我的订阅首页
     */
    @RequestMapping(value = "clerkDocumentFileList")
    public String clerkDocumentFileList(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/officialdocument/gongcheng/clerkDocumentFileList";
    }

    /**
     * 我的档案首页
     */
    @RequestMapping(value = "mydocument")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/officialdocument/gongcheng/gongchengList";
    }

    /**
     * 查询工程档案案卷著录list
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model) {
        Result data = OALoginUtiles.cdPost(request, RequestParameter.requestParamMap(request), "/gongcheng/myCdList");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 获取工程档案案卷著录树状list
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "listData2")
    @ResponseBody
    public List listData2(HttpServletRequest request, HttpServletResponse response, Model model) {
        Result data = OALoginUtiles.cdPost(request, RequestParameter.requestParamMap(request), "/gongcheng/myCdList");
        if (data.getCode() == 20000) {
            JSONObject jsonObj = new JSONObject(data.getData());
            List<Map> listMap= JSONArray.parseArray(jsonObj.get("list").toString(), Map.class);
            //实际返回的list
            List<Map<String, Object>> relist = Lists.newArrayList();
            for(Map map : listMap){
                Map m = Maps.newHashMap();
                m.put("name", map.get("item"));
                m.put("id", map.get("id"));
                relist.add(m);
            }
            return relist;
        }
        return null;
    }

    /**
     * 工程档案案卷著录编辑页
     * @param id  案卷著录id
     * @return
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        boolean isNewRecord = true;
        if(StringUtils.isNotEmpty(id)){  //id不为空是修改操作
            isNewRecord = false;
            Result data = DbraLoginUtiles.cdGet(request,null,"/gongcheng/findById/" + id);
            model.addAttribute("clerkDocument", JSON.parse(data.getData()));
        }else{  //id为空是新增操作
            model.addAttribute("clerkDocument", null);
        }
        model.addAttribute("isNewRecord", isNewRecord);
        return "modules/officialdocument/gongcheng/gongchengForm";
    }

    /**
     * 保存工程档案案卷著录
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

        Result data = DbraLoginUtiles.cdPost(request,dataMap,"/gongcheng/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 工程档案案卷著录删除
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.cdGet(request,null,"/gongcheng/delete/" + id);
        if(data.getCode()==20000){
            return renderResult(Global.TRUE, text("删除成功！"));
        }else if(data.getCode()==20001){
            return renderResult(Global.TRUE, text("存在关联的卷内文件，无法删除！"));
        }else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 工程档案卷内文件首页
     */
    @RequestMapping(value = "fileList")
    public String fileList(HttpServletRequest request, HttpServletResponse response, Model model, String clerkDocumentId) {
        model.addAttribute("clerkDocumentId", clerkDocumentId);
        return "modules/officialdocument/gongcheng/gongchengFileList";
    }

    /**
     * 根据查询工程档案案卷著录list
     */
    @RequestMapping(value = "fileListData")
    @ResponseBody
    public String fileListData(HttpServletRequest request, HttpServletResponse response, Model model, String type) {
        Result data = null;
        if(StringUtils.isEmpty(type)){
            data = OALoginUtiles.cdPost(request, RequestParameter.requestParamMap(request), "/gongcheng/fileListData");
        }else{
            data = OALoginUtiles.cdPost(request, RequestParameter.requestParamMap(request), "/gongcheng/fileAllListData");
        }

        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 工程档案案卷著录编辑页
     * @param id  案卷著录id
     * @return
     */
    @RequestMapping(value = "findCd")
    @ResponseBody
    public String findCd(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
        Result data = DbraLoginUtiles.cdGet(request,null,"/gongcheng/findById/" + id);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 工程档案卷内文件编辑页
     * @param clerkDocumentId 案卷著录id
     * @param id  卷内文件id
     * @return
     */
    @RequestMapping(value = "fileForm")
    public String fileForm(HttpServletRequest request, HttpServletResponse response, Model model,String clerkDocumentId, String id) {
        //案卷著录id有值时，是新增操作
        if(StringUtils.isNotEmpty(clerkDocumentId)){
            model.addAttribute("clerkDocumentId", clerkDocumentId);
            model.addAttribute("id", UUID.randomUUID().toString().replaceAll("-", ""));
            model.addAttribute("clerkDocumentFile", null);
        }else{
            Result data = DbraLoginUtiles.cdGet(request,null,"/gongcheng/findCdfById/" + id);
            JSONObject jsonObj = new JSONObject(data.getData());
            clerkDocumentId = jsonObj.get("clerkDocumentId").toString();
            model.addAttribute("clerkDocumentId", clerkDocumentId);
            model.addAttribute("clerkDocumentFile", JSON.parse(data.getData()));
        }
        return "modules/officialdocument/gongcheng/gongchengFileForm";
    }

    /**
     * 工程档案卷内文件编辑页
     * @param clerkDocumentId 案卷著录id
     * @param id  卷内文件id
     * @return
     */
    @RequestMapping(value = "fileView")
    public String fileView(HttpServletRequest request, HttpServletResponse response, Model model,String clerkDocumentId, String id) {
        Result data = DbraLoginUtiles.cdGet(request,null,"/gongcheng/findCdfById/" + id);
        JSONObject jsonObj = new JSONObject(data.getData());
        clerkDocumentId = jsonObj.get("clerkDocumentId").toString();
        model.addAttribute("clerkDocumentId", clerkDocumentId);
        model.addAttribute("clerkDocumentFile", JSON.parse(data.getData()));
        //获取关联的案卷著录名称
        Result data2 = DbraLoginUtiles.cdGet(request,null,"/gongcheng/findById/" + clerkDocumentId);
        jsonObj = new JSONObject(data2.getData());
        model.addAttribute("title", jsonObj.get("title").toString());
        return "modules/officialdocument/gongcheng/gongchengFileView";
    }

    /**
     * 保存工程档案卷内文件
     */
    @PostMapping(value = "fileSave")
    @ResponseBody
    public String fileSave(HttpServletRequest request) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String, String> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if((entry.getValue().length>0) && !entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }

        Result data = DbraLoginUtiles.cdPost(request,dataMap,"/gongcheng/fileSave");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存成功！"));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 工程档案卷内文件删除
     */
    @RequestMapping(value = "deleteFile")
    @ResponseBody
    public String deleteFile(HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.cdGet(request,null,"/gongcheng/deleteFile/" + id);
        if(data.getCode()==20000){
            return renderResult(Global.TRUE, text("删除成功！"));
        }else if(data.getCode()==20001){
            return renderResult(Global.TRUE, text("存在关联的卷内文件，无法删除！"));
        }else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping(value = "cansee")
    @ResponseBody
    public String cansee(HttpServletRequest request, HttpServletResponse response, Model model, String cdfId) {
        Result data = DbraLoginUtiles.cdGet(request,null,"/gongcheng/cansee/" + cdfId);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 订阅审批首页
     */
    @RequestMapping(value = "application")
    public String application(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("oa","sdfdsf");
//        return "modules/item/todoApplication";
        return "modules/officialdocument/gongcheng/todoApplication";
    }

    //档案借阅申请待办
    //计划代办申请
    @RequestMapping(value = "listDataOA")
    @ResponseBody
    public Page<Oa> listDataOA(HttpServletRequest request, HttpServletResponse response) {
        String pageNo = request.getParameter("pageNo")==null||request.getParameter("pageNo").equals("")?"1":request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize")==null||request.getParameter("pageSize").equals("")?"20":request.getParameter("pageSize");
        String orderBy = request.getParameter("orderBy");
        // 系统登陆
        String path = "horizon/workflow/rest/todo/page.wf?page="+pageNo+"&limit="+pageSize+"&sort="+orderBy;
        Map dataMap = new HashMap<String, Object>();
        dataMap.put("requestType", "TodoList");
        dataMap.put("search","档案借阅");
        dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
        String reslt = HttpUtil.doPost(OALoginUtiles.oaHZhttp + path, net.sf.json.JSONObject.fromObject(dataMap));
        Page<Oa> page = new Page<>();
        page.setList(OALoginUtiles.entityJson(reslt));

        net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(reslt);
        Object recordsTotal = jsonobject.get("recordsTotal");
        page.setCount(Long.parseLong((String) recordsTotal));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPageSize(Integer.parseInt(pageSize));
        return page;
    }

    private List<Oa> entityJson(String reslt){
        net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(reslt);
        List<Oa> oas = new ArrayList<Oa>();
        try {
            net.sf.json.JSONArray array = jsonobject.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                net.sf.json.JSONObject object = (net.sf.json.JSONObject) array.get(i);     //将array中的数据进行逐条转换
                Oa rule = (Oa) net.sf.json.JSONObject.toBean(object, Oa.class);  //通过JSONObject.toBean()方法进行对象间的转换
                oas.add(rule);
            }
        }catch (Exception e){
            logger.error("oa数据解析失败！",e);
        }
        return oas;
    }

}
