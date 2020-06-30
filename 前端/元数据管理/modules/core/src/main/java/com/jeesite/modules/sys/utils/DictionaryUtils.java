package com.jeesite.modules.sys.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryUtils {

    /**
     * 字典自定义map
     * @param init 初始值 用于map的key计算 默认0
     * @param count 计算值 用于map的key计算  默认1
     * @param names  value值，按照传入顺序排序
     * @return
     */
    public static List setMap(Long init, Long count,String... names){
        List list = ListUtils.newArrayList();
        long i = (init==null?0:init);
        long m = (count==null?1:count);
        for (String name : names){
            Map<String,Object> map = MapUtils.newHashMap();
            map.put("name",name);
            map.put("code",i+"");
            list.add(map);
            i += m;
        }
        return list;
    }

    /**
     * 字典自定义map
     * @param key key值，按照传入顺序排序
     * @param names  value值，按照传入顺序排序，必须与key顺序对应
     * @return
     */
    public static List setMapKey(String[] key,String... names){
        List list = ListUtils.newArrayList();
        int i = 0;
        for (String code : key){
            list.add(setMap(code,names[i]));
            i++;
        }
        return list;
    }

    /**
     * 字典自定义map
     * @param jsonArray alibaba JSONArray
     * @return
     */
    public static List setMap(JSONArray jsonArray){
        List list = ListUtils.newArrayList();
        for (Object code : jsonArray){
            JSONObject o = (JSONObject) code;
            list.add(setMap((String) o.get("code"), (String) o.get("name")));
        }
        return list;
    }

    /**
     * 返回map
     * @param key
     * @param value
     * @return
     */
    public static Map setMap(String key, String value){
        Map<String,Object> map = MapUtils.newHashMap();
        map.put("name",value);
        map.put("code",key);
        return map;
    }

    /**
     * 返回string
     * @param key
     * @return
     */
    public static String setDicData(String key, HttpServletRequest request){
        List list = setDicDataList(key, request);
        try {
            return JsonMapper.getInstance().writerWithView(DictData.SimpleView.class).writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 返回List
     * @param key
     * @return
     */
    public static List setDicDataList(String key, HttpServletRequest request){
        Map<String, Object> mapParam = MapUtils.newHashMap();
        mapParam.put("dicType", key);
        Result data = OALoginUtiles.authGet(request, mapParam,"/dicData/dicType");
        List<Map> list = ListUtils.newArrayList();
        if(data.getData()!=null) {
            List<JSONObject> dictData = (List) JSON.parse(data.getData());
            for (JSONObject object : dictData) {
                Map<String, Object> map = MapUtils.newHashMap();
                map.put("dictLabel", object.get("dictLabel"));
                map.put("dictValue", object.get("dictValue"));
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 返回List
     * @param key
     * @return
     */
    public static List<DictData> setDicDataListSelect(String key, HttpServletRequest request){
        Map<String, Object> mapParam = MapUtils.newHashMap();
        mapParam.put("dicType", key);
        Result data = OALoginUtiles.authGet(request, mapParam,"/dicData/dicType");
        List<DictData> list = ListUtils.newArrayList();
        if(data.getData()!=null) {
            List<JSONObject> dictData = (List) JSON.parse(data.getData());
            for (JSONObject object : dictData) {
                DictData map = new DictData();
                map.setDictValue((String) object.get("dictLabel"));
                map.setDictCode((String) object.get("dictValue"));
                list.add(map);
            }
        }
        return list;
    }
}
