package com.chinags.device.basic.service;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.entity.TreeData;
import com.chinags.common.service.BaseService;
import com.chinags.common.utils.ResultMap;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.dao.DeviceDao;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.entity.DeviceParam;
import com.chinags.device.client.UserClient;
import com.chinags.device.threshold.dao.ThresholdValDao;
import com.chinags.device.threshold.entity.ThresholdVal;
import com.chinags.device.threshold.service.ThresholdValService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zhang
 */
@Service
public class DeivceService extends BaseService<Device>{

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DerviceOfficeService derviceOfficeService;
    @Autowired
    private DeviceParamService deviceParamService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ThresholdValService thresholdValService;
    @Autowired
    private ThresholdValDao thresholdValDao;

    /**
     * 获取左侧菜单栏
     * @param facility 查询对象
     * @return Office集合
     */
    public List<Device> findAll(Device facility){
        //菜单状态 正常
        facility.setUseStatus("0");
        Sort sort = new Sort(Sort.Direction.ASC, "treeLevel","treeSort");
        Example<Device> ex = Example.of(facility);
        return deviceDao.findAll(ex, sort);
    }

    /**
     * 获取树形结构
     * @param deviceCode 设备code
     * @return Device集合
     */
    public List<Device> treeData(String deviceCode){
        return deviceDao.findAll((Specification<Device>) (root, query, cb) -> getPredicate(deviceCode, query, cb, root.get("parentCodes"), root.get("id"), root.get("status"), root.get("treeLevel"), root.get("treeSort"), root));
    }

    /**
     * 获取特殊树形结构
     * @param deviceCode 设备code
     * @return Device集合
     */
    public List<TreeData> treeDatas(String deviceCode){
        List<Device> devices = treeData(deviceCode);
        List<TreeData> treeDatas = ListUtils.newArrayList();

        ResultMap<Device> resultMap = new ResultMap<>();
        //分类id
        List<DerviceOffice> derviceOffices = derviceOfficeService.treeData(null);
        for (DerviceOffice derviceOffice: derviceOffices) {
            TreeData treeData = new TreeData(derviceOffice.getId(), derviceOffice.getParentCode(), derviceOffice.getOfficeName(), "设备分类", "1");
            treeDatas.add(treeData);
        }
        resultMap.setEntityList(devices);
        //最顶级设备
//        List<Device> devicesFirst = resultMap.resultLists(true,"parentCode", "0");
        for (Device a : devices) {
            TreeData treeData = new TreeData(a.getId(), a.getDeviceClass(), a.getDeviceName(), "设备", "0");
            treeDatas.add(treeData);
        }
        //移除devices对应所有devicesFirst
//        devices.removeAll(devicesFirst);
//        for (Device a : devices) {
//            TreeData treeData = new TreeData(a.getId(), a.getParentCode(), a.getDeviceName(), "设备", "0");
//            treeDatas.add(treeData);
//        }
        return treeDatas;
    }

    /**
     * 获取名称树形结构
     * @param deviceCode 设备code
     * @return Device集合
     */
    public List<TreeData> treeDatas2(String deviceCode){
        List<Device> devices = treeData(deviceCode);
        List<TreeData> treeDatas = ListUtils.newArrayList();

        ResultMap<Device> resultMap = new ResultMap<>();
        resultMap.setEntityList(devices);
        //最顶级设备
        List<Device> devicesFirst = resultMap.resultLists(true,"parentCode", "0");
        for (Device a : devicesFirst) {
            TreeData treeData = new TreeData(a.getId(), a.getDeviceClass(), a.getDeviceName(), "设备", "0");
            treeDatas.add(treeData);
        }
        //移除devices对应所有devicesFirst
        devices.removeAll(devicesFirst);
        for (Device a : devices) {
            TreeData treeData = new TreeData(a.getId(), a.getParentCode(), a.getDeviceName(), "设备", "0");
            treeDatas.add(treeData);
        }
        return treeDatas;
    }

    /**
     * 获取编码树形结构
     * @param deviceCode 设备code
     * @return Device集合
     */
    public List<TreeData> treeDatas3(String deviceCode){
        List<Device> devices = treeData(deviceCode);
        List<TreeData> treeDatas = ListUtils.newArrayList();
        ResultMap<Device> resultMap = new ResultMap<>();
        resultMap.setEntityList(devices);
        //最顶级设备
        List<Device> devicesFirst = resultMap.resultLists(true,"parentCode", "0");
        for (Device a : devicesFirst) {
            TreeData treeData = new TreeData(a.getId(), a.getDeviceClass(), a.getDeviceCode(), "设备", "0");
            treeDatas.add(treeData);
        }
        //移除devices对应所有devicesFirst
        devices.removeAll(devicesFirst);
        for (Device a : devices) {
            TreeData treeData = new TreeData(a.getId(), a.getParentCode(), a.getDeviceCode(), "设备", "0");
            treeDatas.add(treeData);
        }
        return treeDatas;
    }

