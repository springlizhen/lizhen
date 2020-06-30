package com.jeesite.modules.sys.utils;

import com.jeesite.common.collect.MapUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
public class RequestParameter {

    public static Map<String,Object> requestParamMap(HttpServletRequest request){
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy")) {
                    dataMap.put(entry.getKey(), (entry.getValue()[0].replace(" ", "%20")));
                } else {
                    dataMap.put(entry.getKey(), paramValue(entry.getValue()));
                }
        }
        return dataMap;
    }

    public static Map<String,Object> requestParamMapPost(HttpServletRequest request){
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,Object> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals("")) {
                if (entry.getKey().equals("orderBy")) {
                    dataMap.put(entry.getKey(), (entry.getValue()[0].replace(" ", "%20")));
                } else if(entry.getKey().contains(".")){
                    paramVlue(dataMap, entry.getKey(), entry.getValue());
                } else {
                    dataMap.put(entry.getKey(), paramValue(entry.getValue()));
                }
            }
        }
        return dataMap;
    }

    private static void paramVlue(Map<String,Object> dataMap, String key, String[] value){
        int start = key.indexOf(".");
        int end = key.lastIndexOf(".");
        if (start==end){
            String[] map = key.split("\\.");
            Object o = dataMap.get(map[0]);
            if(o==null){
                Map<String,Object> odata = MapUtils.newHashMap();
                odata.put(map[1], paramValue(value));
                dataMap.put(map[0],odata);
            }else {
                Map<String,Object> odata = (Map<String, Object>) o;
                odata.put(map[1], paramValue(value));
                dataMap.put(map[0],odata);
            }
        }else{
            Map<String,Object> odata = MapUtils.newHashMap();
            String k = key.substring(0,start);
            String v = key.substring(start+1);
            paramVlue(odata, v, value);
            dataMap.put(k,odata);
        }
    }

    /**
     * 参数具体返回
     * @param value
     * @return
     */
    private static String paramValue(String[] value){
        if (value.length > 1) {
            String param = "";
            for (String params : value) {
                param += params + ",";
            }
            return param.substring(0, param.length() - 1);
        } else {
            return value[0];
        }
    }
}
