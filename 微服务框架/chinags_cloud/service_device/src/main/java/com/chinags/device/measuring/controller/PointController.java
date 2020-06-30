package com.chinags.device.measuring.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.LoginUser;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.measuring.entity.Point;
import com.chinags.device.measuring.entity.PointAlarm;
import com.chinags.device.measuring.service.PointAlarmService;
import com.chinags.device.measuring.service.PointService;
import com.chinags.device.measuring.util.PointUtil;
import com.chinags.device.threshold.entity.ThresholdVal;
import com.chinags.device.threshold.service.ThresholdValService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 测点维护controller
 * @Author : Mark_Wang
 * @Date : 2019/12/5
 **/
@Api("测点维护管理controller")
@RestController
@CrossOrigin
@RequestMapping("/point")
public class PointController extends BaseController {

    @Autowired
    private DeivceService deivceService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ThresholdValService thresholdValService;

    @Autowired
    private PointAlarmService pointAlarmService;

    @ApiOperation("保存测点维护信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result savePoint(@RequestBody Point point) {
        // 检查正常测点信息数据是否录入过
        Point point1 = pointService.getByPointIdAndDateNum(point.getPid(), point.getDateNum());
        if (point1 != null && !point1.getId().equals(point.getId())) {
            return new Result(false, StatusCode.ERROR, "测点期数已录入过");
        }
        // TODO 测点信息在报警中存在也不允许再录入正常的本期数据？
        // 检查测点报警信息是否录入过
        PointAlarm pointAlarm1 = pointAlarmService.getByPointIdAndDateNum(point.getPid(), point.getDateNum());
        if (pointAlarm1 != null) {
            return new Result(false, StatusCode.ERROR, "测点期数已录入过");
        }
        // 获取测点的沉浮阈值
        ThresholdVal thresholdVal = thresholdValService.selectByDeceiveId(point.getPid());
        point.setUpdateBy(getLoginUser().getUsercode());
        point.setUpdateDate(new Date());
        // 计算累计沉浮：累计沉浮=本期高程-初始值
        double upDownSum = point.getAltitude() - point.getInValue();
        point.setUpDownSum(upDownSum);
        // 如果测点是水平测点：Y轴累计沉浮=Y轴-Y轴初始值
        if (point.getSuch().equals("水平")) {
            point.setYUpDownSum(point.getYAltitude() - point.getYInValue());
        }
        // 获取前一期的测点维护信息
        int oldNum = PointUtil.dataNumOldByThis(point.getDateNum());
        Point pointFront = pointService.getByPointIdAndDateNum(point.getPid(), oldNum);
        if (pointFront != null) {
            // 如果存在前一期的测点维护信息：本期沉浮=本期高程-上期高程
            point.setUpDown(point.getAltitude() - pointFront.getAltitude());
            // 如果存在前一期的测点维护信息：Y轴本期沉浮=本期Y轴-上期Y轴
            if (point.getSuch().equals("水平")) {
                point.setYUpDown(point.getYAltitude() - pointFront.getYAltitude());
            }
        } else {
            // 如果不存在前一期的测点维护信息：本期沉浮=本期高程-初始值
            point.setUpDown(point.getAltitude() - point.getInValue());
            // 如果不存在前一期的测点维护信息：Y轴本期沉浮=Y轴-Y轴初始值
            if (point.getSuch().equals("水平")) {
                point.setYUpDown(point.getYAltitude() - point.getYInValue());
            }
        }
        // 获取后一期的测点维护信息
        int otherNum = PointUtil.dataNumOtherByThis(point.getDateNum());
        Point pointOld = pointService.getByPointIdAndDateNum(point.getPid(), otherNum);
        if (pointOld != null) {
            // 后一期的本期沉浮=后一期的高程-本期高程
            pointOld.setUpDown(pointOld.getAltitude() - point.getAltitude());
            // 后一期的Y轴本期沉浮=后一期的Y轴-本期Y轴
            if (point.getSuch().equals("水平")) {
                pointOld.setYUpDown(pointOld.getYAltitude() - point.getYAltitude());
            }
            // 更新后一期的测点维护数据
            pointService.save(pointOld);
        }
        // 判断该测点的阈值是否存在，如果存在判断是否启用阈值，阈值存在且启用的情况下才进行阈值的范围校验
        // TODO 现在没有Y轴的阈值区间，等到获取到Y轴阈值的区间需要把Y轴超限的数据中的Y轴阈值区间写入到报警数据中
        if (thresholdVal != null && thresholdVal.getIsUse().equals("1")) {
            // 检查数值是否超阈值
            Result result = PointUtil.checkValue(point, thresholdVal);
            if (!result.isStatus()) {
                // 测点报警信息入库
                PointAlarm pointAlarm = JSON.parseObject(JSON.toJSONString(point), PointAlarm.class);
                pointAlarm.setNowUpper(thresholdVal.getNowUpper());
                pointAlarm.setNowLower(thresholdVal.getNowLower());
                pointAlarm.setAddLower(thresholdVal.getAddLower());
                pointAlarm.setAddUpper(thresholdVal.getAddUpper());
                pointAlarm.setMsg(result.getMessage());
                pointAlarmService.save(pointAlarm);
                return new Result(true, StatusCode.OK, result.getMessage());
            }
        }
        // 写入本期的测点维护数据
        pointService.save(point);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("按照条件查询测点维护信息")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public Result findByPointInfo(@RequestBody Point point) {
        List<Point> points = pointService.getAllByPoint(point);
        if (CollectionUtils.isEmpty(points)) {
            return new Result(false, StatusCode.ERROR, "没有测点维护信息");
        }
        return new Result(true, StatusCode.OK, "查询成功", points);
    }

    @ApiOperation("根据测点id查询该测点下所有的测点维护信息")
    @RequestMapping(value = "/findByPointId/{pid}", method = RequestMethod.GET)
    public Result findByPointId(@PathVariable("pid") String pid) {
        List<Point> points = pointService.getPointsByPointId(pid);
        return new Result(true, StatusCode.OK, "查询成功", points);
    }

    @ApiOperation("按照测点id和期数查询测点维护信息")
    @RequestMapping(value = "/form/{pid}/{dateNum}", method = RequestMethod.GET)
    public Result findByPointIdAndDateNum(@PathVariable("pid") String pid, @PathVariable("dateNum") int dateNum) {
        Point point = new Point();
        if (dateNum == 0) {
            Device device = deivceService.selectById(pid);
            point = PointUtil.deviceToPoint(device);
            point.setCreateBy(getLoginUser().getLogincode());
        } else {
            point = pointService.getByPointIdAndDateNum(pid, dateNum);
        }
        return new Result(true, StatusCode.OK, "查询成功", point);
    }

    @ApiOperation("根据年度和测点信息查询测点维护信息")
    @RequestMapping(value = "/year/{year}", method = RequestMethod.POST)
    public Result findByYearAndPoint(@PathVariable("year") int year, @RequestBody Point point) {
        return new Result(true, StatusCode.OK, "查询成功", JSON.parseArray(pointService.getPointsByYearAndPoint(year, point)));
    }

    @ApiOperation("根据年度和测点id查询测点维护信息")
    @RequestMapping(value = "/year/{year}/{pid}", method = RequestMethod.GET)
    public Result findByYearAndPointId(@PathVariable("year") int year, @PathVariable("pid") String pid) {
        return new Result(true, StatusCode.OK, "查询成功", pointService.getPointsByYearAndPointId(year, pid));
    }

    @ApiOperation("根据月份区间和测点信息查询测点维护信息")
    @RequestMapping(value = "/month/{startMonth}/{endMonth}", method = RequestMethod.POST)
    public Result findByStartMonthEndMonthAndPoint(@PathVariable("startMonth") String startMonth, @PathVariable("endMonth") String endMonth, @RequestBody Point point) {
        return new Result(true, StatusCode.OK, "查询成功", JSON.parseArray(pointService.getPointsByStartAndEndDateNum(point, startMonth, endMonth)));
    }

    @ApiOperation("根据月份区间和测点id查询测点维护信息")
    @RequestMapping(value = "/month/{startMonth}/{endMonth}/{pid}", method = RequestMethod.GET)
    public Result findByStartMonthEndMonthAndPointId(@PathVariable("startMonth") int startMonth, @PathVariable("endMonth") int endMonth, @PathVariable("pid") String pid) {
        if (endMonth < startMonth) {
            return new Result(true, StatusCode.ERROR, "结束时间不能小于开始时间");
        }
        return new Result(true, StatusCode.OK, "查询成功", pointService.getPointsByStartAndEndDateNumAndPointId(startMonth, endMonth, pid));
    }

    /**
     * 根据设施设备代码级联查询人工传感器设备测点维护信息
     * @return
     */
    @ApiOperation("根据设施设备代码级联查询人工传感器设备测点维护信息")
    @RequestMapping(value = "/findAllByDeviceCode/{deviceCode}", method = RequestMethod.GET)
    public Result findAllByDeviceCode(@PathVariable("deviceCode") String deviceCode) {
        List<Point> points;
        try {
            Device device = deivceService.selectByDeviceCode(deviceCode);
            if (device == null) {
                return new Result(true, StatusCode.OK, "获取成功", ListUtils.newArrayList());
            }
            points = pointService.findAllByDeviceId(device.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", points);
    }

    /**
     * 查询所有的人工传感器设备测点维护信息
     * @return
     */
    @ApiOperation("查询所有的人工传感器设备测点维护信息")
    @RequestMapping(value = "/findAllRenGong", method = RequestMethod.GET)
    public Result findAllRenGong() {
        List<Point> points;
        try {
            points = pointService.findAllRenGong();
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", points);
    }

    /**
     * 根据设施设备代码级联查询人工传感器设备测点维护信息
     * @return
     */
    @ApiOperation("根据设施设备代码级联查询人工传感器设备测点维护信息")
    @RequestMapping(value = "/findAllByDeviceCodeToK/{deviceCode}", method = RequestMethod.GET)
    public Result findAllByDeviceCodeToK(@PathVariable("deviceCode") String deviceCode) {
        List<Point> points;
        Integer shui =0;
        Integer gao = 0;
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        try {
            Device device = deivceService.selectByDeviceCode(deviceCode);
            if (device == null) {
                return new Result(true, StatusCode.OK, "获取成功", ListUtils.newArrayList());
            }
            points = pointService.findAllByDeviceId(device.getId());
            if(points.size()>0){
                for(Point ti:points){
                    if("水平".equals(ti.getSuch())){
                        shui+=1;
                    }else if("高程".equals(ti.getSuch())){
                        gao+=1;
                    }
                }
                map.put("shuiping",shui);
                map.put("gaocheng",gao);
                list.add(map);
            }

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    /**
     * 根据设施设备代码级联查询人工传感器设备测点维护信息
     * @return
     */
    @ApiOperation("根据设施设备代码级联查询人工传感器设备测点维护信息")
    @RequestMapping(value = "/findAllByDeviceCodeTo", method = RequestMethod.GET)
    public Result findAllByDeviceCodeTo() {
        List<Point> points;
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        List<Device> devices;
        List<Point> pointList = new ArrayList<>();
        Integer shuiping = 0;
        Integer gaocheng = 0;
        try {
            devices = deivceService.findIsHavePointTopList();
            if(devices.size()>0) {
                for (Device ti : devices) {
                    points = pointService.findAllByDeviceId(ti.getId());
                    if (points.size() > 0) {
                        pointList.addAll(points);
                    }
                }
            }
            if(pointList.size()>0){
                for(Point k:pointList){
                    if("水平".equals(k.getSuch())){
                        shuiping+=1;
                    }else if("高程".equals(k.getSuch())){
                        gaocheng+=1;
                    }
                }
            }
            map.put("shuiping",shuiping);
            map.put("gaocheng",gaocheng);
            list.add(map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }



}