    /**
     * 查询设备列表给工程安全阈值使用
     * 查询传感器属性为是的设备
     */
    public List<TreeData> selectForThresholdVal(){
        /*
        查询阈值表中已存在的设备id
         */
        //获取全部的阈值信息
        List<ThresholdVal> valList = thresholdValDao.findAll();
        //存储设备id
        List<String> deviceIdList = Lists.newArrayList();
        for(ThresholdVal tv : valList){
            deviceIdList.add(tv.getDeceiveId());
        }
        //如果是第一次录入，没有deviceid，则乱输一个，保证数组不为空
        if(deviceIdList.size() == 0){
            deviceIdList.add("dgfdsgfhdsy3543dtjur5e6ewtdtgdfgds123132123sdfsdfsdfds21312321");
        }
        String[] deviceIds = new String[deviceIdList.size()];
        deviceIdList.toArray(deviceIds);
        List<Device> deviceList = deviceDao.selectForThresholdVal(deviceIds);
        List<TreeData> treeDatas = ListUtils.newArrayList();
        for(Device device : deviceList){
            //获取设备简称，没有的话赋值为空
            String shortName = device.getDeviceShortName();
            if(StringUtils.isNotEmpty(shortName)){
                shortName += "/";
            }else{
                shortName = "/";
            }
            TreeData treeData = new TreeData(device.getId(), "", shortName + device.getDeviceCode(), "设备", "0");
            treeDatas.add(treeData);
        }
        return treeDatas;
    }

    /**
     * 查询列表
     * @return office分页数据
     */
    public PageResult find(Device device) {
        PageRequest pageable;
        if(device.getOrderBy()==null|| "".equals(device.getOrderBy())) {
            pageable = PageRequest.of(device.getPageNo(), device.getPageSize(), device.getDesc(), "sort");
        } else {
            pageable = PageRequest.of(device.getPageNo(), device.getPageSize(), device.getDesc(), device.getOrderBy());
        }
        if(device.getOrgId()!=null){
            String treeLevel = deviceDao.getOrgId(device.getOrgId());
            if(treeLevel!=null) {
                device.setTreeLevel(Integer.valueOf(treeLevel));
            }
        }

        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(deviceDao.findAllPage(device,pageable));
    }

