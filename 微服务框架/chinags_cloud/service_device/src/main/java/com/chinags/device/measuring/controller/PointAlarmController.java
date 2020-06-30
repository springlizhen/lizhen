package com.chinags.device.measuring.controller;

import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.basic.service.DerviceOfficeService;
import com.chinags.device.client.UserClient;
import com.chinags.device.maintain.entity.MaintainInfo;
import com.chinags.device.maintain.service.MaintainInfoService;
import com.chinags.device.measuring.entity.PointAlarm;
import com.chinags.device.measuring.service.PointAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测点报警管理controller
 * @Author : Mark_Wang
 * @Date : 2020/3/5
 **/
@Api("测点报警管理controller")
@RestController
@CrossOrigin
@RequestMapping("/alarm")
public class PointAlarmController {
    @Autowired
    private PointAlarmService pointAlarmService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private DerviceOfficeService derviceOfficeService;
    @Autowired
    private MaintainInfoService maintainInfoService;
    @Autowired
    private DeivceService deivceService;

    @ApiOperation("查询测点报警信息列表")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ResponseBody
    public Result getPointAlarmsByPointAlarm(@RequestBody PointAlarm pointAlarm) {
        return new Result(true, StatusCode.OK, "查询成功", pointAlarmService.getPointAlarmsByPointAlarm(pointAlarm));
    }
    @ApiOperation("查询全部测点报警信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseBody
    public Result getPointAlarmsByPointAlarmTo() {
        return new Result(true, StatusCode.OK, "查询成功", pointAlarmService.getPointAlarmsByPointAlarmTo());
    }
    @ApiOperation("查询排序的测点信息")
    @RequestMapping(value = "/pid", method = RequestMethod.POST)
    @ResponseBody
    public Result getPointAlarmsByPid() {
        return new Result(true, StatusCode.OK, "查询成功", pointAlarmService.selectByCount());
    }
    @ApiOperation("查询测点信息")
    @RequestMapping(value = "/pidTo/{workId}/{sendUserName}", method = RequestMethod.GET)
    @ResponseBody
    public Result getPointAlarmsByPidTo(@PathVariable("workId") String wordId,@PathVariable("sendUserName") String sendUserName) {
        Result result1 = null;
        result1 = userClient.getPointAlarmsByPidTo(sendUserName);
        List<String> list = (List<String>)result1.getData();
        List<Object> list1 = new ArrayList<>();
        Map<String,Object> map = pointAlarmService.getPointAlarmsByPidTo(wordId);
        list1.add(map);
        if(list.size()>0){
            list1.add(list.get(0));
        }
        return new Result(true, StatusCode.OK, "查询成功",list1);
    }
    @ApiOperation("获取相应养护")
    @RequestMapping(value = "/listDateToK/{date}", method = RequestMethod.GET)
    @ResponseBody
    public Result listDateToK(@PathVariable("date") String date) {
        List<Map<String, Object>> objectList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        int gugan = 0;
        int guanli = 0;
        int fushu = 0;
        int gongheng = 0;
        if (null != date && !"".equals(date)) {
            String startDate = date + "-01-01";
            String endDate = date + "-12-31";
            List<MaintainInfo> list = maintainInfoService.selectBymaintainDate(startDate, endDate);
            if (list.size() > 0) {
                for (MaintainInfo toL : list) {
                    Device device = deivceService.getbyId(toL.getDeceiveId());
                    DerviceOffice derviceOffice = derviceOfficeService.selectByoffice(device.getDeviceClass());
                    String codes = derviceOffice.getParentCodes();
                    String[] code = codes.split(",");
                    //获取最高的设施设备名称
                    DerviceOffice derviceOffice1 = derviceOfficeService.selectByoffice(code[1]);
                    if ("骨干工程".equals(derviceOffice1.getOfficeName())) {
                        gugan += 1;
                    } else if ("管理设施".equals(derviceOffice1.getOfficeName())) {
                        guanli += 1;
                    } else if ("附属设施".equals(derviceOffice1.getOfficeName())) {
                        fushu += 1;
                    } else if ("工程设施".equals(derviceOffice1.getOfficeName())) {
                        gongheng += 1;
                    }

                }
                map.put("gugan", gugan);
                map.put("guanli", guanli);
                map.put("fushu", fushu);
                map.put("gongcheng", gongheng);
                objectList.add(map);


            } else {
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }
        } else {
            String startDate = date + "-01-01";
            String endDate = date + "-12-31";
            List<MaintainInfo> list = maintainInfoService.selectBymaintainDateTo();
            if (list.size() > 0) {
                for (MaintainInfo toL : list) {
                    Device device = deivceService.getbyId(toL.getDeceiveId());
                    DerviceOffice derviceOffice = derviceOfficeService.selectByoffice(device.getDeviceClass());
                    String codes = derviceOffice.getParentCodes();
                    String[] code = codes.split(",");
                    //获取最高的设施设备名称
                    DerviceOffice derviceOffice1 = derviceOfficeService.selectByoffice(code[1]);
                    if ("骨干工程".equals(derviceOffice1.getOfficeName())) {
                        gugan += 1;
                    } else if ("管理设施".equals(derviceOffice1.getOfficeName())) {
                        guanli += 1;
                    } else if ("附属设施".equals(derviceOffice1.getOfficeName())) {
                        fushu += 1;
                    } else if ("工程设施".equals(derviceOffice1.getOfficeName())) {
                        gongheng += 1;
                    }

                }
                map.put("骨干工程", gugan);
                map.put("管理设施", guanli);
                map.put("附属设施", fushu);
                map.put("工程设施", gongheng);
            }
            return new Result(true, StatusCode.OK, "获取成功", objectList);
        }
        return new Result(true, StatusCode.OK, "获取成功", objectList);
    }
    @ApiOperation("查询测点信息")
    @RequestMapping(value = "/pidToK/{workId}/{sendUserName}", method = RequestMethod.GET)
    @ResponseBody
    public Result getPointAlarmsByPidToK(@PathVariable("workId") String wordId,@PathVariable("sendUserName") String sendUserName) {
        Result result1 = null;
        result1 = userClient.getPointAlarmsByPidTo(sendUserName);
        List<String> list = (List<String>)result1.getData();
        List<Object> list1 = new ArrayList<>();
        Map<String,Object> map = pointAlarmService.getPointAlarmsByPidToK(wordId);
        list1.add(map);
        if(list.size()>0){
            list1.add(list.get(0));
        }
        return new Result(true, StatusCode.OK, "查询成功",list1);
    }

    @ApiOperation("修改测点报警信息处理状态")
    @RequestMapping(value = "/status/{id}", method = RequestMethod.GET)
    public Result updateStatusById(@PathVariable("id") String id) {
        pointAlarmService.updateStatusById(id);
        return new Result(true, StatusCode.OK, "处理完成");
    }

    @ApiOperation("根据id获取测点报警信息")
    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result findById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "查询成功", pointAlarmService.selectById(id));
    }
    @ApiOperation("获取相应测点")
    @RequestMapping(value = "/cedianbilie", method = RequestMethod.GET)
    @ResponseBody
    public Result cedianbilie() {
        //获取到类别为泵站的且有传感器的设施设备
        List<Map<String,Object>> mapList=new ArrayList<>();
        List<Device> list = deivceService.selectByDevice();
        List<Device> list1 = deivceService.selectByGco();
        Map<String,Object> map = new HashMap<>();
        map.put("shuiping",list.size());
        map.put("gaocheng",list1.size());
        mapList.add(map);
        return new Result(true, StatusCode.OK, "获取成功", mapList);
    }


}
