package com.chinags.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.StringUtils;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FilterShould {
    private final static int URL_LENGTH =2;

    private static String url(RequestContext currentContext){
        HttpServletRequest request = currentContext.getRequest();
        return request.getRequestURI();
    }

    /**
     *
     * @param currentContext 对应requsetContext
     * @return
     */
    public static boolean urlFilter(RequestContext currentContext){
        String requestURI = url(currentContext);
        //系统拦截地址
        String[] urls = Global.getZullSystem();
        boolean index = true;
        for (String url : urls) {
            if (StringUtils.isEmpty(url)){
                continue;
            }
            //如果匹配成功直接跳出，减小开销
            if(!index) {
                break;
            }
            index = !indexStatus(url, requestURI);
        }
        return index;
    }

    /**
     *
     * @param currentContext 对应requsetContext
     * @return
     */
    public static boolean noFilter(RequestContext currentContext){
        String requestURI = url(currentContext);
        String[] urls = Global.getZullNoFilter();
        boolean index = false;
        for (String url : urls) {
            if (StringUtils.isEmpty(url)){
                continue;
            }
            //如果匹配成功直接跳出，减小开销
            if(index) {
                break;
            }
            index = indexStatus(url, requestURI);
        }
        return index;
    }

    /**
     * 匹配规则
     * @param url 拦截url
     * @param requestURI 待匹配url
     * @return
     */
    private static boolean indexStatus(String url, String requestURI){
        boolean index = false;
        int indexOf = url.indexOf("/*");
        //是否为全路径
        if(indexOf!=-1) {
            int lastIndexOf = url.lastIndexOf("*");
            //匹配的拦截路径，不带*
            url = url.substring(0, indexOf);
            //是否是系统拦截url，如果是，返回false
            if (requestURI.contains(url)) {
                // 拦截路径/**
                if ((indexOf + URL_LENGTH) == lastIndexOf) {
                    index = requestURI.indexOf(url) == 0;
                } else {
                    requestURI = requestURI.replace(url, "");
                    switch (url) {
                        case "":
                            index = requestURI.split("/").length == URL_LENGTH;
                            break;
                        default:
                            String[] split = requestURI.split("/");
                            index = split.length == URL_LENGTH;
                    }
                }
            }
        }else{
            if (requestURI.equals(url)) {
                index = true;
            }
        }
        return index;
    }

    /**
     * 错误信息发送
     * @param currentContext 对应requsetContext
     * @param status 返回状态
     * @param message 返回错误信息
     */
    public static void errorMessage(RequestContext currentContext, int status, String message){
        HttpServletResponse response = currentContext.getResponse();
        response.setContentType("application/json; charset=utf8");
        response.setStatus(status);
        PrintWriter writer = null;
        try {
            //防止后面过滤器继续执行
            currentContext.setSendZuulResponse(false);
            writer = response.getWriter();
            writer.print(JSON.toJSON(new Result(false, status, message)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer!=null){
                writer.flush();
                writer.close();
            }
        }
    }
}
