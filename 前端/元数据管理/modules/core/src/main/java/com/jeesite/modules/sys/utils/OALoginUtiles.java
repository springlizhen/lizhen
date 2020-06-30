package com.jeesite.modules.sys.utils;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
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

    private String accessToken = null;

    /**
     * 获取token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        return  new OALoginUtiles(request).accessToken;
    }

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

    /**
     * Post
     * @param request
     * @param dataMap 发送数据map
     * @param path 访问路径
     * @return
     */
    public static Result dataPost(HttpServletRequest request, Map<String, Object> dataMap, String path) {
        dataMap.put("systemname", authName);
        String reslt = HttpUtil.doPost(systemHttp + path + suffix, JSONObject.toJSONString(dataMap), getToken(request));
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
        String reslt = HttpUtil.doPost(ippost + oaprefix + path + suffix, JSONObject.toJSONString(dataMap), getToken(request));
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
        String reslt = HttpUtil.doPost(href + prefix + path + suffix, JSONObject.toJSONString(dataMap), getToken(request));
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

}
