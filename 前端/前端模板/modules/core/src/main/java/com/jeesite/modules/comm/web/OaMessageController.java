package com.jeesite.modules.comm.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.utils.Oa;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.HttpUtil;
import com.jeesite.modules.sys.utils.OAConfigUtil;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
@RequestMapping(value = "${adminPath}/oa/message")
public class OaMessageController {

    private static Logger logger = LoggerFactory.getLogger(OaMessageController.class);

    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<Oa> listData(HttpServletRequest request, HttpServletResponse response) {
//        LoginInfo loginInfo = UserUtils.getLoginInfo();
//        User user = UserUtils.get(loginInfo.getId());
        // 系统登陆
        String path = "/todo/page.wf?page=1&limit=5&sort=";
        Map dataMap = new HashMap<String, Object>();
        dataMap.put("requestType", "TodoList");
        dataMap.put("search", "");
        dataMap.put("title", "");
        dataMap.put("flowName", "%发文%");
        dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
        String reslt = HttpUtil.doPost(OAConfigUtil.oaHZhttp() + OAConfigUtil.oaHZprefix() + path, JSONObject.fromObject(dataMap));
        return entityJson(reslt);
    }

    enum UrlList {
        待阅("/todo/page.wf?page=1&limit=1", "MatterRead"), 待办("/todo/page.wf?page=1&limit=1", "TodoList"), 已办("/haddone/page.wf?page=1&limit=1", "HadDone"), 已阅("/haddone/page.wf?page=1&limit=1", "HadRead"), 我的请求("/hadstarted/page.wf?page=1&limit=1", "HadDone");
        private String url;
        private String requestType;

        UrlList(String url, String requestType) {
            this.url = OAConfigUtil.oaHZhttp() + OAConfigUtil.oaHZprefix() + url;
            this.requestType = requestType;
        }
    }

    @RequestMapping(value = "pageAmount")
    @ResponseBody
    public Map getHomePageAmount(HttpServletRequest request, HttpServletResponse response) {
//        User user = UserUtils.getUser();
//        String senderid = user.getId();
        Map val = new HashMap<String, String>();
        try {
            String token = new OALoginUtiles().tokenNew(request, response);

            Map dataMap = new HashMap<String, Object>();
            dataMap.put("search", "");
            dataMap.put("type", "发文");
            dataMap.put("accessToken", token);
            //待办
            String url = UrlList.待办.url;
            dataMap.put("requestType", UrlList.待办.requestType);
            String reslt = HttpUtil.doPost(url, JSONObject.fromObject(dataMap));
            JSONObject jsonobject = JSONObject.fromObject(reslt);
            long recordsTotal = Long.parseLong(jsonobject.getString("recordsTotal"));
            val.put("daiban", recordsTotal);

            /*dataMap.put("type", "收文");
            reslt = HttpUtil.doPost(url, JSONObject.fromObject(dataMap));
            jsonobject = JSONObject.fromObject(reslt);
            recordsTotal = Long.parseLong(jsonobject.getString("recordsTotal"));
            val.put("daibansw", recordsTotal);
            val.put("daiban", (long) val.get("daibanfw") + recordsTotal);*/

            //已办
            url = UrlList.已办.url;
            dataMap.put("requestType", UrlList.已办.requestType);
            dataMap.remove("type");
            reslt = HttpUtil.doPost(url, JSONObject.fromObject(dataMap));
            jsonobject = JSONObject.fromObject(reslt);
            recordsTotal = Long.parseLong(jsonobject.getString("recordsTotal"));
            val.put("yiban", recordsTotal);

            //我的请求
            url = UrlList.我的请求.url;
            dataMap.put("requestType", UrlList.我的请求.requestType);
            reslt = HttpUtil.doPost(url, JSONObject.fromObject(dataMap));
            jsonobject = JSONObject.fromObject(reslt);
            recordsTotal = Long.parseLong(jsonobject.getString("recordsTotal"));
            val.put("qingqiu", recordsTotal);

            //待阅
            url = UrlList.待阅.url;
            dataMap.put("requestType", UrlList.待阅.requestType);
            reslt = HttpUtil.doPost(url, JSONObject.fromObject(dataMap));
            jsonobject = JSONObject.fromObject(reslt);
            recordsTotal = Long.parseLong(jsonobject.getString("recordsTotal"));
            val.put("daiyue", recordsTotal);

            //已阅
            url = UrlList.已阅.url;
            dataMap.put("requestType", UrlList.已阅.requestType);
            reslt = HttpUtil.doPost(url, JSONObject.fromObject(dataMap));
            jsonobject = JSONObject.fromObject(reslt);
            recordsTotal = Long.parseLong(jsonobject.getString("recordsTotal"));
            val.put("yiyue", recordsTotal);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("失败");
            return null;
        }
        return val;
    }

