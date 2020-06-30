package com.chinags.device.maintain.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.maintain.entity.MaintainInfo;
import com.chinags.device.maintain.service.MaintainInfoService;
import com.chinags.device.preserve.entity.Preserve;
import com.chinags.device.preserve.service.PreserveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author XieWenqing
 * @date 2019/12/6 下午 4:44
 */
@Api("养护记录controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/maintain")
public class MaintainInfoController extends BaseController {
    @Autowired
    private MaintainInfoService maintainInfoService;
    @Autowired
    private PreserveService preserveService;
    @Autowired
    private DeivceService deivceService;

    @ApiOperation("保存养护记录")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody MaintainInfo maintainInfo) {
        //获取登录用户
        String userid = getLoginUser().getUsercode();
        String createBy = maintainInfo.getCreateBy();
        if(StringUtils.isEmpty(createBy)){  //为空说明是新增操作
            maintainInfo.setCreateBy(userid);
            maintainInfo.setCreateDate(new Date());
            Device device = deivceService.selectById(maintainInfo.getDeceiveId());
            maintainInfo.setDeceiveName(device.getDeviceName());
        }else{
            if(maintainInfo.getOrgId().equals(maintainInfo.getDeceiveId())){
                maintainInfo.setDeceiveId(maintainInfo.getDeceiveName());
                Device device = deivceService.selectById(maintainInfo.getDeceiveName());
                maintainInfo.setDeceiveName(device.getDeviceName());
            }else{
                Device device = deivceService.selectById(maintainInfo.getDeceiveId());
                maintainInfo.setDeceiveName(device.getDeviceName());
            }

        }
        //获取设备名称

        maintainInfo.setUpdateBy(userid);
        maintainInfo.setUpdateDate(new Date());
        maintainInfoService.save(maintainInfo);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("查询养护记录list")
    @RequestMapping(value ="/listData", method = RequestMethod.POST)
    public Result listData(@RequestBody MaintainInfo maintainInfo) {
        return new Result(true, StatusCode.OK, "",maintainInfoService.listDate(maintainInfo));
    }
    @ApiOperation("查询维护记录")
    @RequestMapping(value ="/listDataTo", method = RequestMethod.POST)
    public Result listDataTo() {
        List<MaintainInfo> list = maintainInfoService.listDataToK();
        for ( int i = 0 ; i < list.size() - 1 ; i ++ ) {
            for ( int j = list.size() - 1 ; j > i; j -- ) {
                if (list.get(j).getDeceiveId().equals(list.get(i).getDeceiveId())) {
                    list.remove(j);
                }
            }
        }
        for(MaintainInfo ti:list){
            String id =ti.getDeceiveId();
            MaintainInfo maintainInfo = new MaintainInfo();
            maintainInfo.setDeceiveId(id);
            Integer count = maintainInfoService.listCount(id);
            ti.setCount(count);

        }
        return new Result(true, StatusCode.OK, "",list);
    }
    @ApiOperation("查询维护记录")
    @RequestMapping(value ="/listDateToK", method = RequestMethod.POST)
    public Result listDateToK() {
        List<MaintainInfo> list = maintainInfoService.listDataTo();
        List<MaintainInfo> list1 = new ArrayList<>();
        if(list.size()>0 && null!=list){
            for(MaintainInfo ti:list){
                if(null != ti.getCountNum()){
                    list1.add(ti);
                }
            }
        }
        return new Result(true, StatusCode.OK, "",list1);
    }
    @ApiOperation("保存维护记录")
    @RequestMapping(value = "/saveTo", method = RequestMethod.POST)
    @Transactional
    public Result saveTo(@RequestBody Preserve preserve) {
        preserveService.save(preserve);
        String id = preserve.getMaintainId();
        Integer count = Integer.parseInt(preserve.getCountNum());
        maintainInfoService.updateBycount(id,count+1);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("根据id查询养护记录")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        MaintainInfo maintainInfo = maintainInfoService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", maintainInfo);
    }

    @ApiOperation("根据id删除养护记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        maintainInfoService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }


}
