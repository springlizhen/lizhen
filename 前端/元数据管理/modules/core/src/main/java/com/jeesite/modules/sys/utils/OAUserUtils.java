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

    public static final String suffix = OAConfigUtil.oaSuffix();

    public static String ippostPrefix = OALoginUtiles.ippostPrefix;

    public static OAUserUtils oaUserUtils(){
        return new OAUserUtils();
    }

}
