package com.jeesite.modules.sys.utils;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.modules.sys.entity.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
public class DbraLoginUtiles {

    //auth网址
    public static final String ippost = OAConfigUtil.oaHttp();// "http://192.16.199.29:18080/horizon";//
    // http://wxapt.hanb.cn/horizon
    //网址前缀
    public static final String prefix = OAConfigUtil.oaPrefix();
    //网址尾缀
    public static final String suffix = OAConfigUtil.oaSuffix();
    //登录路径
    public static final String addressPath = OAConfigUtil.addressPath();
    //auth网址，网址+访问前缀
    public static final String ippostPrefix = ippost + prefix;
    //系统网址
    public static final String systemHttp = OAConfigUtil.systemHttp() + prefix;
    //cookie名称
    public static final String cookieName = OAConfigUtil.cookieName();
    //系统名称
    public static final String authName = OAConfigUtil.authName();
    //文件上传网址
    public static final String uploadHtp = OAConfigUtil.uploadHtp();
    //工程档案
    public static final String projectfile = OAConfigUtil.projectfile();

    private String accessToken = null;

    /**
     * 获取token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        return  new DbraLoginUtiles(request).accessToken;
    }

    /**
     * 初始化类
     * @param reques
     */
    DbraLoginUtiles(HttpServletRequest reques){
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
     * @param object 发送数据map
     * @param path 访问路径
     * @return
     */
    public static Result dataPost(HttpServletRequest request, Object object, String path) {
        //dataMap.put("systemname", authName);
        String json = JSON.toJSONString(object);
        String reslt = HttpUtil.doPost(systemHttp + path + suffix, json, getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 保存工程档案案卷著录
     * @param request
     * @param object 发送数据map
     * @param path 访问路径
     * @return
     */
    public static Result cdPost(HttpServletRequest request, Object object, String path) {
        //dataMap.put("systemname", authName);
        String json = JSON.toJSONString(object);
        String reslt = HttpUtil.doPost(projectfile + path, json, getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 工程档案案卷著录get方法
     * @param request
     * @param dataMap 发送参数
     * @param path 访问路径
     * @return
     */
    public static Result cdGet(HttpServletRequest request, Map<String, String> dataMap, String path) {
        if(dataMap==null)
            dataMap = MapUtils.newHashMap();
        dataMap.put("systemname", authName);
        //参数拼接
        StringBuilder param = new StringBuilder("?");
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if(entry.getValue()!=null)
                param.append(entry.getKey() + "=" + entry.getValue()+"&");
        }
        //去除多余参数符号
        switch (param.length()){
            case 1:
                param.delete(0, 0);
                break;
            default:
                param = param.delete(param.length()-1,param.length());
        }
        String reslt = HttpUtil.doGet(projectfile + path + param.toString(), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 保存安全工程档案
     * @param request
     * @param object 发送数据map
     * @param path 访问路径
     * @return
     */
    public static Result safePost(HttpServletRequest request, Object object, String path) {
        String json = JSON.toJSONString(object);
        String reslt = HttpUtil.doPost(systemHttp + path, json, getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * 安全工程档案get方法
     * @param request
     * @param dataMap 发送参数
     * @param path 访问路径
     * @return
     */
    public static Result safeGet(HttpServletRequest request, Map<String, String> dataMap, String path) {
        if(dataMap==null)
            dataMap = MapUtils.newHashMap();
        dataMap.put("systemname", authName);
        //参数拼接
        StringBuilder param = new StringBuilder("?");
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if(entry.getValue()!=null)
                param.append(entry.getKey() + "=" + entry.getValue()+"&");
        }
        //去除多余参数符号
        switch (param.length()){
            case 1:
                param.delete(0, 0);
                break;
            default:
                param = param.delete(param.length()-1,param.length());
        }
        String reslt = HttpUtil.doGet(systemHttp + path + param.toString(), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }


    /**
     * Get
     * @param request
     * @param dataMap 发送参数
     * @param path 访问路径
     * @return
     */
    public static Result dataGet(HttpServletRequest request, Map<String, String> dataMap, String path) {
        if(dataMap==null)
            dataMap = MapUtils.newHashMap();
        dataMap.put("systemname", authName);
        //参数拼接
        StringBuilder param = new StringBuilder("?");
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if(entry.getValue()!=null)
                param.append(entry.getKey() + "=" + entry.getValue()+"&");
        }
        //去除多余参数符号
        switch (param.length()){
            case 1:
                param.delete(0, 0);
                break;
            default:
                param = param.delete(param.length()-1,param.length());
        }
        String reslt = HttpUtil.doGet(systemHttp + path + suffix + param.toString(), getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    /**
     * Get
     * @param request
     * @param path 访问路径
     * @return
     */
    public static Result uploadGet(HttpServletRequest request, String path) {
        String reslt = HttpUtil.doGet(uploadHtp + path + suffix, getToken(request));
        Result result = com.alibaba.fastjson.JSONObject.parseObject(reslt, Result.class);
        return result;
    }

    public static String getType(Object o) { // 获取变量类型方法
        return o.getClass().toString(); // 使用int类型的getClass()方法
    }

}
