//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jeesite.modules.comm.web;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.utils.Oa;
import com.jeesite.common.web.BaseController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.HttpUtil;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.SystemException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"${adminPath}/oa"})
public class OaController extends BaseController {
    public OaController() {
    }
    @RequestMapping({"oa"})
    public String oa(Model model) {
        model.addAttribute("oa", new Oa());
        return "modules/contract/todoApplication";
    }
    @RequestMapping({"oaK"})
    public String oaK(Model model) {
        model.addAttribute("oa", new Oa());
        return "modules/contract/todoApplicationLb";
    }
    @RequestMapping({"oaKA"})
    public String oaKA(Model model) {
        model.addAttribute("oa", new Oa());
        return "modules/contract/todoApplicationYu";
    }
    //合同代办申请
    @RequestMapping({"listDataOA"})
    @ResponseBody
    public String listDataOA(HttpServletRequest request, HttpServletResponse response) {
//        String pageNo = request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("") ? request.getParameter("pageNo") : "1";
//        String pageSize = request.getParameter("pageSize") != null && !request.getParameter("pageSize").equals("") ? request.getParameter("pageSize") : "20";
//        String path = "/todo/page.wf";
//        Map dataMap = new HashMap();
//        dataMap.put("requestType", "TodoList");
//        dataMap.put("search", "合同登记");
//        dataMap.put("accessToken", this.tokenNew(request, response));
//        String reslt = doPost("http://10.0.2.5:9020/horizon-workflow-boot/horizon/workflow/rest" + path, JSONObject.fromObject(dataMap));
//        Page<Oa> page = new Page();
//        page.setList(this.entityJson(reslt));
//        JSONObject jsonobject = JSONObject.fromObject(reslt);
//        Object recordsTotal = jsonobject.get("recordsTotal");
//        page.setCount(Long.parseLong((String)recordsTotal));
//        page.setPageNo(Integer.parseInt(pageNo));
//        page.setPageSize(Integer.parseInt(pageSize));
//        return page;
        Result data = null;
        Map dataMap = MapUtils.newHashMap();

        try {
            data = OALoginUtiles.dataPost(request,dataMap,"/contract/selectTo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getData();

    }
    //合同我的发起
    @RequestMapping(value = "listDataCount")
    @ResponseBody
    public String listDataOAL(HttpServletRequest request, HttpServletResponse response) {
        Result data = null;
        Map dataMap = MapUtils.newHashMap();
        try {
            data = OALoginUtiles.dataPost(request,dataMap,"/contract/selectUi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getData();



    }
    /**
     * 合同已办申请
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"listDataOATo"})
    @ResponseBody
    public String listDataOATo(HttpServletRequest request, HttpServletResponse response) {
//        String pageNo = request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("") ? request.getParameter("pageNo") : "1";
//        String pageSize = request.getParameter("pageSize") != null && !request.getParameter("pageSize").equals("") ? request.getParameter("pageSize") : "20";
//        String path = "/todo/page.wf";
//        Map dataMap = new HashMap();
//        dataMap.put("requestType", "TodoList");
//        dataMap.put("search", "合同登记");
//        dataMap.put("accessToken", this.tokenNew(request, response));
//        String reslt = doPost("http://10.0.2.5:9020/horizon-workflow-boot/horizon/workflow/rest" + path, JSONObject.fromObject(dataMap));
//        Page<Oa> page = new Page();
//        page.setList(this.entityJson(reslt));
//        JSONObject jsonobject = JSONObject.fromObject(reslt);
//        Object recordsTotal = jsonobject.get("recordsTotal");
//        page.setCount(Long.parseLong((String)recordsTotal));
//        page.setPageNo(Integer.parseInt(pageNo));
//        page.setPageSize(Integer.parseInt(pageSize));
//        return page;
        Result data = null;

        Map dataMap = MapUtils.newHashMap();

        try {
            data = OALoginUtiles.dataPost(request,dataMap,"/contract/selectToKb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getData();
    }

    /**
     * 合同计量已办申请
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"listDataOAToYu"})
    @ResponseBody
    public String listDataOAToYu(HttpServletRequest request, HttpServletResponse response) {
//        String pageNo = request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("") ? request.getParameter("pageNo") : "1";
//        String pageSize = request.getParameter("pageSize") != null && !request.getParameter("pageSize").equals("") ? request.getParameter("pageSize") : "20";
//        String path = "/todo/page.wf";
//        Map dataMap = new HashMap();
//        dataMap.put("requestType", "TodoList");
//        dataMap.put("search", "合同登记");
//        dataMap.put("accessToken", this.tokenNew(request, response));
//        String reslt = doPost("http://10.0.2.5:9020/horizon-workflow-boot/horizon/workflow/rest" + path, JSONObject.fromObject(dataMap));
//        Page<Oa> page = new Page();
//        page.setList(this.entityJson(reslt));
//        JSONObject jsonobject = JSONObject.fromObject(reslt);
//        Object recordsTotal = jsonobject.get("recordsTotal");
//        page.setCount(Long.parseLong((String)recordsTotal));
//        page.setPageNo(Integer.parseInt(pageNo));
//        page.setPageSize(Integer.parseInt(pageSize));
//        return page;
        Result data = null;

        Map dataMap = MapUtils.newHashMap();

        try {
            data = OALoginUtiles.dataPost(request,dataMap,"/contract/selectToYu");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getData();
    }
    /**
     * 合同计量待办申请
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"listDataOALb"})
    @ResponseBody
    public String listDataOALb(HttpServletRequest request, HttpServletResponse response) {
//        String pageNo = request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("") ? request.getParameter("pageNo") : "1";
//        String pageSize = request.getParameter("pageSize") != null && !request.getParameter("pageSize").equals("") ? request.getParameter("pageSize") : "20";
//        String path = "/todo/page.wf";
//        Map dataMap = new HashMap();
//        dataMap.put("requestType", "TodoList");
//        dataMap.put("search", "合同登记");
//        dataMap.put("accessToken", this.tokenNew(request, response));
//        String reslt = doPost("http://10.0.2.5:9020/horizon-workflow-boot/horizon/workflow/rest" + path, JSONObject.fromObject(dataMap));
//        Page<Oa> page = new Page();
//        page.setList(this.entityJson(reslt));
//        JSONObject jsonobject = JSONObject.fromObject(reslt);
//        Object recordsTotal = jsonobject.get("recordsTotal");
//        page.setCount(Long.parseLong((String)recordsTotal));
//        page.setPageNo(Integer.parseInt(pageNo));
//        page.setPageSize(Integer.parseInt(pageSize));
//        return page;
        Result data = null;

        Map dataMap = MapUtils.newHashMap();

        try {
            data = OALoginUtiles.dataPost(request,dataMap,"/contract/selectLb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getData();
    }
    @RequestMapping({"item"})
    public String item(Model model) {
        model.addAttribute("oa", new Oa());
        return "modules/item/todoApplication";
    }

    @RequestMapping({"listDataItem"})
    @ResponseBody
    public Page<Oa> listDataItem(HttpServletRequest request, HttpServletResponse response) {
        String pageNo = request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("") ? request.getParameter("pageNo") : "1";
        String pageSize = request.getParameter("pageSize") != null && !request.getParameter("pageSize").equals("") ? request.getParameter("pageSize") : "20";
        String path = "/todo/page.wf";
        Map dataMap = new HashMap();
        dataMap.put("requestType", "TodoList");
        dataMap.put("search", "项目登记");
        dataMap.put("accessToken", this.tokenNew(request, response));
        String reslt = doPost("http://10.0.2.5:9020/horizon-workflow-boot/horizon/workflow/rest" + path, JSONObject.fromObject(dataMap));
        Page<Oa> page = new Page();
        page.setList(this.entityJson(reslt));
        JSONObject jsonobject = JSONObject.fromObject(reslt);
        Object recordsTotal = jsonobject.get("recordsTotal");
        page.setCount(Long.parseLong((String)recordsTotal));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPageSize(Integer.parseInt(pageSize));
        return page;
    }

    public String tokenNew(HttpServletRequest request, HttpServletResponse response) {
        String address = "/user/login";
        Map map = new HashMap();
        map.put("loginName", "system");
        map.put("password", "06cd9cd70969c81b6d0cfa58ea8ac49915f53a4cd90cdb4956e89d93");
        map.put("tenantCode", "");
        String reslt = doPost("http://10.0.2.5:9020/horizon-workflow-boot/horizon/workflow/rest" + address + ".wf", JSONObject.fromObject(map));
        return analysisMapStr(reslt);
    }

    @RequestMapping({"oaDelete"})
    public String oaDelete(Model model) {
        model.addAttribute("oa", new Oa());
        return "modules/contract/todoDelete";
    }
    @RequestMapping({"listDataDelete"})
    @ResponseBody
    public Page<Oa> oaDelete(HttpServletRequest request, HttpServletResponse response) {
        String pageNo = request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("") ? request.getParameter("pageNo") : "1";
        String pageSize = request.getParameter("pageSize") != null && !request.getParameter("pageSize").equals("") ? request.getParameter("pageSize") : "20";
        String path = "/todo/page.wf";
        Map dataMap = new HashMap();
        dataMap.put("requestType", "TodoList");
        dataMap.put("search", "合同作废");
        dataMap.put("accessToken", this.tokenNew(request, response));
        String reslt = doPost("http://10.0.2.5:9020/horizon-workflow-boot/horizon/workflow/rest" + path, JSONObject.fromObject(dataMap));
        Page<Oa> page = new Page();
        page.setList(this.entityJson(reslt));
        JSONObject jsonobject = JSONObject.fromObject(reslt);
        Object recordsTotal = jsonobject.get("recordsTotal");
        page.setCount(Long.parseLong((String)recordsTotal));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPageSize(Integer.parseInt(pageSize));
        return page;
    }

    @RequestMapping({"oaUpdate"})
    public String oaUpdate(Model model) {
        model.addAttribute("oa", new Oa());
        return "modules/contract/todoUpdate";
    }

    @RequestMapping({"listDataUpdate"})
    @ResponseBody
    public Page<Oa> oaUpdate(HttpServletRequest request, HttpServletResponse response) {
        String pageNo = request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("") ? request.getParameter("pageNo") : "1";
        String pageSize = request.getParameter("pageSize") != null && !request.getParameter("pageSize").equals("") ? request.getParameter("pageSize") : "20";
        String path = "/todo/page.wf";
        Map dataMap = new HashMap();
        dataMap.put("requestType", "TodoList");
        dataMap.put("search", "合同变更");
        dataMap.put("accessToken", this.tokenNew(request, response));
        String reslt = doPost("http://10.0.2.5:9020/horizon-workflow-boot/horizon/workflow/rest" + path, JSONObject.fromObject(dataMap));
        Page<Oa> page = new Page();
        page.setList(this.entityJson(reslt));
        JSONObject jsonobject = JSONObject.fromObject(reslt);
        Object recordsTotal = jsonobject.get("recordsTotal");
        page.setCount(Long.parseLong((String)recordsTotal));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPageSize(Integer.parseInt(pageSize));
        return page;
    }

    public static String doPost(String url, Map<String, Object> params) {
        System.out.println(url + "   |   " + JSONObject.fromObject(params));
        BufferedReader in = null;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));
            request.setHeader("Cookie", UUID.randomUUID().toString());
            request.setEntity(new StringEntity(JSONObject.fromObject(params).toString(), "utf-8"));
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code != 200) {
                System.out.println("状态码：" + code);
                return null;
            } else {
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");

                while((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }

                in.close();
                return sb.toString();
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public static String analysisMapStr(String reslt) {
        if (null != reslt) {
            Map loginMap = JSONObject.fromObject(reslt);
            Iterator var2 = loginMap.keySet().iterator();

            while(var2.hasNext()) {
                Object obj = var2.next();
                Object object = loginMap.get(obj);
                if ("accessToken" == obj || "accessToken".equals(obj)) {
                    return (String)object;
                }

                if (null != object) {
                    String classType = getType(object);
                    if (classType.indexOf("JSONObject") > -1) {
                        return analysisMapStr(JSONObject.fromObject(object).toString());
                    }
                }
            }
        }

        return null;
    }

    private List<Oa> entityJson(String reslt) {
        JSONObject jsonobject = JSONObject.fromObject(reslt);
        ArrayList oas = new ArrayList();

        try {
            JSONArray array = jsonobject.getJSONArray("data");

            for(int i = 0; i < array.size(); ++i) {
                JSONObject object = (JSONObject)array.get(i);
                Oa rule = (Oa)JSONObject.toBean(object, Oa.class);
                oas.add(rule);
            }
        } catch (Exception var8) {
            this.logger.error("oa数据解析失败！", var8);
        }

        return oas;
    }

    public static String getType(Object o) {
        return o.getClass().toString();
    }
}
