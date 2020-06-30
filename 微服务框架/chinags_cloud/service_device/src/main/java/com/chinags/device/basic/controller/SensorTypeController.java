package com.chinags.device.basic.controller;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.basic.entity.SensorType;
import com.chinags.device.basic.service.SensorService;
import com.chinags.device.basic.service.SensorTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Api("传感器类型管理controller")
@RestController
@CrossOrigin
@RequestMapping("/sensorType")
public class SensorTypeController extends BaseController {

    @Autowired
    private SensorTypeService sensorTypeService;

    /**
     * 分页获取传感器类型信息
     * @return
     */
    @ApiOperation("分页获取传感器类型信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(SensorType sensorType){
        PageResult<SensorType> page;
        try {
            String parentCode = sensorType.getParentCode()==null?"0":sensorType.getParentCode();
            sensorType.setParentCode(parentCode);
            //类似查询全部
            sensorType.setPageSize(100000000);
            page = sensorTypeService.find(sensorType);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取字典信息
     * @return
     */
    @ApiOperation("获取字典信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String id, String parentCode){
        SensorType sensorType = new SensorType();
        try {
            if(StringUtils.isNotEmpty(id)){
                sensorType = sensorTypeService.getById(id);
            }else if(StringUtils.isNotEmpty(parentCode)){
                SensorType sensorTypeParent = sensorTypeService.getById(parentCode);
                sensorType.setParentCode(parentCode);
                sensorType.setTreeNames(sensorTypeParent.getName()+"/"+sensorTypeParent.getName()+"/"+sensorTypeParent.getName());
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",sensorType);
    }

    @ApiOperation("保存类型")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody SensorType sensorType) {
        SensorType dicDataParent = sensorTypeService.getById(sensorType.getId());
        if(dicDataParent!=null) {
            sensorType.setStatus(dicDataParent.getStatus());
        }else{
            sensorType.setStatus("0");
        }
        sensorType.setCreateBy(getLoginUser().getUsercode());
        sensorType.setUpdateBy(getLoginUser().getUsercode());
        sensorType.setCreateDate(new Date());
        sensorType.setUpdateDate(new Date());
//        sensorType.setId(UUID.randomUUID().toString());
        this.sensorTypeService.save(sensorType);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除类型")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(SensorType sensorType) {
        if(sensorTypeService.delete(sensorType)) {
            return new Result(true, StatusCode.OK, "删除成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "请先删除子菜单");
        }
    }

    /**
     * 获取sensorType信息
     * @return
     */
    @ApiOperation("获取sensorType信息")
    @RequestMapping(value = "/dicType", method = RequestMethod.GET)
    public Result dicType(String code){
        SensorType sensorType = null;
        try {
            if(StringUtils.isNotEmpty(code)){
                sensorType = sensorTypeService.getByCode(code);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",sensorType);
    }

    /**
     * 菜单树结构
     * @return
     */
    @ApiOperation("分类树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String excludeCode){
        List list = ListUtils.newArrayList();
        List<SensorType> offices;
        try {
            offices = sensorTypeService.treeData(excludeCode);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (SensorType a : offices) {
            HashMap map = MapUtils.newHashMap();
            map.put("id", a.getId());
            map.put("pId", a.getParentCode());
            map.put("name", a.getName());
            map.put("title", a.getName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }
}
