package com.chinags.device.measuring.service;

import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.measuring.dao.PointDao;
import com.chinags.device.measuring.entity.Point;
import com.chinags.device.measuring.util.PointUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测点维护接口
 * @Author : Mark_wang
 * @Date : 2019-12-4
 **/
@Service
public class PointService {

    @Autowired
    private PointDao pointDao;

    @Autowired
    private DeivceService deivceService;

    /**
     * 保存测点维护信息
     * @param point
     */
    public void save(Point point) {
        pointDao.save(point);
    }

    /**
     * 根据测点id和期数查询测点维护信息
     * @param pid
     * @param dateNum
     * @return
     */
    public Point getByPointIdAndDateNum(String pid, int dateNum) {
        return pointDao.getByPointIdAndDateNum(pid, dateNum);
    }

    /**
     * 根据point信息查询符合条件和期数范围的测点维护信息最后期数的集合
     * @param point
     * @return
     */
    public List<Point> getAllByPoint(Point point) {
        // 获取所有的测点维护信息中的最后一期的信息
        List<Point> pointList = pointDao.getAllBySuch(point.getSuch());
        Map<String, Point> pointMap = new HashMap<String, Point>();
        for (Point point1 : pointList) {
            pointMap.put(point1.getPid(), point1);
        }
        // 返回符合条件的测点维护的集合
        List<Point> points = new ArrayList<>();
        // 根据测点种类查询所有的测点信息
        List<Device> devices = deivceService.findByCgqTypeName(point.getSuch());
        List<Point> pointsList = PointUtil.dervicesToPoints(devices);
        if(!CollectionUtils.isEmpty(pointsList)) {
            for (Point point1 : pointsList) {
                Point point2 = pointMap.get(point1.getPid());
                if (point2 != null) {
                    points.add(point2);
                } else {
                    points.add(point1);
                }
            }
        }
        return points;
    }

    /**
     * 根据测点id获取测点维护信息集合
     * @param pid
     * @return
     */
    public List<Point> getPointsByPointId(String pid) {
        return pointDao.getPointsByPointId(pid);
    }

    /**
     * 根据期数区间获取所有的测点维护信息
     * @param point
     * @param startDate
     * @param endDate
     * @return
     */
    public String getPointsByStartAndEndDateNum(Point point, String startDate, String endDate) {
        // 根据point条件获取符合条件的测点基础信息
        Device device = PointUtil.pointToDevice(point);
        List<Device> devices = deivceService.findListByCondition(device);
        List<Point> points = PointUtil.dervicesToPoints(devices);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        List<String> dateNums = new ArrayList<>();
        dateNums.add(startDate);
        String strYear = startDate.substring(0, 4);
        String strMonth = startDate.substring(4);
        int year = Integer.parseInt(strYear);
        int month = Integer.parseInt(strMonth);
        for (int i = 1; i < 3; i++) {
            int mon = month + i;
            if (mon > 12) {
                int ye = year + 1;
                mon = mon - 12;
                dateNums.add(ye+"0"+mon);
            } else if (mon > 9) {
                dateNums.add(year+""+mon);
            } else {
                dateNums.add(year+"0"+mon);
            }
        }
        // 获取出符合年度的测点维护信息
        List<Point> pointList = pointDao.getPointsByStartAndEndDateNumAndSuch(Integer.parseInt(dateNums.get(0)), Integer.parseInt(dateNums.get(2)), point.getSuch());
        // 年度维护信息生成Map集合：key：pointId_dataNum  value:测点维护信息
        Map<String, Point> pointMap = new HashMap<String, Point>();
        for(Point point1 : pointList) {
            pointMap.put(point1.getPid()+"_"+point1.getDateNum(), point1);
        }
        // 拼接生成返回json
        String result = "[";
        if(!CollectionUtils.isEmpty(points)) {
            for (Point point1 : points) {
                result = result + "{\"sctionId\":\"" + point1.getSctionId() + "\",\"pid\":\"" + point1.getPid() + "\",\"pointId\":\"" + point1.getPointId() + "\",\"inValue\":" + point1.getInValue()+ ",\"yinValue\":" + point1.getYInValue() + ",\"startMonth\":" + startDate+ ",\"endMonth\":" + endDate;
                int num = 1;
                for (String dataNum : dateNums) {
                    String key = point1.getPid()+"_"+dataNum;
                    Point point2 = pointMap.get(key);
                    if (point2!=null) {
                        result = result + ",\"altitude"+num+"\":"+point2.getAltitude()+",\"altitudeError"+num+"\":"+point2.getAltitudeError()+",\"upDown"+num+"\":"+point2.getUpDown()+",\"upDownSum"+num+"\":"+point2.getUpDownSum()+"";
                        result = result + ",\"yaltitude"+num+"\":"+point2.getYAltitude()+",\"yaltitudeError"+num+"\":"+point2.getYAltitudeError()+",\"yupDown"+num+"\":"+point2.getYUpDown()+",\"yupDownSum"+num+"\":"+point2.getYUpDownSum()+"";
                    } else {
                        result = result + ",\"altitude"+num+"\":\"未录入\",\"altitudeError"+num+"\":\"未录入\",\"upDown"+num+"\":\"未录入\",\"upDownSum"+num+"\":\"未录入\"";
                        result = result + ",\"yaltitude"+num+"\":\"未录入\",\"yaltitudeError"+num+"\":\"未录入\",\"yupDown"+num+"\":\"未录入\",\"yupDownSum"+num+"\":\"未录入\"";
                    }
                    num++;
                }
                result = result + "},";
            }
        }
        result = result + "]";
        return result;
    }

