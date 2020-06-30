package com.jeesite.modules.sys.utils;

import java.util.ResourceBundle;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
public class OAConfigUtil {

    public static String OACONFIG = "oa";

    /*
     * @description:根据属性获取文件名
     *
     * @param:propertyName文件的属性名
     *
     * @return:返回文件的属性值
     * */
    public static String getByName( String propertyName) {
        String resultM = "";//返回结果

        ResourceBundle bundle = ResourceBundle.getBundle(OACONFIG);//获取config的熟悉

        if (bundle.containsKey(propertyName)){//包含属性的key就返回相应的值
            resultM = bundle.getString(propertyName);//设置返回值
        }

        return resultM;
    }
    /**
     * oa地址
     */
    public static String oaHttp(){
        return getByName("oahttp");
    }
    /**
     * oa前缀
     */
    public static String oaPrefix(){
        return getByName("oaprefix");
    }
    /**
     * system前缀
     */
    public static String systemPrefix(){
        return getByName("systemprefix");
    }
    /**
     * oa尾椎缀
     */
    public static String oaSuffix(){
        return getByName("oasuffix");
    }
    /**
     * oa登录地址
     */
    public static String addressPath(){
        return getByName("address");
    }
    /**
     * cookie类型
     */
    public static String cookieName(){
        return getByName("cookiename");
    }
    /**
     * 系统名称类型
     */
    public static String authName(){
        return getByName("chinags.name");
    }

    /**
     * 系统名称类型
     */
    public static String systemHttp(){
        return getByName("systemhttp");
    }

    /**
     * 文件上传系统网址
     */
    public static String uploadHtp() {
        return getByName("uploadhttp");
    }
}
