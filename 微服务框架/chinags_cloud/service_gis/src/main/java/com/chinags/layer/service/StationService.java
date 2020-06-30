package com.chinags.layer.service;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;
import com.chinags.layer.dao.slave.StationDao;
import com.chinags.layer.dao.slave.StationDaoPlus;

import com.chinags.layer.entity.slave.Precipitation;
import com.chinags.layer.entity.slave.Station;
import com.chinags.layer.entity.slave.Stbprp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StationService extends BaseService<Station> {
    @Autowired
    private StationDaoPlus stationDaoPlus;
    @Autowired
    private StationDao stationDao;

    public List<Map<String, Object>> selectAppStation(String mn) {
        return stationDao.selectAppStation(mn);
    }

    public JSONObject selectStcd() {
        JSONObject json = new JSONObject();
        if (stationDao.selectStcd().isEmpty()) {
            String str = "{\"rainfallInfos\": [ {\"name\": \"小雨\",\"value\": 0},{\"name\": \"中雨\",\"value\": 0 }, {\"name\": \"大雨\",\"value\": 0 }, {\"name\": \"暴雨\",\"value\": 0 }, {\"name\": \"特大暴雨\",\"value\": 0 }],\"startTime\": \"2020-03-17 09:00\",\"endTime\": \"2020-03-28 09:00\"}";
            json = JSONObject.parseObject(str);
        } else {
            json.put("rainfallInfos", stationDao.selectStcd());
            json.put("startTime", stationDao.selectStcd().get(0).get("tm"));
            json.put("endTime", stationDao.selectStcd().get(0).get("tm"));
//            String str = "{\"rainfallInfos\": [ {\"name\": \"小雨\",\"value\": 0},{\"name\": \"中雨\",\"value\": 0 }, {\"name\": \"大雨\",\"value\": 0 }, {\"name\": \"暴雨\",\"value\": 0 }, {\"name\": \"特大暴雨\",\"value\": 0 }],\"startTime\": \"2020-03-17 09:00\",\"endTime\": \"2020-03-28 09:00\"}";
//            String str = JSON.toJSONString(stationDao.selectStcd());
//            String str1 = str.substring(1,str.length()-1);
//            JSONObject[] json1= new JSONObject[]{(JSONObject.parseObject(str1))};
            json = JSONObject.parseObject(stationDao.selectStcd().toString());

            json = JSONObject.parseObject(stationDao.selectStcd().toString());
        }
        return json;
    }

    public PageResult<List<Map<String, Object>>> selectPrecipitation(Precipitation prec, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo,pageSize,Sort.by(Sort.Direction.DESC,"tm"));
        return PageResult.getPageResult(stationDaoPlus.selectPrecipitation(prec,pageable));
    }

    public List<Map<String, Object>> selectAppMn(String orgId) {
        return stationDao.selectAppMn(orgId);
    }

    public List<Map<String, Object>> selectStation(String mn) {
        return stationDao.selectStation(mn);
    }

    public PageResult<List<Map<String, Object>>> selectStbprp(Stbprp stbprp, int pageNo, int pageSize) {
        PageRequest pageable;
        pageable = new PageRequest(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "moditime"));


        return PageResult.getPageResult(stationDaoPlus.selectStation(stbprp, pageable));
    }

    public List<Object> selectStcdTo() throws ParseException {
       List<Object> list = new ArrayList<>();
        List<Map<String,Object>> mapList = stationDao.selectStcd();
        Integer xiaoyu =0;
        Integer zhongyu = 0;
        Integer dayu = 0;
        Integer baoyu = 0;
        Integer tedabaoyu = 0;
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String end = ft.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String start = ft.format(calendar.getTime());
        Date endTime = ft.parse(end);
        Date startTime = ft.parse(start);
//        Date startTime = ft.parse(new Date());
        for(Map<String,Object> ti:mapList){
          String name = (String) ti.get("RAIN");
          BigDecimal num = (BigDecimal) ti.get("COUNT(DISTINCTSTCD)");
          Date now1 = (Date) ti.get("STCD");
          Date now = ft.parse(ft.format(now1));
          Boolean tu = isEffectiveDate(now,startTime,endTime);
          if(tu){
              if(name.equals("小雨")){
                  xiaoyu+=num.intValue();
              }else if(name.equals("中雨")){
                  zhongyu+=num.intValue();
              }else if(name.equals("大雨")){
                  dayu+=num.intValue();
              }else if(name.equals("暴雨")){
                  baoyu+=num.intValue();
              }else if(name.equals("特大暴雨")){
                  tedabaoyu+=num.intValue();
              }
          }

        }
        list.add(xiaoyu);
        list.add(zhongyu);
        list.add(dayu);
        list.add(baoyu);
        list.add(tedabaoyu);
        return list;

    }
    /**
     *
     * @param nowTime   返回时间
     * @param startTime	开始时间
     * @param endTime   结束时间
     * @return
     * @author sunran   判断当前时间在时间区间内
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

}
