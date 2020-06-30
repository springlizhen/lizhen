package com.chinags.device.measuring.util;

import com.alibaba.fastjson.JSON;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.lang.StringUtils;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.measuring.entity.Point;
import com.chinags.device.measuring.entity.PointAlarm;
import com.chinags.device.threshold.entity.ThresholdVal;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-12-18
 **/
public class PointUtil {
    /**
     * 判断计算的数值是否超过阈值限制
     * @param point
     * @param thresholdVal
     * @return
     */
    public static Result checkValue(Point point, ThresholdVal thresholdVal) {
        if (point.getSuch().equals("水平")) {
            if (point.getUpDown() > thresholdVal.getNowUpper()) {
                return new Result(false, StatusCode.ERROR, "X变形量超上限");
            }
            if (point.getUpDown() < thresholdVal.getNowLower()) {
                return new Result(false, StatusCode.ERROR, "X变形量超下限");
            }
            if (point.getUpDownSum() > thresholdVal.getAddUpper()) {
                return new Result(false, StatusCode.ERROR, "X累计变形量超上限");
            }
            if (point.getUpDownSum() < thresholdVal.getAddLower()) {
                return new Result(false, StatusCode.ERROR, "X累计变形量超下限");
            }
            if (point.getYUpDown() > thresholdVal.getYNowUpper()) {
                return new Result(false, StatusCode.ERROR, "Y变形量超上限");
            }
            if (point.getYUpDown() < thresholdVal.getYNowLower()) {
                return new Result(false, StatusCode.ERROR, "Y变形量超下限");
            }
            if (point.getYUpDownSum() > thresholdVal.getYAddUpper()) {
                return new Result(false, StatusCode.ERROR, "Y累计变形量超上限");
            }
            if (point.getYUpDownSum() < thresholdVal.getYAddLower()) {
                return new Result(false, StatusCode.ERROR, "Y累计变形量超下限");
            }
        } else {
            if (point.getUpDown() > thresholdVal.getNowUpper()) {
                return new Result(false, StatusCode.ERROR, "本期沉浮超上限");
            }
            if (point.getUpDown() < thresholdVal.getNowLower()) {
                return new Result(false, StatusCode.ERROR, "本期沉浮超下限");
            }
            if (point.getUpDownSum() > thresholdVal.getAddUpper()) {
                return new Result(false, StatusCode.ERROR, "累计沉浮超上限");
            }
            if (point.getUpDownSum() < thresholdVal.getAddLower()) {
                return new Result(false, StatusCode.ERROR, "累计沉浮超下限");
            }
        }
        return new Result(true, StatusCode.OK, null);
    }

    /**
     * 测点基础信息集合转测点维护信息集合
     * @param devices
     * @return
     */
    public static List<Point> dervicesToPoints(List<Device> devices) {
        if (CollectionUtils.isNotEmpty(devices)) {
            List<Point> points = new ArrayList<>();
            for (Device device : devices) {
                Point point = new Point();
                point.setPid(device.getId());
                if (StringUtils.isEmpty(device.getCgqValue()))
                    point.setInValue(0.0);
                else
                    point.setInValue(Double.valueOf(device.getCgqValue().toString()));
                if (StringUtils.isEmpty(device.getCgqValueYz()))
                    point.setYInValue(0.0);
                else
                    point.setYInValue(Double.valueOf(device.getCgqValueYz().toString()));
                point.setSctionId(device.getCgqGcdCode());
                point.setPointId(device.getCgqCode());
                point.setSubCenter(device.getPlanParentId());
                point.setStation(device.getStationId());
                point.setOffice(device.getOfficeId());
                // TODO 设施对应的测点基础信息不明确
                point.setEqu(device.getDeviceCode());
                point.setPid(device.getId());
                point.setPointId(device.getCgqCode());
                point.setSctionId(device.getCgqGcdCode());
                points.add(point);
            }
            return points;
        }
        return null;
    }

    /**
     * 测点信息转换基础信息
     * @param point
     * @return
     */
    public static Device pointToDevice(Point point) {
        if(point != null) {
            Device device = new Device();
            device.setPlanParentId(point.getSubCenter());
            device.setStationId(point.getStation());
            device.setOfficeId(point.getOffice());
            device.setId(point.getPid());
            device.setCgqTypeName(point.getSuch());
            // TODO 设施对应的测点基础信息不明确
            device.setDeviceCode(point.getEqu());
            return device;
        }
        return null;
    }

    /**
     * 基础信息转测点信息
     * @param device
     * @return
     */
    public static Point deviceToPoint(Device device) {
        if (device != null) {
            Point point = new Point();
            point.setPid(device.getId());
            if (StringUtils.isEmpty(device.getCgqValue()))
                point.setInValue(0.0);
            else
                point.setInValue(Double.valueOf(device.getCgqValue().toString()));
            if (StringUtils.isEmpty(device.getCgqValueYz()))
                point.setYInValue(0.0);
            else
                point.setYInValue(Double.valueOf(device.getCgqValueYz().toString()));
            point.setSctionId(device.getCgqGcdCode());
            point.setPointId(device.getCgqCode());
            point.setSubCenter(device.getPlanParentId());
            point.setStation(device.getStationId());
            point.setOffice(device.getOfficeId());
            point.setSuch(device.getCgqTypeName());
            // TODO 设施对应的测点基础信息不明确
            point.setEqu(device.getDeviceCode());
            return point;
        }
        return null;
    }

    /**
     * 根据测点的本期期数获取下一期期数
     * @param dataNum
     * @return
     */
    public static int dataNumOtherByThis(int dataNum) {
        dataNum = dataNum + 1;
        String strNum = String.valueOf(dataNum).substring(4);
        if (Integer.parseInt(strNum) == 13) {
            dataNum = dataNum + 100 - 12;
        }
        return dataNum;
    }

    /**
     * 根据测点的本期期数获取上一期期数
     * @param dataNum
     * @return
     */
    public static int dataNumOldByThis(int dataNum) {
        dataNum = dataNum - 1;
        String strNum = String.valueOf(dataNum).substring(4);
        if (Integer.parseInt(strNum) == 0) {
            dataNum = dataNum - 100 - 1;
        }
        return dataNum;
    }

}
