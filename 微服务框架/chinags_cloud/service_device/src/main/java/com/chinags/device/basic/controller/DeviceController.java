package com.chinags.device.basic.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.entity.TreeData;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.entity.DeviceParam;
import com.chinags.device.basic.entity.Field;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.basic.service.DerviceOfficeService;
import com.chinags.device.basic.service.DeviceParamService;
import com.chinags.device.basic.service.FieldService;
import com.chinags.device.client.UserClient;
import com.chinags.device.measuring.entity.Point;
import com.chinags.device.measuring.entity.PointAlarm;
import com.chinags.device.measuring.service.PointAlarmService;
import com.chinags.device.measuring.service.PointService;
import com.chinags.device.threshold.service.ThresholdValService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 设备
 *
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("设备管理controller")
@RestController
@CrossOrigin
@RequestMapping("/device")
public class DeviceController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DeviceController.class);
    @Autowired
    private DeivceService deivceService;
    @Autowired
    private DerviceOfficeService derviceOfficeService;

    @Autowired
    private DeviceParamService deviceParamService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private PointService pointService;
    @Autowired
    private PointAlarmService pointAlarmService;
    @Autowired
    private ThresholdValService thresholdValService;  //阈值管理

    @Autowired
    private UserClient userClient;

    /**
     * 分页获取设备信息
     *
     * @return
     */
    @ApiOperation("分页获取设备信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListPage(Device device) {
        PageResult<Device> page;
        try {
            //类似查询全部
            page = deivceService.find(device);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }

    /**
     * 获取机构名称
     *
     * @return
     */
    @ApiOperation("获取机构名称")
    @RequestMapping(value = "/kb", method = RequestMethod.GET)
    @ResponseBody
    public Result kb(String id) {
        Result result = null;
        String name = "";
        List<String> to = new ArrayList<>();
        try {
            Device device = deivceService.selectByDeviceName(id);
            String orgId = device.getOrgIdName();
            to.add(orgId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", to);
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    @ApiOperation("获取设备信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result officeForm(String id, String parentCode) {
        Device device = new Device();
        try {
            if (id != null) {
                device = deivceService.getAreaById(id);
            } else if (parentCode != null && StringUtils.isNotEmpty(parentCode) && !"0".equals(parentCode)) {
                Device o = deivceService.getAreaById(parentCode);
                device.setDeviceClass(o.getDeviceClass());
                device.setDeviceClassName(o.getDeviceClassName());
                device.setSort(o.getSort());
//                device.setDeviceCode(o.getDeviceCode());
                device.setOrgId(o.getOrgId());
                device.setOrgIdName(o.getOrgIdName());
                device.setAreaCode(o.getAreaCode());
                device.setCtlStatus(o.getCtlStatus());
                device.setUseStatus(o.getUseStatus());
                device.setTypeDicId(o.getTypeDicId());
                device.setDevicePosition(o.getDevicePosition());
                device.setSchUnit(o.getSchUnit());
                device.setParentCode(o.getId());
                device.setTreeNames(o.getDeviceName() + "/" + o.getDeviceName() + "/" + o.getDeviceName());
                device.setDeviceRelegation(o.getId());
                device.setDeviceRelegationName(o.getDeviceName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", device);
    }

    /**
     * 菜单树结构
     *
     * @return
     */
    @ApiOperation("设备树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String excludeCode) {
        List<TreeData> devices;
        try {
            devices = deivceService.treeDatas(excludeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", devices);
    }

    /**
     * 菜单树结构(根据权限)
     *
     * @return
     */
    @ApiOperation("设备树结构")
    @RequestMapping(value = "/treeDataT", method = RequestMethod.GET)
    public Result treeDataT(String excludeCode) {
        List<TreeData> devices;
        String userCode = getLoginUser().getUsercode();

        try {
            devices = deivceService.treeDatasTo(excludeCode, userCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", devices);
    }

    /**
     * 获取查询信息
     *
     * @return
     */
    @ApiOperation("获取基础查询信息")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Result query(String userCode) {
        Result result = null;
        result = userClient.findQuery(userCode);
        return new Result(true, StatusCode.OK, "获取成功", result);
    }


    /**
     * 根据officecode返回数据（三级联动）
     *
     * @return
     */
    @ApiOperation("返回信息")
    @RequestMapping(value = "/basics", method = RequestMethod.GET)
    public Result basics(String officeCode) {
        Result result = null;
        result = userClient.findQueryOfficeCode(officeCode);
        return new Result(true, StatusCode.OK, "获取成功", result);
    }
    /**
     *
     *
     * @return
     */
    @ApiOperation("返回管理站和管理所")
    @RequestMapping(value = "/basicsP", method = RequestMethod.GET)

    public Result basicsP(String officeCode) {
        Result result = null;
        result = userClient.findQueryOfficeCodeP(officeCode);
        List<Map<String,Object>> list = (List<Map<String,Object>>) result.getData();
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    /**
     * 根据所代码和类型查询
     * @param officeCode
     * @return
     */
    @ApiOperation("根据所代码和类型查询")
    @RequestMapping(value = "/officeListToLbZuLoK", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListToLbZuLoK(String officeCode,String deviceClassName) {
        List<Device> listAll = deivceService.selectByOfficeCode(officeCode,deviceClassName);
        return new Result(true, StatusCode.OK, "获取成功", listAll);
    }

    /**
     * 根据人员id返回基础信息
     *
     * @return
     */
    @ApiOperation("返回底部数据")
    @RequestMapping(value = "/basicsZ", method = RequestMethod.GET)
    public Result basicsZ(Integer pageSize, String tocode, String type,String kpName) {
        Result result = null;
        List<Object> listAll = new ArrayList<>();
        List<Object> listAllTo = new ArrayList<>();
        if (null == pageSize) {
            pageSize = 1;
        }
        Device device = new Device();
        device.setOrgId(tocode);
        List<Device> list = new ArrayList<>();

        //获取到最低级别的设备
        //类型和名字都有
        if (!"".equals(type) && null != type && !"".equals(kpName) && null != kpName) {
            list = deivceService.selectByOrgIdTo(tocode,type,kpName);
            // 没有类型有名字
        } else if(null == type && !"".equals(kpName) && null!= kpName){
            list = deivceService.selectByOrgIdToX(tocode,kpName);
            //有类型没名字
        }else if(!"".equals(type) && null != type && null == kpName){
            list = deivceService.selectByOrgIdB(tocode, type);
        }else if(null == type && null == kpName){
            list = deivceService.selectByOrgIdLO(tocode);
        }
        List<Device> listDevice = new ArrayList<>();
        if (list.size() > 0) {
            for (Device tp : list) {
                Map<String, Object> map1 = new HashMap<>();
                String value = "";
                String name = "";
                //获取分类编码
                String deviceClass = tp.getDeviceClass();
                String id = tp.getId();
                //根据编码获取到该类型的参数信息
                List<Field> listField = fieldService.selectByDeviceClass(deviceClass);
                for (Field too : listField) {
                    String Fileid = too.getId();
                    name = too.getFieldName();
                    //根据参数编码和设备编码获取到对应的数值
                    DeviceParam deviceParam = deviceParamService.selectByFileIdAndId(id, Fileid);
                    if (null != deviceParam) {
                        value = deviceParam.getValue1();
                        map1.put(name, value);
                    }
                }
                tp.setMap(map1);
                listAll.add(tp);
            }
        }
        //管理站编码不为空，但是管理所编码为空
        Integer count =  listAll.size();
        if((pageSize - 1) * 20<= count && count<=  pageSize * 20){
            for (int i = (pageSize - 1) * 20; i < count; i++) {

                listAllTo.add(listAll.get(i));
            }
            return new Result(true, StatusCode.OK, "获取成功", listAllTo);
        }else if((pageSize - 1) * 20>count){
            return new Result(true, StatusCode.OK, "获取成功", listAllTo);
        }else if(count>pageSize * 20){
            for (int i = (pageSize - 1) * 20; i < pageSize * 20; i++) {

                listAllTo.add(listAll.get(i));
            }
            return new Result(true, StatusCode.OK, "获取成功", listAllTo);
        }

        return new Result(true, StatusCode.OK, "获取成功", listAllTo);
    }

    /**
     * 测点实时信息
     */
    @ApiOperation("测点实时信息")
    @RequestMapping(value = "/actual", method = RequestMethod.GET)
    public Result actual(String officeCode,String cedian,Integer pageSize) throws ParseException {
        Result result = null;
        List<Object> objectList = new ArrayList<>();
        List<Point> list = new ArrayList<>();
        List<Device> listDevice = new ArrayList<>();
        Map<String,Object> map =new HashMap<>();
        //获取该级别下的所有子级
//        result = userClient.getziji(officeCode);
        if(null == pageSize){
            pageSize = 1;
        }
        //获取所有管理所下的所属机构的id
//        List<String> getlist = (List<String>)result.getData();
        listDevice = deivceService.selectByOrgIdZo(officeCode);
        if(listDevice.size()>0 && null != cedian && !"".equals(cedian)){
            List<Object> objectList1 = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Device ui:listDevice){
                //根据所属机构id和测点编码获取测点
                List<Point> list1 = new ArrayList<>();
                List<PointAlarm> list2 = new ArrayList<>();
                list1 = pointService.selectPoint(cedian,ui.getId());
                list2 = pointAlarmService.selectPointAlarm(cedian,ui.getId());
                if(list1.size()>0 && list2.size()>0){
                    String h = sdf.format(list1.get(0).getCreateDate());
                    Date date1 = sdf.parse(h);
                    String w = sdf.format(list1.get(0).getCreateDate());
                    Date date2 = sdf.parse(w);
                    int count = date1.compareTo(date2);
                    if(count == -1){
                        list2.get(0).setSheibei(ui.getDeviceName());
                        list2.get(0).setSuoshu(ui.getOrgIdName());
                        list2.get(0).setChaochu("超出阈值");
                        objectList1.add(list2.get(0));
                    }else {
                        list1.get(0).setSheibei(ui.getDeviceName());
                        list1.get(0).setSuoshu(ui.getOrgIdName());
                        objectList1.add(list1.get(0));
                    }
                }else if(list1.size()>0 && list2.size() == 0){
                    list1.get(0).setSheibei(ui.getDeviceName());
                    list1.get(0).setSuoshu(ui.getOrgIdName());
                    objectList1.add(list1.get(0));
                }else if(list1.size() ==0 && list2.size()>0){
                    list2.get(0).setSheibei(ui.getDeviceName());
                    list2.get(0).setSuoshu(ui.getOrgIdName());
                    list2.get(0).setChaochu("超出阈值");
                    objectList1.add(list2.get(0));
                }
            }
            Integer count = objectList1.size();
            if((pageSize - 1) * 20<= count && count<=  pageSize * 20){
                for(int i=(pageSize-1)*20;i<count;i++){
                    objectList.add(objectList1.get(i));
                }
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }else if((pageSize - 1) * 20>= count){
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }else if(count>pageSize*20){
                for(int i=((pageSize-1)*20);i<pageSize*20;i++){
                    objectList.add(objectList1.get(i));
                }
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }


        }else if(listDevice.size()>0 && null == cedian){
            List<Object> objectList1 = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Device ui:listDevice){
                //根据所属机构id和测点编码获取测点
                List<Point> list1 = new ArrayList<>();
                List<PointAlarm> list2 = new ArrayList<>();
                list1 = pointService.selectPointLb(ui.getId());
                list2 = pointAlarmService.selectPointAlarmLb(ui.getId());
                if(list1.size()>0 && list2.size()>0){
                    String h = sdf.format(list1.get(0).getCreateDate());
                    Date date1 = sdf.parse(h);
                    String w = sdf.format(list1.get(0).getCreateDate());
                    Date date2 = sdf.parse(w);
                    int count = date1.compareTo(date2);
                    if(count == -1){
                        list2.get(0).setSheibei(ui.getDeviceName());
                        list2.get(0).setSuoshu(ui.getOrgIdName());
                        list2.get(0).setChaochu("超出阈值");
                        objectList1.add(list2.get(0));
                    }else {
                        list1.get(0).setSheibei(ui.getDeviceName());
                        list1.get(0).setSuoshu(ui.getOrgIdName());
                        objectList1.add(list1.get(0));
                    }
                }else if(list1.size()>0 && list2.size() == 0){
                    list1.get(0).setSheibei(ui.getDeviceName());
                    list1.get(0).setSuoshu(ui.getOrgIdName());
                    objectList1.add(list1.get(0));
                }else if(list1.size() ==0 && list2.size()>0){
                    list2.get(0).setSheibei(ui.getDeviceName());
                    list2.get(0).setSuoshu(ui.getOrgIdName());
                    list2.get(0).setChaochu("超出阈值");
                    objectList1.add(list2.get(0));
                }
            }
//            Integer count = objectList.size();

            Integer count = objectList1.size();
            if((pageSize - 1) * 20<= count && count<=  pageSize * 20){
                for(int i=(pageSize-1)*20;i<count;i++){
                    objectList.add(objectList1.get(i));
                }
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }else if((pageSize - 1) * 20>= count){
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }else if(count>pageSize*20){
                for(int i=((pageSize-1)*20);i<pageSize*20;i++){
                    objectList.add(objectList1.get(i));
                }
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }
        }
        return new Result(true, StatusCode.OK, "获取成功", objectList);
    }
    /**
     * 测点历史信息
     */
    @ApiOperation("测点历史信息")
    @RequestMapping(value = "/actualK", method = RequestMethod.GET)
    public Result actualK(String officeCode,String cedian,Integer pageSize) throws ParseException {
        Result result = null;
        List<Object> objectList = new ArrayList<>();
        List<Point> list = new ArrayList<>();
        List<Device> listDevice = new ArrayList<>();
        Map<String,Object> map =new HashMap<>();
        //获取该级别下的所有子级
//        result = userClient.getziji(officeCode);
        if(null == pageSize){
            pageSize = 1;
        }
        //获取所有管理所下的所属机构的id
//        List<String> getlist = (List<String>)result.getData();

        listDevice = deivceService.selectByOrgIdZo(officeCode);
        if(listDevice.size()>0 && null != cedian && !"".equals(cedian)){
            List<Object> objectList1 = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Device ui:listDevice){
                //根据所属机构id和测点编码获取测点
                List<Point> list1 = new ArrayList<>();
                List<PointAlarm> list2 = new ArrayList<>();
                Map<String,Object> map1 = new HashMap<>();
                list1 = pointService.selectPoint(cedian,ui.getId());
                list2 = pointAlarmService.selectPointAlarm(cedian,ui.getId());
                if(list1.size()>0 && list2.size()>0){
                    for(Point ti:list1){
                        ti.setSheibei(ui.getDeviceName());
                        ti.setSuoshu(ui.getOrgIdName());
                    }
                    for(PointAlarm to:list2){
                        to.setChaochu("超出阈值");
                        to.setSheibei(ui.getDeviceName());
                        to.setSuoshu(ui.getOrgIdName());
                    }
                    objectList1.addAll(list1);
                    objectList1.addAll(list2);
                }else if(list1.size()>0 && list2.size() == 0){
                    for(Point ti:list1){
                        ti.setSheibei(ui.getDeviceName());
                        ti.setSuoshu(ui.getOrgIdName());
                    }
                    objectList1.addAll(list1);
                }else if(list1.size() ==0 && list2.size()>0){
                    for(PointAlarm to:list2){
                        to.setChaochu("超出阈值");
                        to.setSheibei(ui.getDeviceName());
                        to.setSuoshu(ui.getOrgIdName());
                    }
                    objectList1.addAll(list2);
                }

            }
            Integer count = objectList1.size();
            if((pageSize - 1) * 20<= count && count<=  pageSize * 20){
                for(int i=(pageSize-1)*20;i<count;i++){
                    objectList.add(objectList1.get(i));
                }
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }else if((pageSize - 1) * 20>= count){
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }else if(count>pageSize*20){
                for(int i=((pageSize-1)*20);i<pageSize*20;i++){
                    objectList.add(objectList1.get(i));
                }
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }

        }else if(listDevice.size()>0 && null == cedian){
            List<Object> objectList1 = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Device ui:listDevice){
                //根据所属机构id和测点编码获取测点
                List<Point> list1 = new ArrayList<>();
                List<PointAlarm> list2 = new ArrayList<>();
                list1 = pointService.selectPointLb(ui.getId());
                list2 = pointAlarmService.selectPointAlarmLb(ui.getId());
                if(list1.size()>0 && list2.size()>0){
                    for(Point ti:list1){
                        ti.setSheibei(ui.getDeviceName());
                        ti.setSuoshu(ui.getOrgIdName());
                    }
                    for(PointAlarm to:list2){
                        to.setChaochu("超出阈值");
                        to.setSheibei(ui.getDeviceName());
                        to.setSuoshu(ui.getOrgIdName());
                    }
                    objectList1.addAll(list1);
                    objectList1.addAll(list2);
                }else if(list1.size()>0 && list2.size() == 0){
                    for(Point ti:list1){
                        ti.setSheibei(ui.getDeviceName());
                        ti.setSuoshu(ui.getOrgIdName());
                    }
                    objectList1.addAll(list1);
                }else if(list1.size() ==0 && list2.size()>0){
                    for(PointAlarm to:list2){
                        to.setChaochu("超出阈值");
                        to.setSheibei(ui.getDeviceName());
                        to.setSuoshu(ui.getOrgIdName());
                    }
                    objectList1.addAll(list2);
                }

            }
//            Integer count = objectList.size();

            Integer count = objectList1.size();
            if((pageSize - 1) * 20<= count && count<=  pageSize * 20){
                for(int i=(pageSize-1)*20;i<count;i++){
                    objectList.add(objectList1.get(i));
                }
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }else if((pageSize - 1) * 20>= count){
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }else if(count>pageSize*20){
                for(int i=((pageSize-1)*20);i<pageSize*20;i++){
                    objectList.add(objectList1.get(i));
                }
                return new Result(true, StatusCode.OK, "获取成功", objectList);
            }
        }

        return new Result(true, StatusCode.OK, "获取成功", objectList);
    }

    /**
     * 菜单树结构
     *
     * @return
     */
    @ApiOperation("设备树结构")
    @RequestMapping(value = "/treeData2", method = RequestMethod.GET)
    public Result treeData2(String excludeCode) {
        List<TreeData> devices;
        try {
            devices = deivceService.treeDatas2(excludeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", devices);
    }

    /**
     * 获取设施设备数据，只展示传感器为是的，且已录入传感器不再重复显示
     * 展示直拉式列表，包含名称、编号和id
     * @return
     */
    @ApiOperation("设备树结构")
    @RequestMapping(value = "/treeData3", method = RequestMethod.GET)
    public Result treeData3(String excludeCode) {
        List<TreeData> devices;
        try {
//            devices = deivceService.treeDatas3(excludeCode);  //树状结构
            devices = deivceService.selectForThresholdVal();  //平铺列表
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", devices);
    }

    @ApiOperation("保存设备")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Device device) {
        if (device.getIsNewRecord()) {
            Device officeParent;
            officeParent = deivceService.getAreaById(device.getId());
            if (officeParent != null) {
                return new Result(true, StatusCode.ERROR, "设备编码已存在");
            }
            device.setPlanParentId((String) userClient.getofficeEndType(device.getOrgId(), Global.getConfig("dicData.planParentId")).getData());
            device.setStationId((String) userClient.getofficeEndType(device.getOrgId(), Global.getConfig("dicData.stationId")).getData());
            device.setOfficeId((String) userClient.getofficeEndType(device.getOrgId(), Global.getConfig("dicData.officeId")).getData());
        }
        device.setCreateBy(getLoginUser().getUsercode());
        device.setUpdateBy(getLoginUser().getUsercode());
        device.setCreateDate(new Date());
        device.setUpdateDate(new Date());
        this.deivceService.save(device);

        /*
        保存设备后，更新安全阈值信息
        如果该设备传感器选项为是，阈值列表进行显示，反之阈值列表不显示
         */
        //获取传感器信息
        String cgqStatus = device.getCgqStatus();
        String id = device.getId();
        if("是".equals(cgqStatus)){
            thresholdValService.updateIsShowByDeceiveId("1", id);
        }else{
            thresholdValService.updateIsShowByDeceiveId("0", id);
        }
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除设备")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(Device device) {
        if (deivceService.delete(device)) {
            return new Result(true, StatusCode.OK, "删除成功");
        } else {
            return new Result(true, StatusCode.ERROR, "请先删除子菜单");
        }
    }

    @ApiOperation("停用设备")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    @ResponseBody
    public Result disable(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return deivceService.disable(id);
        } else {
            return new Result(true, StatusCode.ERROR, "未选择设备");
        }
    }

    @ApiOperation("启用设备")
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    public Result enable(String id) {
        if (StringUtils.isNotEmpty(id)) {
            String result = deivceService.enable(id);
            return new Result(true, StatusCode.OK, result);
        } else {
            return new Result(true, StatusCode.ERROR, "未选择设备");
        }
    }

    @ApiOperation("获取代码值")
    @RequestMapping(value = "/paramValus", method = RequestMethod.GET)
    @ResponseBody
    public Result paramValus(String id) {
        if (StringUtils.isNotEmpty(id)) {
            DeviceParam deviceParam = new DeviceParam();
            deviceParam.setDeviceId(id);
            List<DeviceParam> result = deviceParamService.findAll(deviceParam);
            return new Result(true, StatusCode.OK, "获取成功", result);
        } else {
            return new Result(true, StatusCode.ERROR, "未选择设备");
        }
    }

    @ApiOperation("根据id工程档案案卷著录记录")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        Device device = deivceService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功", device);
    }
}
