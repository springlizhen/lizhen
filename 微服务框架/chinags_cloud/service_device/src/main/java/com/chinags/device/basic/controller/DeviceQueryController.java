package com.chinags.device.basic.controller;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.entity.TreeData;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.entity.DeviceParam;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.basic.service.DeviceParamService;
import com.chinags.device.client.UserClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 设备查询
 *
 * @author suijinchi
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("设备查询controller")
@RestController
@CrossOrigin
@RequestMapping("/deviceQuery")
public class DeviceQueryController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DeviceQueryController.class);

    @Autowired
    private DeivceService deivceService;

    /**
     * 根据传感器类型名称查询设备信息
     * @return
     */
    @ApiOperation("根据传感器类型名称查询设备信息")
    @RequestMapping(value = "/findByCgqTypeName/{cgqTypeName}", method = RequestMethod.GET)
    public Result findByCgqTypeName(@PathVariable("cgqTypeName") String cgqTypeName){
        List list = ListUtils.newArrayList();
        List<Device> devices;
        try {
            devices = deivceService.findByCgqTypeName(cgqTypeName);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Device d : devices) {
            HashMap map = MapUtils.newHashMap();
            map.put("equ", d.getDeviceName());
            map.put("subCenter", d.getPlanParentId());
            map.put("station", d.getStationId());
            map.put("office", d.getOfficeId());
            map.put("inValue", d.getCgqValue());
            map.put("pointId", d.getCgqCode());
            map.put("pid", d.getId());
            map.put("sctionId", d.getCgqGcdCode());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    /**
     * 根据指定信息查询设备信息列表
     * 分中心ID，管理站ID，管理所ID，设施ID，测点编号，测点种类（必填）（高程/水平）
     * @return
     */
    @ApiOperation("根据指定信息查询设备信息列表")
    @RequestMapping(value = "/findListByCondition", method = RequestMethod.GET)
    public Result findListByCondition(Device device){
        List<Device> devices;
        try {
            devices = deivceService.findListByCondition(device);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",devices);
    }

    /**
     * 根据管理所ID查询人工测点设备列表
     * @return
     */
    @ApiOperation("根据管理所ID查询人工测点设备列表")
    @RequestMapping(value = "/findListByOfficeId/{officeId}", method = RequestMethod.GET)
    public Result findListByOfficeId(@PathVariable("officeId") String officeId){
        List list = ListUtils.newArrayList();
        List<Device> devices;
        try {
            devices = deivceService.findListByOfficeId(officeId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        for (Device d : devices) {
            HashMap map = MapUtils.newHashMap();
            map.put("deviceId", d.getId());
            map.put("deviceName", d.getDeviceName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    /**
     * 查询具有人工测点的最顶级设备列表
     */
    @ApiOperation("查询具有人工测点的最顶级设备列表")
    @RequestMapping(value = "/findIsHavePointTopList", method = RequestMethod.GET)
    public Result findIsHavePointTopList(){
        List<Device> devices;
        try {
            devices = deivceService.findIsHavePointTopList();
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", devices);
    }

}
