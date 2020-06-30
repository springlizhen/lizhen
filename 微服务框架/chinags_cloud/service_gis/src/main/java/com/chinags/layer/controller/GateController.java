package com.chinags.layer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.layer.entity.slave.Precipitation;
import com.chinags.layer.entity.slave.Stbprp;
import com.chinags.layer.service.DeviceParamService;
import com.chinags.layer.service.DeviceService;
import com.chinags.layer.service.OfficeService;
import com.chinags.layer.service.StationService;
import com.chinags.layer.utils.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Api("图层申请管理controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/gate")
public class GateController extends BaseController {

    @Autowired
    private OfficeService officeService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceParamService deviceParamService;
    @Autowired
    private StationService stationService;
    @ApiOperation("根据id获取设施设备属性")
    @RequestMapping(value = "selectAppDevice", method = RequestMethod.GET)
    public Result selectAppDevice(String id) {
        List list = deviceParamService.selectDeviceByCodeId(id);
        return new Result(true, StatusCode.OK, "获取成功", list);

    }

    @ApiOperation("获取监测信息")
    @RequestMapping(value = "selectStcd", method = RequestMethod.GET)
    public Result selectStcd() {
        JSONObject list = stationService.selectStcd();
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取监测信息")
    @RequestMapping(value = "selectStcdTo", method = RequestMethod.GET)
    public List<Object> selectStcdTo() throws ParseException {
        List<Object> list = stationService.selectStcdTo();
        return list;
    }
    @ApiOperation("获取设施设备水质监测信息详情")
    @RequestMapping(value = "selectAppMn", method = RequestMethod.GET)
    public Result selectAppMn(String orgId) {
        List list = stationService.selectAppMn(orgId);
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取测站")
    @RequestMapping(value = "appSelectStation", method = RequestMethod.GET)
    public Result selectAppStation(String mn) {
        List<Map<String, Object>> list = stationService.selectAppStation(mn);
        return new Result(true, StatusCode.OK, "获取成功", list);

    }
    @ApiOperation("分页获取测站信息")
    @RequestMapping(value = "selectStbprp", method = RequestMethod.GET)
    public Result selectStation(Stbprp stbprp, int pageSize, int pageNo) {
        PageResult pageResult = stationService.selectStbprp(stbprp, pageNo, pageSize);
        return new Result(true, StatusCode.OK, "获取成功", pageResult);

    }
    @ApiOperation("分页获取测站降水量")
    @RequestMapping(value = "selectPrecipitation", method = RequestMethod.GET)
    public Result selectPrecipitation(Precipitation prec, int pageSize, int pageNo) {
        PageResult pageResult = stationService.selectPrecipitation(prec,pageNo, pageSize);
        return new Result(true, StatusCode.OK, "获取成功", pageResult);
    }

    @ApiOperation("获取机构")
    @RequestMapping(value = "selectOffice", method = RequestMethod.GET)
    public Result selectOffice() {
        List list = officeService.selectOffice();
        return new Result(true, StatusCode.OK, "获取成功", list);
    }

    @ApiOperation("根据mn号获取水质监测信息")
    @RequestMapping(value = "selectStation", method = RequestMethod.GET)
    public Result selectStation(String mn) {
        List list = stationService.selectStation(mn);
        return new Result(true, StatusCode.OK, "获取成功", list);

    }

    @ApiOperation("获取设施基础信息")
    @RequestMapping(value = "getAACParam", method = RequestMethod.GET)
    public Result selectAACParam(String id) {
        List list = deviceParamService.selectAACParam(id);
        return new Result(true, StatusCode.OK, "获取成功", list);

    }

    @ApiOperation("获取设施基础信息")
    @RequestMapping(value = "selectAADParam", method = RequestMethod.GET)
    public Result selectAADParam(String id) {
        List list = deviceParamService.selectAADParam(id);
        return new Result(true, StatusCode.OK, "获取成功", list);

    }

    @ApiOperation("获取设施基础信息")
    @RequestMapping(value = "selectDeviceParam", method = RequestMethod.GET)
    public Result selectDeviceParam(String id) {
        List list = deviceParamService.selectDeviceParam(id);
        return new Result(true, StatusCode.OK, "获取成功", list);

    }

    @ApiOperation("获取值班信息")
    @RequestMapping(value = "getScheduling", method = RequestMethod.GET)
    public JSONObject getScheduling(String deptId) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        Map map = new HashMap();
        map.put("username", "sjcsyh");
        map.put("password", "111111");
        String result = HttpUtil.doPost("http://10.0.2.11:8077/api-auth/login/userLogin", map);
        String token = JSON.parseObject(result).getString("token");
        String reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/duty/getSchedulingListToAjaxResult?starttm=" + date + "&deptId=" + deptId, token);
        JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        return jsonObject;
    }

    @ApiOperation("根据类型获取基础设施详情")
    @RequestMapping(value = "selectGate", method = RequestMethod.GET)
    public Result selectGate(String type,String orgId) {
        List list = deviceService.selectGate(type,orgId);
        return new Result(true, StatusCode.OK, "获取成功", list);

    }

    @ApiOperation("获取token")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public JSONObject getGateStationList(String slcd) {
        Map map = new HashMap();
        map.put("username", "sjcsyh");
        map.put("password", "111111");
        String result = HttpUtil.doPost("http://10.0.2.11:8077/api-auth/login/userLogin", map);
        String token = JSON.parseObject(result).getString("token");
        String reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?tm_type=0&slcd=" + slcd, token);
        JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        return jsonObject;
    }

    @ApiOperation("获取token")
    @RequestMapping(value = "listData", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getGateStationListData() {
        Map map = new HashMap();
        map.put("username", "sjcsyh");
        map.put("password", "111111");
        String result = HttpUtil.doPost("http://10.0.2.11:8077/api-auth/login/userLogin", map);
        String token = JSON.parseObject(result).getString("token");
        String u = "0,1,2";
        String reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-slsj/homePage/waterovervie/getWaterOverviewList?fill_flag="+u, token);
        JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        return jsonObject;
    }
    @ApiOperation("获取水质监测等级")
    @RequestMapping(value = "listDataK", method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getGateStationListDataK() {
        Map map = new HashMap();
        List<JSONObject> list = new ArrayList<>();
        map.put("username", "sjcsyh");
        map.put("password", "111111");
        String result = HttpUtil.doPost("http://10.0.2.11:8077/api-auth/login/userLogin", map);
        String token = JSON.parseObject(result).getString("token");
//        String u = "0,1,2";
        String  cezhan = "";
        cezhan = "AEA37070207B001";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        String kb = format.format(calendar.getTime());
        String reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-szbh/szbh/waterquality/queryMonitorOnlineData?stcd="+cezhan+"&startTM="+str+"&endTM="+str, token);
        JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject);
        cezhan = "AEA37070207B002";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-szbh/szbh/waterquality/queryMonitorOnlineData?stcd="+cezhan+"&startTM="+str+"&endTM="+str, token);
        JSONObject jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject1);
        cezhan = "AAC37020112B001";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-szbh/szbh/waterquality/queryMonitorOnlineData?stcd="+cezhan+"&startTM="+str+"&endTM="+str, token);
        JSONObject jsonObject2 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject2);
        cezhan = "AEA37060204B001";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-szbh/szbh/waterquality/queryMonitorOnlineData?stcd="+cezhan+"&startTM="+str+"&endTM="+str, token);
        JSONObject jsonObject3 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject3);
        cezhan = "AEA37060102B001";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-szbh/szbh/waterquality/queryMonitorOnlineData?stcd="+cezhan+"&startTM="+str+"&endTM="+str, token);
        JSONObject jsonObject4 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject4);
        cezhan = "AEA37060604B001";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-szbh/szbh/waterquality/queryMonitorOnlineData?stcd="+cezhan+"&startTM="+str+"&endTM="+str, token);
        JSONObject jsonObject5 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject5);
        return list;
    }
    @ApiOperation("获取水位曲线图时间")
    @RequestMapping(value = "listDataKX", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getGateStationlistDataKX() {
        Map map = new HashMap();
        map.put("username", "sjcsyh");
        map.put("password", "111111");
        String result = HttpUtil.doPost("http://10.0.2.11:8077/api-auth/login/userLogin", map);
        String token = JSON.parseObject(result).getString("token");
//        String u = "0,1,2";
        String type = "1";
        String lx = "0";
        String shuju = "0";
        String ui = "y";
        String fag = "2";
        String reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag, token);
        JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        return jsonObject;
    }
    @ApiOperation("获取流量曲线图时间")
    @RequestMapping(value = "listDataKXU", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getGateStationlistDataKXU() {
        Map map = new HashMap();
        map.put("username", "sjcsyh");
        map.put("password", "111111");
        String result = HttpUtil.doPost("http://10.0.2.11:8077/api-auth/login/userLogin", map);
        String token = JSON.parseObject(result).getString("token");
//        String u = "0,1,2";
        String type = "1";
        String lx = "2";
        String shuju = "1";
        String ui = "y";
        String fag = "2";
        String reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag, token);
        JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        return jsonObject;
    }
    @ApiOperation("获取当前年份流量曲线图")
    @RequestMapping(value = "listDataKXUY", method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getGateStationlistDataKXUY() {
        List<JSONObject> list = new ArrayList<>();
        String slnm = "";
        String result= "";
        Map map = new HashMap();
        map.put("username", "sjcsyh");
        map.put("password", "111111");
        result = HttpUtil.doPost("http://10.0.2.11:8077/api-auth/login/userLogin", map);
        String token = JSON.parseObject(result).getString("token");
//        String u = "0,1,2";
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String str = format.format(new Date());
        String start = str +"-01-01";
        String end =  str+"-01-31";
        String type = "1";
        String lx = "2";
        String shuju = "1";
        String ui = "y";
        String fag = "2";
        slnm = "灰埠泵站";
        String reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject);
         start = str +"-02-01";
         end =  str+"-02-28";
         reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject02 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject02);
        start = str +"-03-01";
        end =  str+"-03-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject03 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject03);
        start = str +"-04-01";
        end =  str+"-04-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject04 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject04);
        start = str +"-05-01";
        end =  str+"-05-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject05 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject05);
        start = str +"-06-01";
        end =  str+"-06-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject06 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject06);
        start = str +"-07-01";
        end =  str+"-07-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject07 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject07);
        start = str +"-08-01";
        end =  str+"-08-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject08 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject08);
        start = str +"-09-01";
        end =  str+"-09-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject09 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject09);
        start = str +"-10-01";
        end =  str+"-10-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject10 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject10);
        start = str +"-11-01";
        end =  str+"-11-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject11 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject11);
        start = str +"-12-01";
        end =  str+"-12-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject12 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject12);
        slnm = "宋庄泵站";
        start = str +"-01-01";
        end =  str+"-01-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject1);
        start = str +"-02-01";
        end =  str+"-02-28";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject102 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject102);
        start = str +"-03-01";
        end =  str+"-03-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject103 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject103);
        start = str +"-04-01";
        end =  str+"-04-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject104 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject104);
        start = str +"-05-01";
        end =  str+"-05-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject105 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject105);
        start = str +"-06-01";
        end =  str+"-06-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject106 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject106);
        start = str +"-07-01";
        end =  str+"-07-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject107 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject107);
        start = str +"-08-01";
        end =  str+"-08-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject108 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject108);
        start = str +"-09-01";
        end =  str+"-09-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject109 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject109);
        start = str +"-10-01";
        end =  str+"-10-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject110 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject110);
        start = str +"-11-01";
        end =  str+"-11-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject111 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject111);
        start = str +"-12-01";
        end =  str+"-12-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject112 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject112);
        slnm = "高瞳泵站";
        start = str +"-01-01";
        end =  str+"-01-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject2 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject2);
        start = str +"-02-01";
        end =  str+"-02-28";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject202 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject202);
        start = str +"-03-01";
        end =  str+"-03-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject203 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject203);
        start = str +"-04-01";
        end =  str+"-04-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject204 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject204);
        start = str +"-05-01";
        end =  str+"-05-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject205 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject205);
        start = str +"-06-01";
        end =  str+"-06-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject206 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject206);
        start = str +"-07-01";
        end =  str+"-07-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject207 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject207);
        start = str +"-08-01";
        end =  str+"-08-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject208 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject208);
        start = str +"-09-01";
        end =  str+"-09-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject209 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject209);
        start = str +"-10-01";
        end =  str+"-10-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject210 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject210);
        start = str +"-11-01";
        end =  str+"-11-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject211 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject211);
        start = str +"-12-01";
        end =  str+"-12-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject212 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject212);
        slnm = "黄水河泵站";
        start = str +"-01-01";
        end =  str+"-01-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject3 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject3);
        start = str +"-02-01";
        end =  str+"-02-28";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject302 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject302);
        start = str +"-03-01";
        end =  str+"-03-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject303 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject303);
        start = str +"-04-01";
        end =  str+"-04-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject304 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject304);
        start = str +"-05-01";
        end =  str+"-05-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject305 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject305);
        start = str +"-06-01";
        end =  str+"-06-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject306 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject306);
        start = str +"-07-01";
        end =  str+"-07-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject307 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject307);
        start = str +"-08-01";
        end =  str+"-08-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject308 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject308);
        start = str +"-09-01";
        end =  str+"-09-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject309 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject309);
        start = str +"-10-01";
        end =  str+"-10-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject310 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject310);
        start = str +"-11-01";
        end =  str+"-11-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject311 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject311);
        start = str +"-12-01";
        end =  str+"-12-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject312 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject312);
        return list;
    }
    @ApiOperation("获取指定年份流量曲线图")
    @RequestMapping(value = "listDataKXUYU", method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getGateStationlistDataKXUYU(String str) {
        List<JSONObject> list = new ArrayList<>();
        String slnm = "";
        String result= "";
        Map map = new HashMap();
        map.put("username", "sjcsyh");
        map.put("password", "111111");
        result = HttpUtil.doPost("http://10.0.2.11:8077/api-auth/login/userLogin", map);
        String token = JSON.parseObject(result).getString("token");
//        String u = "0,1,2";
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String start = str +"-01-01";
        String end =  str+"-01-31";
        String type = "1";
        String lx = "2";
        String shuju = "1";
        String ui = "y";
        String fag = "2";
        slnm = "灰埠泵站";
        String reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject);
        start = str +"-02-01";
        end =  str+"-02-28";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject02 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject02);
        start = str +"-03-01";
        end =  str+"-03-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject03 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject03);
        start = str +"-04-01";
        end =  str+"-04-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject04 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject04);
        start = str +"-05-01";
        end =  str+"-05-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject05 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject05);
        start = str +"-06-01";
        end =  str+"-06-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject06 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject06);
        start = str +"-07-01";
        end =  str+"-07-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject07 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject07);
        start = str +"-08-01";
        end =  str+"-08-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject08 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject08);
        start = str +"-09-01";
        end =  str+"-09-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject09 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject09);
        start = str +"-10-01";
        end =  str+"-10-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject10 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject10);
        start = str +"-11-01";
        end =  str+"-11-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject11 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject11);
        start = str +"-12-01";
        end =  str+"-12-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject12 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject12);
        slnm = "宋庄泵站";
        start = str +"-01-01";
        end =  str+"-01-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject1);
        start = str +"-02-01";
        end =  str+"-02-28";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject102 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject102);
        start = str +"-03-01";
        end =  str+"-03-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject103 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject103);
        start = str +"-04-01";
        end =  str+"-04-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject104 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject104);
        start = str +"-05-01";
        end =  str+"-05-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject105 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject105);
        start = str +"-06-01";
        end =  str+"-06-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject106 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject106);
        start = str +"-07-01";
        end =  str+"-07-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject107 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject107);
        start = str +"-08-01";
        end =  str+"-08-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject108 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject108);
        start = str +"-09-01";
        end =  str+"-09-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject109 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject109);
        start = str +"-10-01";
        end =  str+"-10-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject110 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject110);
        start = str +"-11-01";
        end =  str+"-11-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject111 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject111);
        start = str +"-12-01";
        end =  str+"-12-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject112 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject112);
        slnm = "高瞳泵站";
        start = str +"-01-01";
        end =  str+"-01-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject2 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject2);
        start = str +"-02-01";
        end =  str+"-02-28";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject202 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject202);
        start = str +"-03-01";
        end =  str+"-03-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject203 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject203);
        start = str +"-04-01";
        end =  str+"-04-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject204 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject204);
        start = str +"-05-01";
        end =  str+"-05-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject205 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject205);
        start = str +"-06-01";
        end =  str+"-06-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject206 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject206);
        start = str +"-07-01";
        end =  str+"-07-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject207 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject207);
        start = str +"-08-01";
        end =  str+"-08-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject208 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject208);
        start = str +"-09-01";
        end =  str+"-09-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject209 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject209);
        start = str +"-10-01";
        end =  str+"-10-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject210 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject210);
        start = str +"-11-01";
        end =  str+"-11-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject211 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject211);
        start = str +"-12-01";
        end =  str+"-12-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject212 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject212);
        slnm = "黄水河泵站";
        start = str +"-01-01";
        end =  str+"-01-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject3 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject3);
        start = str +"-02-01";
        end =  str+"-02-28";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject302 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject302);
        start = str +"-03-01";
        end =  str+"-03-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject303 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject303);
        start = str +"-04-01";
        end =  str+"-04-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject304 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject304);
        start = str +"-05-01";
        end =  str+"-05-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject305 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject305);
        start = str +"-06-01";
        end =  str+"-06-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject306 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject306);
        start = str +"-07-01";
        end =  str+"-07-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject307 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject307);
        start = str +"-08-01";
        end =  str+"-08-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject308 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject308);
        start = str +"-09-01";
        end =  str+"-09-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject309 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject309);
        start = str +"-10-01";
        end =  str+"-10-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject310 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject310);
        start = str +"-11-01";
        end =  str+"-11-30";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject311 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject311);
        start = str +"-12-01";
        end =  str+"-12-31";
        reslt = HttpUtil.doGet("http://10.0.2.11:8077/api-rcdd/rcdd/waterInfo/getGateStationList?sttp="+type+"&tm_type="+lx+"&if_init="+shuju+"&isOtherSystem="+ui+"&fill_flag="+fag+"&starttm="+start+"&end="+end+"&slnm="+slnm, token);
        JSONObject jsonObject312 = com.alibaba.fastjson.JSONObject.parseObject(reslt);
        list.add(jsonObject312);
        return list;
    }



}



