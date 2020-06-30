package com.chinags.device.preserve.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.maintain.entity.MaintainInfo;
import com.chinags.device.maintain.service.MaintainInfoService;
import com.chinags.device.preserve.entity.Preserve;
import com.chinags.device.preserve.service.PreserveService;
import com.chinags.device.schedule.entity.ScheduleComm;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api("维护记录controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/preserve")
public class PreserveController extends BaseController {
    @Autowired
    private  PreserveService preserveService;
    @Autowired
    private MaintainInfoService maintainInfoService;
    @ApiOperation("根据养护记录id查询维护记录")
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Result findById(@RequestBody Preserve preserve) {

        return new Result(true, StatusCode.OK, "",preserveService.listData(preserve));
    }
    @ApiOperation("根据进度id查询进度下的所有验收信息")
    @RequestMapping(value = "/findByScheduleId", method = RequestMethod.POST)
    public Result findByScheduleId(@RequestBody Preserve preserve) {
        List<Preserve> list = preserveService.findByScheduleId(preserve.getMaintainId());
        if (null == list && list.size() ==0) {
            return new Result(false, StatusCode.ERROR, "没有验收信息，请增加");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", list);
        }
    }

    @ApiOperation("查询数量")
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public Result count() {
       List<Preserve>  list= preserveService.selectCount();
        List<Preserve> list1 = new ArrayList<>();
//        if(list.size()>0 && null!= list){
//            for(Preserve ti:list){
//                String id = ti.getMaintainId();
//                MaintainInfo maintainInfo = maintainInfoService.selectBymaitainId(id);
//                ti.setToname(maintainInfo.getDeceiveName());
//                list1.add(ti);
//            }
//        }


        return new Result(true, StatusCode.OK, "查询成功",list1);
    }

}
