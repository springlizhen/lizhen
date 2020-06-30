package com.chinags.device.threshold.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.threshold.entity.ThresholdVal;
import com.chinags.device.threshold.service.ThresholdValService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

/**
 * @author XieWenqing
 * @date 2019/12/10 上午 11:33
 */
@Api("工程安全阈值设置controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/threshold")
public class ThresholdValController extends BaseController {
    @Autowired
    private ThresholdValService thresholdValService;
    @Autowired
    private DeivceService deivceService;

    @ApiOperation("保存养护记录")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody ThresholdVal thresholdVal) {
        //做一个计数器，如果进行批量保存时，重复选中了设备，则更改这个数值，页面给与不同的提示
        int count = 0;
        //获取登录用户
        String userid = getLoginUser().getUsercode();
        String createBy = thresholdVal.getCreateBy();
        thresholdVal.setUpdateBy(userid);
        thresholdVal.setUpdateDate(new Date());
        if(StringUtils.isEmpty(createBy)){  //为空说明是新增操作
            thresholdVal.setCreateBy(userid);
            thresholdVal.setCreateDate(new Date());
            //有可能用户进行了多选，将设备id进行拆分
            String[] ids = thresholdVal.getDeceiveId().split(",");
            for(String deceiveId : ids){
                ThresholdVal t = thresholdValService.selectByDeceiveId(deceiveId);
                if(t != null){  //根据设备id能查询到记录，则表示重复添加，该条记录不予保存
                    count = 1;
                    continue;
                }
                //获取设备编码
                Device device = deivceService.selectById(deceiveId);
                //获取设备简称，没有的话赋值为空
                String shortName = device.getDeviceShortName();
                if(StringUtils.isNotEmpty(shortName)){
                    shortName += "/";
                }else{
                    shortName = "/";
                }
                thresholdVal.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                thresholdVal.setDeceiveId(deceiveId);
                thresholdVal.setDeceiveCode(shortName + device.getDeviceCode());
                thresholdValService.save(thresholdVal);
            }
        }else{  //进行修改操作，修改时只会选中其中一种，且修改后的数据，设备id不能重复
            //获取修改时选中的设备id
            String deceiveNewId = thresholdVal.getDeceiveId();
            String id = thresholdVal.getId();
            //获取数据库中还未修改的记录
            ThresholdVal t = thresholdValService.findById(id);
            //获取数据库中该条记录的旧的设备id
            String deceiveOldId = t.getDeceiveId();
            if(!deceiveNewId.equals(deceiveOldId)){  //如果当前两个设备id不相等，进行判断
                ThresholdVal tv = thresholdValService.selectByDeceiveId(deceiveNewId);
                if(tv != null){  //根据设备id能查询到记录，则表示重复添加，该条记录不予保存
                    return new Result(true, StatusCode.ERROR, "保存失败，该设备记录已存在");
                }
            }
            //如果设备不重复，重新获取设备并保存
            Device device = deivceService.selectById(deceiveNewId);
            //获取设备简称，没有的话赋值为空
            String shortName = device.getDeviceShortName();
            if(StringUtils.isNotEmpty(shortName)){
                shortName += "/";
            }else{
                shortName = "/";
            }
            thresholdVal.setDeceiveCode(shortName + device.getDeviceCode());
            thresholdValService.save(thresholdVal);
        }
        if(count != 0){
            return new Result(true, StatusCode.OK, "保存成功，部分重复设备记录未进行保存");
        }
        return new Result(true, StatusCode.OK, "保存成功");
    }


    @ApiOperation("根据id工程安全阈值设置记录")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        ThresholdVal thresholdVal = thresholdValService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", thresholdVal);
    }

    @ApiOperation("查询工程安全阈值设置list")
    @RequestMapping(value ="/listData", method = RequestMethod.POST)
    public Result listData(@RequestBody ThresholdVal thresholdVal) {
        return new Result(true, StatusCode.OK, "",thresholdValService.listDate(thresholdVal));
    }
    @ApiOperation("更改工程安全阈值使用状态")
    @RequestMapping(value = "/changeStatus/{id}/{status}", method = RequestMethod.GET)
    public Result changeStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
        //获取登录用户
        String userid = getLoginUser().getUsercode();
        thresholdValService.updateStatusById(id, status, userid, new Date());
        return new Result(true, StatusCode.OK, "更改成功");
    }

}
