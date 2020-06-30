package com.chinags.common.utils;

public class RegularUtils {

    /**
     * 字符串是否是数字
     * @param str
     * @return
     */
    public static boolean stringParseInt(String str){
        if(str == null){
            return false;
        }
        return str.matches("\\d+");
    }
}