    /**
     * 保存机构
     * @param device 设备数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Device device){
        device.setTreeSort(Integer.parseInt(device.getSort()));
        device.setParentCode(device.getDeviceRelegation());
        //父级
        Device officeParent = getAreaById(device.getParentCode());
        //同级
        List<Device> list = deviceDao.selectByDevice(device.getParentCode());
        //子级
        List<Device> deviceList = new ArrayList<>();
        //如果是修改的话可能会有子级但是增加的时候不会产生子级-----修改情况
        if(device.getId()!=null && !"".equals(device.getId())){
            deviceList = deviceDao.selectByDevice(device.getId());

        }
        //如果有父级并且没有子级---- 单级别
        if(officeParent!=null && deviceList.size()==0) {
            device.setParentCodes(officeParent.getParentCodes() + officeParent.getId() + ",");
            device.setTreeSorts(officeParent.getTreeSorts() + String.format("%10d", device.getTreeSort()).replace(" ", "0") + ",");
            device.setTreeNames(officeParent.getTreeNames() + "/" + device.getDeviceName());
            if (device.getIsNewRecord()) {
                    device.setStatus("0");
            }
            //是最末级
            device.setTreeLeaf("1");
            //父级别的层次级别+1
            device.setTreeLevel(officeParent.getTreeLevel() + 1);
            officeParent.setTreeLeaf("0");
            deviceDao.save(officeParent);
            //如果有父级并且有子级----中间级别
        }else if(officeParent != null && deviceList.size() > 0){
            device.setParentCodes(officeParent.getParentCodes() + officeParent.getId() + ",");
            device.setTreeSorts(officeParent.getTreeSorts() + String.format("%10d", device.getTreeSort()).replace(" ", "0") + ",");
            device.setTreeNames(officeParent.getTreeNames() + "/" + device.getDeviceName());
            if (device.getIsNewRecord()) {
                device.setStatus("0");
            }
            //不是最末级
            device.setTreeLeaf("0");
            //父级别的层次级别+1
            device.setTreeLevel(officeParent.getTreeLevel() + 1);
            officeParent.setTreeLeaf("0");
            deviceDao.save(officeParent);
            //没有父级但是有子级
        }else if(officeParent == null && deviceList.size() > 0){
            device.setParentCode("0");
            device.setParentCodes("0,");
            device.setTreeSorts(String.format("%10d", device.getTreeSort()).replace(" ", "0") + ",");
            device.setTreeNames(device.getDeviceName());
            //不是最末级
            device.setTreeLeaf("0");
            device.setTreeLevel(0);
            if (device.getIsNewRecord()) {
                device.setStatus("0");
            }
            //父级别的层次级别+1
//            device.setTreeLevel(officeParent.getTreeLevel() + 1);
//            officeParent.setTreeLeaf("0");
//            deviceDao.save(officeParent);
        }
        else {
            device.setParentCode("0");
            device.setParentCodes("0,");
            device.setTreeSorts(String.format("%10d", device.getTreeSort()).replace(" ", "0") + ",");
            device.setTreeNames(device.getDeviceName());
            device.setTreeLeaf("1");
            device.setTreeLevel(0);
            if (device.getIsNewRecord()) {
                device.setStatus("0");
            }
        }
        String parentCode = null;
        if(!device.getIsNewRecord()){
            Device o = this.getAreaById(device.getId());
            device.setStatus(o.getStatus());
            parentCode = o.getParentCode();
        }
        deviceDao.save(device);

        if(parentCode!=null) {
            menuleav(parentCode);
        }
//        officeParent
        List deviceparams = ListUtils.newArrayList();
        if(device.getDicParams()!=null){
            for (Map.Entry<String,String> map: device.getDicParams().entrySet()){
                DeviceParam param = new DeviceParam();
                if(deviceParamService.countByDeviceIdAndCodeId(device.getId(), (map.getKey()))) {
                    List<String> idDeviceIdAndCodeId = deviceParamService.getIdDeviceIdAndCodeId(device.getId(), map.getKey());
                    param.setId(idDeviceIdAndCodeId.get(0));
                }
                param.setCodeId(map.getKey());
                param.setDeviceId(device.getId());
                param.setValue1(map.getValue());
                param.setCreateBy(device.getUpdateBy());
                param.setCreateDate(new Date());
                param.setUpdateBy(device.getUpdateBy());
                param.setUpdateDate(new Date());
                deviceparams.add(param);
            }
        }
        deviceParamService.saveAll(deviceparams);
    }

    /**
     * 重置树形结构下级
     * @param id
     */
    private void menuleav(String id){
        Integer count = deviceDao.getStopParentCodesCount(id);
        if(count==0){
            deviceDao.updateApplyNumById(id);
        }
    }

    /**
     * 删除设备
     * @param device 实体
     */
    public Boolean delete(Device device){
        int count = deviceDao.getParentCodesCount("%"+device.getId()+",%");
        if(count > 0) {
            return false;
        }
        Device o = getAreaById(device.getId());
        o.setStatus(Device.STATUS_DELETE);
        deviceDao.save(o);
        String parentCode = o.getParentCode();
        if(parentCode!=null) {
            menuleav(parentCode);
        }
        return true;
    }

    /**
     * 批量保存
     * @param offices office集合
     */
    public void updateTree(List<Device> offices){
        deviceDao.saveAll(offices);
    }

    /**
     * 获得设备信息
     * @param deviceCode code
     * @return 机构信息
     */
    public Device getAreaById(String deviceCode){
        return deviceDao.getAreaById(deviceCode);
    }

    /**
     * 停用设备
     * @param id office code
     */
    public Result disable(String id) {
        int count = deviceDao.getParentCodesCount(id);
        if(count>0){
            return new Result(true, StatusCode.ERROR, "该设备包含未停用的子设备");
        }
        Device device = deviceDao.getAreaById(id);
        device.setStatus("2");
        deviceDao.save(device);
        return new Result(true, StatusCode.OK, "停用设备"+device.getDeviceName()+"成功");
    }

    /**
     * 启用设备
     * @param id office code
     */
    public String enable(String id) {
        Device device = deviceDao.getAreaById(id);
        device.setStatus("0");
        deviceDao.save(device);
        return "启用设备"+device.getDeviceName()+"成功";
    }

    /**
     * 根据id查询设备
     */
    public Device selectById(String id) {
        return deviceDao.selectById(id);
    }