    @RequestMapping(value = "listDataDY")
    @ResponseBody
    public Page<Oa> listDataDY(HttpServletRequest request, HttpServletResponse response) {
//        LoginInfo loginInfo = UserUtils.getLoginInfo();
//        User user = UserUtils.get(loginInfo.getId());
        // 系统登陆
        String path = "/todo/page.wf?page=1&limit=5&sort=";
        Map dataMap = new HashMap<String, Object>();
        dataMap.put("requestType", "MatterRead");
        dataMap.put("search", "");
        dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
        String reslt = HttpUtil.doPost(OAConfigUtil.oaHZhttp() + OAConfigUtil.oaHZprefix() + path, JSONObject.fromObject(dataMap));
        return entityJson(reslt);
    }

    /**
     * 已办流程
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "listDataEnd")
    @ResponseBody
    public Page<Oa> listDataEnd(HttpServletRequest request, HttpServletResponse response) {
//        LoginInfo loginInfo = UserUtils.getLoginInfo();
//        User user = UserUtils.get(loginInfo.getId());
        // 系统登陆
        String path = "/haddone/page.wf?page=1&limit=5&sort=";
        Map dataMap = new HashMap<String, Object>();
        dataMap.put("requestType", "HadDone");
        dataMap.put("search", "");
        dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
        String reslt = HttpUtil.doPost(OAConfigUtil.oaHZhttp() + OAConfigUtil.oaHZprefix() + path, JSONObject.fromObject(dataMap));
        return entityJson(reslt);
    }

    /**
     * 已阅流程
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "listDataRead")
    @ResponseBody
    public Page<Oa> listDataRead(HttpServletRequest request, HttpServletResponse response) {
//        LoginInfo loginInfo = UserUtils.getLoginInfo();
//        User user = UserUtils.get(loginInfo.getId());
        // 系统登陆
        String path = "/haddone/page.wf?page=1&limit=5&sort=";
        Map dataMap = new HashMap<String, Object>();
        dataMap.put("requestType", "HadRead");
        dataMap.put("search", "");
        dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
        String reslt = HttpUtil.doPost(OAConfigUtil.oaHZhttp() + OAConfigUtil.oaHZprefix() + path, JSONObject.fromObject(dataMap));
        return entityJson(reslt);
    }

    /**
     * 我的请求
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "myListData")
    @ResponseBody
    public Page<Oa> myListData(HttpServletRequest request, HttpServletResponse response) {
//        LoginInfo loginInfo = UserUtils.getLoginInfo();
//        User user = UserUtils.get(loginInfo.getId());
        // 系统登陆
        String path = "/hadstarted/page.wf?page=1&limit=5&sort=";
        Map dataMap = new HashMap<String, Object>();
        dataMap.put("requestType", "HadDone");
        dataMap.put("search", "");
        dataMap.put("accessToken", new OALoginUtiles().tokenNew(request, response));
        String reslt = HttpUtil.doPost(OAConfigUtil.oaHZhttp() + OAConfigUtil.oaHZprefix() + path, JSONObject.fromObject(dataMap));
        return entityJson(reslt);
    }

    private Page<Oa> entityJson(String reslt) {
        Page<Oa> page = new Page<>();
        JSONObject jsonobject = JSONObject.fromObject(reslt);
//        logger.info(jsonobject.toString());
        List<Oa> oas = new ArrayList<Oa>();
        try {
            JSONArray array = jsonobject.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = (JSONObject) array.get(i);     //将array中的数据进行逐条转换
                Oa rule = (Oa) JSONObject.toBean(object, Oa.class);  //通过JSONObject.toBean()方法进行对象间的转换
                oas.add(rule);
            }
        } catch (Exception e) {
            logger.error("oa数据解析失败！", e);
            return null;
        }
        page.setList(oas);
        page.setCount(Long.parseLong(jsonobject.getString("recordsTotal")));
        return page;
    }
}