    /**
     * 根据期数区间和测点id查询测点维护信息集合
     * @param startDate
     * @param endDate
     * @param pid
     * @return
     */
    public List<Point> getPointsByStartAndEndDateNumAndPointId(int startDate, int endDate, String pid) {
        return pointDao.getPointsByStartAndEndDateNumAndPointId(startDate, endDate, pid);
    }

    /**
     * 根据年度和测点信息查询测点维护信息
     * @param year
     * @param point
     * @return
     */
    public String getPointsByYearAndPoint(int year, Point point) {
        // 根据point条件获取符合条件的测点基础信息
        Device device = PointUtil.pointToDevice(point);
        List<Device> devices = deivceService.findListByCondition(device);
        List<Point> points = PointUtil.dervicesToPoints(devices);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        // 获取出符合年度的测点维护信息
        List<Point> pointList = pointDao.getPointsByYearAndSuch(year, point.getSuch());
        // 年度维护信息生成Map集合：key：pointId_dataNum  value:测点维护信息
        Map<String, Point> pointMap = new HashMap<String, Point>();
        for(Point point1 : pointList) {
            pointMap.put(point1.getPid()+"_"+point1.getDateNum(), point1);
        }
        // 拼接生成返回json
        String result = "[";
        if(!CollectionUtils.isEmpty(points)) {
            for (Point point1 : points) {
                result = result + "{\"sctionId\":\"" + point1.getSctionId() + "\",\"pid\":\"" + point1.getPid() + "\",\"pointId\":\"" + point1.getPointId() + "\",\"inValue\":" + point1.getInValue()+ ",\"yinValue\":" + point1.getYInValue() + ",\"year\":" + year;
                for (int month = 1; month < 4; month++) {
                    String key = point1.getPid()+"_"+year+"0"+month;
                    Point point2 = pointMap.get(key);
                    if (point2!=null) {
                        result = result + ",\"altitude"+month+"\":"+point2.getAltitude()+",\"altitudeError"+month+"\":"+point2.getAltitudeError()+",\"upDown"+month+"\":"+point2.getUpDown()+",\"upDownSum"+month+"\":"+point2.getUpDownSum()+"";
                        result = result + ",\"yaltitude"+month+"\":"+point2.getYAltitude()+",\"yaltitudeError"+month+"\":"+point2.getYAltitudeError()+",\"yupDown"+month+"\":"+point2.getYUpDown()+",\"yupDownSum"+month+"\":"+point2.getYUpDownSum()+"";
                    } else {
                        result = result + ",\"altitude"+month+"\":\"未录入\",\"altitudeError"+month+"\":\"未录入\",\"upDown"+month+"\":\"未录入\",\"upDownSum"+month+"\":\"未录入\"";
                        result = result + ",\"yaltitude"+month+"\":\"未录入\",\"yaltitudeError"+month+"\":\"未录入\",\"yupDown"+month+"\":\"未录入\",\"yupDownSum"+month+"\":\"未录入\"";
                    }
                }
                result = result + "},";
            }
        }
        result = result + "]";
        return result;
    }

    /**
     * 根据年度和测点id查询测点维护信息
     * @param year
     * @param pid
     * @return
     */
    public List<Point> getPointsByYearAndPointId(int year, String pid) {
        return pointDao.getPointsByYearAndPointId(year, pid);
    }

    /**
     * 根据设施设备ID级联查询人工传感器设备测点维护信息
     * @param deviceId
     * @return
     */
    public List<Point> findAllByDeviceId(String deviceId) {
        return pointDao.findAllByDeviceId(deviceId);
    }

    public List<Point> selectPoint(String cedian, String officeCode) {
        return pointDao.selectPoint(cedian,officeCode);
    }
    public List<Point> selectPointLb(String officeCode) {
        return pointDao.selectPointLb(officeCode);
    }

    /**
     * 查询所有的人工传感器设备测点维护信息
     * @return
     */
    public List<Point> findAllRenGong() {
        return pointDao.findAllRenGong();
    }
}
