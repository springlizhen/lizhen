package com.jeesite.modules.sys.utils;

import com.jeesite.modules.sys.entity.EmpUser;
import net.sf.json.JSONObject;

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
public class OAUserUtils {

    public static final String suffix = OAConfigUtil.oaHZsuffix();

    public static String reflush = OAConfigUtil.oaHZreflush();

    public static String ippostPrefix = OAConfigUtil.oaHZhttp()+OAConfigUtil.oaHZprefix();

    public static OAUserUtils oaUserUtils(){
        return new OAUserUtils();
    }
    /**
     * OA用户实时同步
     */
    public static void oaSyn(){
        HttpUtil.doPost(ippostPrefix + reflush + suffix,new HashMap<>());
    }
}
