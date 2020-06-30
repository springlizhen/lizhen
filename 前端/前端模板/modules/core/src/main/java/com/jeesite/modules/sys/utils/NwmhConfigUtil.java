package com.jeesite.modules.sys.utils;

import java.util.ResourceBundle;

/**
 * @author suijicnhi
 * @date 2020-06-22
 */
public class NwmhConfigUtil {

    public static String OACONFIG = "nwmh";

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
}