    /**
     * 根据机构id查询设备idList
     */
    public List<String> selectByOrgId(String orgId) {
        return deviceDao.selectByOrgId(orgId);
    }

    /**
     * 根据传感器类型名称查询设备信息
     */
    public List<Device> findByCgqTypeName(String cgqTypeName) {
        return deviceDao.findByCgqTypeName(cgqTypeName);
    }

    /**
     * 根据设施设备代码查询设备信息
     * @param deviceCode
     * @return
     */
    public Device selectByDeviceCode(String deviceCode) {
        return deviceDao.selectByDeviceCode(deviceCode);
    }

    /**
     * 根据指定信息查询设备信息列表
     * @param device
     * @return
     */
    public List<Device> findListByCondition(Device device) {
        return deviceDao.findListByCondition(device);
    }

    /**
     * 根据管理所ID查询人工测点设备列表
     * @param officeId
     * @return
     */
    public List<Device> findListByOfficeId(String officeId) {
        return deviceDao.findListByOfficeId(officeId);
    }

    /**
     * 查询具有人工测点的最顶级设备列表
     * @return
     */
    public List<Device> findIsHavePointTopList() {
        return deviceDao.findIsHavePointTopList();
    }

    /**
     * 返回所有传感器参数为否的设备信息
     * @return
     */
    public List<Device> selectAll() {
        return deviceDao.selectAll();
    }

    public List<Device> selectByOrgIdTo(String t,String ty,String kpName) {
        return deviceDao.selectByOrgIdTo(t,ty,kpName);
    }

    public Device selectByDeviceName(String id) {
        return deviceDao.selectByDeviceName(id);
    }
    public List<Device> treeDataD(String deviceCode,String userCode) {
        Result result = userClient.QueryBytreeDate(userCode);
        List<String> list = (List<String>)result.getData();
        List<Device> list1 = new ArrayList<>();
        if(list.size()>0){
            for(String ti:list){
                List<Device>  list2 = deviceDao.selectByOrgIdL(ti);
                list1.addAll(list2);
            }
            return list1;
        }else{
            return deviceDao.findAll((Specification<Device>) (root, query, cb) -> getPredicate(deviceCode, query, cb, root.get("parentCodes"), root.get("id"), root.get("status"), root.get("treeLevel"), root.get("treeSort"), root));
        }

    }
    public List<TreeData> treeDatasTo(String deviceCode, String userCode) {
        List<Device> devices = treeDataD(deviceCode,userCode);
        List<TreeData> treeDatas = ListUtils.newArrayList();
        ResultMap<Device> resultMap = new ResultMap<>();
        //分类id
        List<DerviceOffice> derviceOffices = derviceOfficeService.treeData(null);
        for (DerviceOffice derviceOffice: derviceOffices) {
            TreeData treeData = new TreeData(derviceOffice.getId(), derviceOffice.getParentCode(), derviceOffice.getOfficeName(), "设备分类", "1");
            treeDatas.add(treeData);
        }
        resultMap.setEntityList(devices);
        //最顶级设备
//        List<Device> devicesFirst = resultMap.resultLists(true,"parentCode", "0");
        for (Device a : devices) {
            TreeData treeData = new TreeData(a.getId(), a.getDeviceClass(), a.getDeviceName(), "设备", "0");
            treeDatas.add(treeData);
        }
        //移除devices对应所有devicesFirst
//        devices.removeAll(devicesFirst);
//        for (Device a : devices) {
//            TreeData treeData = new TreeData(a.getId(), a.getParentCode(), a.getDeviceName(), "设备", "0");
//            treeDatas.add(treeData);
//        }
        return treeDatas;
    }

    public List<Device> selectByOrgIdToX(String tocode,String kpName) {
        return deviceDao.selectByOrgIdToX(tocode,kpName);
    }

    public List<Device> selectByOrgIdB(String tocode, String type) {
        return deviceDao.selectByOrgIdB(tocode,type);
    }

    public List<Device> selectByOrgIdLO(String tocode) {
        return deviceDao.selectByOrgIdLO(tocode);
    }

    public List<Device> selectByOrgIdZo(String officeCode) {
        return deviceDao.selectByOrgIdZo(officeCode);
    }

    public Device getbyId(String deceiveId) {
        return deviceDao.getbyId(deceiveId);
    }

    public List<Device> selectByDevice() {
        return deviceDao.selectByDeviceTu();
    }

    public List<Device> selectByGco() {
        return deviceDao.selectByGco();
    }


    public List<Device> selectByOfficeCode(String officeCode, String deviceClassName) {
        return deviceDao.selectByOfficeCode(officeCode,deviceClassName);
    }
}
