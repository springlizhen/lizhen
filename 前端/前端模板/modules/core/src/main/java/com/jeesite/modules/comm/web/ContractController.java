package com.jeesite.modules.comm.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.SystemException;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Controller
@RequestMapping(value = "${adminPath}/contract")
public class ContractController extends BaseController {

    protected Map map = null;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Map get(String id, HttpServletRequest request) { if(id!=null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("id", id);
            Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/queryContract");
            if(data!=null) {
                map = (Map) JSON.parse(data.getData());
            }
            return map;
        }else{
            Map dataMap = MapUtils.newHashMap();
            Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/select");
            map = (Map) JSON.parse(data.getData());
            return map;
        }
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {

        return "modules/contract/commList";
    }
    //合同作废
    @RequestMapping(value = {"listDelete", ""})
    public String listDelte(Model model) {
        return "modules/contract/commDelete";
    }
    //合同查询
    @RequestMapping(value = {"listSelect", ""})
    public String listSelect(Model model) {
        return "modules/contract/commFormlist";
    }
    //合同计量查询
    @RequestMapping(value = {"listSelectTo", ""})
    public String listSelectTo(Model model) {
        return "modules/contract/commFormlistTo";
    }
    /**
     * 查询指定的合同登记的所有合同计量
     */
    @RequestMapping(value = "listTo")
    public String clerkDocumentFileListTo(HttpServletRequest request, HttpServletResponse response, Model model,String id) {
        model.addAttribute("id", id);
        return  "modules/contract/commFormlistToK";
    }
    /**
     * 某个合同登记的所有合同计量
     * @param
     * @return
     */
    @RequestMapping(value = "listDataToKL")
    @ResponseBody
    public String preserve(HttpServletRequest request, HttpServletResponse response, Model model,String contractid) {
        Result data = null;
        try {
            data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/contract/listDataToKL");
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }

    }
    @RequestMapping(value = "formText")
    public String formText(HttpServletRequest request, Model model,Map map) {
            String id=request.getParameter("id");
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        //model.addAttribute("id",id);
        return "modules/contract/textForm";
    }
    @RequestMapping(value = "text")
    public String text(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        return "modules/contract/textList";
    }
    @RequestMapping(value = "view")
    public String view(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        return "modules/contract/commView";
    }
    @RequestMapping(value = "gis")
    public String gis() {
        return "modules/contract/gis";
    }
    @RequestMapping(value = "viewSubContract")
    public String viewSubContract(HttpServletRequest request, Model model,Map map) {
        String id=request.getParameter("id");
        /*Map dataMap = MapUtils.newHashMap();
        dataMap.put("id", id);
        Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/listData");
        model.addAttribute("comm",id);
        if(data==null) {
            return renderResult(Global.TRUE, text("没有子合同"));
       }
       */
        model.addAttribute("comm",id);

        return "modules/contract/subcontractForm";
    }
    /**
     * 查询设备数据
     */
    @RequestMapping(value = "listDataTo")
    @ResponseBody
    public String listDataTo(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.dataPost(request,RequestParameter.requestParamMap(request),"/contract/listDataTo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getData();
    }
    /**
     * 查询合同计量所有数据
     */
    @RequestMapping(value = "listDataToK")
    @ResponseBody
    public String listDataToK(HttpServletRequest request) {
        Result data = null;
        try {
            data = OALoginUtiles.dataPost(request,RequestParameter.requestParamMap(request),"/contract/listDataToK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getData();
    }

    //查看变更
    @RequestMapping(value = "viewUpdate")
    public String viewUpdate(HttpServletRequest request, Model model,Map map) {
        String id=request.getParameter("id");
       /* Map dataMap = MapUtils.newHashMap();
        dataMap.put("id", id);
        Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/listUpdate");
        //JSONObject o =  JSONObject.parseObject(data.getData());
       // Map m=o;

        */
        model.addAttribute("comm",id);
        return "modules/contract/contractFormUpdate";
    }
    //查看收支
    @RequestMapping(value = "viewFund")
    public String viewFund(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        return "modules/contract/fundupdate";
    }
    //查看收支
    @RequestMapping(value = "formFund")
    public String formFund(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        return "modules/contract/fundList";
    }
    //新增收支
    @RequestMapping(value = "fund")
    public String fund(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        return "modules/contract/fundForm";
    }
    @RequestMapping(value = "fundlist")
    public String fundselect(HttpServletRequest request, Model model,Map map) {
        JSONObject o = (JSONObject) map.get("map");
        Map data = o;
        model.addAttribute("comm",data);
        return "modules/contract/commcontractList";
    }
    /**
     * 查询当前登录人最近的五条我的发起合同
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping(value = "wodefaqifawen")
//    @ResponseBody
//    public List<Map<String, Object>> wodefaqifawen(HttpServletRequest request, HttpServletResponse response) {
////        User user = UserUtils.getUser();
////        String loginCode = user.getLoginCode();
//
//        //通过workId获取nodeName（节点名称）
////        if(mapList.size() > 0){
////            for(Map<String, Object> map : mapList){
////                String workid = map.get("WORKID").toString();
////                String nodeName = workZhService.findNodeByWorkId(workid);
////                map.put("NODENAME", nodeName);
////                //获取发起人
////                String faqirenLoginCode = map.get("CREATOR").toString();
////                User u = new User();
////                u.setLoginCode(faqirenLoginCode);
////                List<User> userList = userService.findList(u);
////                String userName = userList.get(0).getUserName();
////                map.put("SENDUSERNAME", userName);
////            }
////        }
//        return mapList;
//    }
    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model,Map map) {
        String id=request.getParameter("id");
        Map dataMap = MapUtils.newHashMap();
        dataMap.put("id", id);
        Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/select");
        model.addAttribute("comm",data.getData());


        return "modules/contract/commForm";
    }
    @RequestMapping(value = "formUpdate")
    public String formUpdate(HttpServletRequest request, Model model,Map map) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String id=request.getParameter("id");
        Map dataMap = MapUtils.newHashMap();
        dataMap.put("id", id);
        Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/selectUpdate");
        JSONObject o =  JSONObject.parseObject(data.getData());
        Map m=o;
        model.addAttribute("comm",m);
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
     * 保存收支
     */
    @PostMapping(value = "saveFund")
    @ResponseBody
    public String saveFund(HttpServletRequest request) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataPost(request,dataMap,"/fund/save");
        if(data.getCode()==20000) {
            return renderResult(Global.TRUE, text("保存消息成功！"));
        }else
            return renderResult(Global.FALSE, text("保存消息失败！"));
    }
    @RequestMapping(value = "selectContract")
    @ResponseBody
    public String selectContract(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> dataParam = request.getParameterMap();
        Map<String, Object> dataMap = MapUtils.newHashMap();
        Result data = OALoginUtiles.dataPost(request, dataMap, "/contract/selectAll");
        return data.getData();
    }
    @RequestMapping(value = "listFund")
    @ResponseBody
    public String listFund(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataGet(request,dataMap,"/fund/select");
        return data.getData();
    }
    @CrossOrigin(allowCredentials="true")
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response) {
        String jsonpCallback=request.getParameter("jsonpCallback");
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(), "createDate desc");
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/selectContract");
       if(jsonpCallback==null) {
           return data.getData();

       }else {
           return jsonpCallback + "(" + data.getData() + ")";
       }
    }
    @RequestMapping(value = "listUpdate")
    @ResponseBody
    public String listUpdate(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/listUpdate");
        if(data!=null) {
                return data.getData();
        }else{
            return null;
        }

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
        String id=request.getParameter("id");

        int a=0;
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        List list=new ArrayList();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if (entry.getKey().equals("fund")) {
                a = entry.getValue().length;
       break;
            }
        }
        for (int i = 0; i < a; i++) {
            Map<String,Object> map = MapUtils.newHashMap();
            for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
                if (entry.getValue().length ==a) {
                    map.put(entry.getKey(), (entry.getValue()[i]));
                    map.put("id", id);


                }
            }
            Result d = OALoginUtiles.dataPost(request,map,"/fund/save");
                }
        Result data = OALoginUtiles.dataPost(request,dataMap,"/contract/save");
        FileUploadUtils.saveFileUpload((String)dataMap.get("itemUpload"), "itemUpload");
        //Result d = OALoginUtiles.dataPost(request,list,"/fund/save");


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

        Result data = OALoginUtiles.dataGet(request,dataMap,"/contract/delete");
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除消息成功！"));
        else
            return renderResult(Global.FALSE, text("删除消息失败！"));
    }
}
