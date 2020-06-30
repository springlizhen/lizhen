package com.chinags.device.basic.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.Sensor;
import com.chinags.device.basic.service.SensorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api("传感器管理controller")
@RestController
@CrossOrigin
@RequestMapping("/sensor")
public class SensorController extends BaseController {

    @Autowired
    private SensorService sensorService;

    /**
     * 分页获取传感器信息
     * @return
     */
    @ApiOperation("分页获取传感器信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(Sensor sensor){
        PageResult<Sensor> page;
        try {
            //类似查询全部
            page = sensorService.find(sensor);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取代码字段信息
     * @return
     */
    @ApiOperation("获取传感器信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String id){
        Sensor field = new Sensor();
        try {
            if(StringUtils.isNotEmpty(id)) {
                field = sensorService.getSensorById(id);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",field);
    }

    @ApiOperation("保存传感器")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Sensor sensor) {
        if(sensor.getIsNewRecord()) {
            sensor.setStatus("0");
        }else{
            Sensor fieldParent = sensorService.getSensorById(sensor.getId());
            sensor.setStatus(fieldParent.getStatus());
        }
        sensor.setCreateBy(getLoginUser().getUsercode());
        sensor.setUpdateBy(getLoginUser().getUsercode());
        sensor.setCreateDate(new Date());
        sensor.setUpdateDate(new Date());
        this.sensorService.save(sensor);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除代传感器")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String id) {
        if(StringUtils.isNotEmpty(id)){
            Sensor field = sensorService.delete(id);
            return new Result(true, StatusCode.OK, "删除传感器“"+field.getSensorName()+"”成功");
        }else {
            return new Result(true, StatusCode.ERROR, "删除传感器失败");
        }
    }

}
