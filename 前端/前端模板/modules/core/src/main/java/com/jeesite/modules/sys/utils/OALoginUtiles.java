package com.jeesite.modules.sys.utils;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.utils.Oa;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.modules.sys.entity.Result;
import io.jsonwebtoken.Claims;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class OALoginUtiles {
    //auth网址
    public static final String ippost = OAConfigUtil.oaHttp();// "http://192.16.199.29:18080/horizon";//
    // http://wxapt.hanb.cn/horizon
    //oa网址前缀
    public static final String prefix = OAConfigUtil.systemPrefix();
    //网址前缀
    public static final String oaprefix = OAConfigUtil.oaPrefix();
    //网址尾缀
    public static final String suffix = OAConfigUtil.oaSuffix();
    //登录路径
    public static final String addressPath = OAConfigUtil.addressPath();
    //auth网址，网址+访问前缀
    public static final String ippostPrefix = ippost + oaprefix;
    //系统网址
    public static final String systemHttp = OAConfigUtil.systemHttp() + prefix;
    //cookie名称
    public static final String cookieName = OAConfigUtil.cookieName();
    //系统名称
    public static final String authName = OAConfigUtil.authName();
    //工程档案
    public static final String projectfile = OAConfigUtil.projectfile();
    //工程
    public static final String projectfile1 = OAConfigUtil.projectfileTo();
    //慧正
    public static final String oaHZhttp = OAConfigUtil.oaHZhttp();
    private String accessToken = null;

    /**
     * 获取token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        return  new OALoginUtiles(request).accessToken;
    }

    public OALoginUtiles(){}

    /**
     * 初始化类
     * @param reques
     */
    OALoginUtiles(HttpServletRequest reques){
        token(reques);
    }



    /**
     * 初始化token
     * @param request
     * @return
     */
    private String token(HttpServletRequest request){
        String token = CookieUtils.getCookie(request,cookieName);
        accessToken = token;
        return accessToken;
    }

    public String tokenNew(HttpServletRequest request, HttpServletResponse response){
        String cookie = CookieUtils.getCookie(request, OALoginUtiles.cookieName);
        Claims claims = JwtUtil.parseJWT(cookie);
        // 系统登陆
        String address = "horizon/workflow/rest/user/login.wf";
        Map map = new HashMap<String, Object>();
        map.put("loginName", claims.get("loginCode"));
        map.put("password", claims.get("pm"));
        map.put("tenantCode", "");
        String reslt = HttpUtil.doPost(oaHZhttp + address, JSONObject.fromObject(map));
        return analysisMapStr(reslt);
    }

    /**
     * Post
     * @param request
     * @param dataMap 发送数据map
     * @param path 访问路径
     * @return
     */
    public static Result dataPost(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        dataMap.put("systemname", authName);
        String reslt = HttpUtil.doPost(systemHttp + path + suffix, JSONObject.fromObject(dataMap), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 查询工程档案list
     * @param request
     * @param dataMap 发送数据map
     * @param path 访问路径
     * @return
     */
    public static Result cdPost(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        dataMap.put("systemname", authName);
        String reslt = HttpUtil.doPost(projectfile + path , JSONObject.fromObject(dataMap), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }
    public static Result cdPostTo(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        dataMap.put("systemname", authName);
        String reslt = HttpUtil.doPost(projectfile1 + path , JSONObject.fromObject(dataMap), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 查询安全工程档案list
     * @param request
     * @param dataMap 发送数据map
     * @param path 访问路径
     * @return
     */
    public static Result safePost(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        dataMap.put("systemname", authName);
        String reslt = HttpUtil.doPost(systemHttp + path , JSONObject.fromObject(dataMap), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 认证系统Post
     * @param request
     * @param dataMap 发送参数
     * @param path 访问路径
     * @Param auth 是否为认证系统连接
     * @return
     */
    public static Result authPost(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        dataMap.put("systemname", authName);
        String reslt = HttpUtil.doPost(ippost + oaprefix + path + suffix, JSONObject.fromObject(dataMap).toString(), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * Post
     * @param request
     * @param dataMap 发送参数
     * @param path 访问路径
     * @Param auth 是否为认证系统连接
     * @return
     */
    public static Result post(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        dataMap.put("systemname", authName);
        String reslt = HttpUtil.doPost(ippost + prefix + path + suffix, dataMap, getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 系统Post
     * @param request
     * @param sysName 系统配置 key值
     * @param dataMap 发送参数
     * @param path 访问路径
     * @Param auth 是否为认证系统连接
     * @return
     */
    public static Result sysPost(HttpServletRequest request, String sysName, Map<String, Object> dataMap, String path) throws SystemException {
        String href = OAConfigUtil.getByName(sysName);
        if(href==null&&href.length()==0)
            throw new SystemException("系统配置为空，请检查oa.properties文件");
        dataMap.put("systemname", authName);
        String reslt = HttpUtil.doPost(href + prefix + path + suffix, JSONObject.fromObject(dataMap), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * Get
     * @param request
     * @param dataMap 发送参数
     * @param path 访问路径
     * @Param auth 是否为认证系统连接
     * @return
     */
    public static Result dataGet(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        String reslt = HttpUtil.doGet(systemHttp + path + suffix + paramValue(dataMap), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 认证系统Get
     * @param request
     * @param dataMap 发送参数
     * @param path 访问路径
     * @Param auth 是否为认证系统连接
     * @return
     */
    public static Result authGet(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        String reslt = HttpUtil.doGet(ippost + oaprefix + path + suffix + paramValue(dataMap), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 其他系统Get
     * @param request
     * @param sysName 系统配置 key值
     * @param dataMap 发送参数
     * @param path 访问路径
     * @Param auth 是否为认证系统连接
     * @return
     */
    public static Result sysGet(HttpServletRequest request, String sysName, Map<String, Object> dataMap, String path) throws SystemException {
        String href = OAConfigUtil.getByName(sysName);
        if(href==null&&href.length()==0)
            throw new SystemException("系统配置为空，请检查oa.properties文件");
        String reslt = HttpUtil.doGet(href + prefix + path + suffix + paramValue(dataMap), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * get参数调整
     * @param dataMap
     * @return
     */
    private static String paramValue(Map<String, Object> dataMap){
        if(dataMap==null)
            dataMap = MapUtils.newHashMap();
        dataMap.put("systemname", authName);
        //参数拼接
        StringBuilder param = new StringBuilder("?");
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            if(entry.getValue()!=null) {
                try {
                    param.append(entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(),"UTF-8")+"&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        //去除多余参数符号
        switch (param.length()){
            case 1:
                param.delete(0, 0);
                break;
            default:
                param = param.delete(param.length()-1,param.length());
        }
        return param.toString();
    }

    public static String getType(Object o) { // 获取变量类型方法
        return o.getClass().toString(); // 使用int类型的getClass()方法
    }
    /**
     * String 转 Map 解析Map
     *
     * @param reslt
     */
    public static String analysisMapStr(String reslt) {

        if (null != reslt) {
//            System.out.println("reslt: " + reslt);
            Map loginMap = (Map) JSONObject.fromObject(reslt);// (reslt,Map.class
            // ,Map.class);//
            // JsonUtil.fromJson(reslt);
            for (Object obj : loginMap.keySet()) {
                Object object = loginMap.get(obj);
//                System.out.println("key为： " + obj + "    --值为--：" + object);
                if ("accessToken" == obj || "accessToken".equals(obj)) {
                    return (String) object;
                }
                if (null != object) {
                    String classType = getType(object);
//                    System.out.println("key为： " + obj + "    --类型--：" + classType);
                    if (classType.indexOf("JSONObject") > -1) { // 如果Json格式重新递归调用
                        return analysisMapStr(JSONObject.fromObject(object).toString());
                    }
                }
            }
        }
        return null;
    }
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(OALoginUtiles.class);

    public static List<Oa> entityJson(String reslt){
        net.sf.json.JSONObject jsonobject = net.sf.json.JSONObject.fromObject(reslt);
        List<Oa> oas = new ArrayList<Oa>();
        try {
            JSONArray array = jsonobject.getJSONArray("data");
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
