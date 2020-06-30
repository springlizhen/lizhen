package com.chinags.device.schedule.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.device.schedule.client.FileUploadClient;
import com.chinags.device.schedule.entity.ScheduleComm;
import com.chinags.device.schedule.service.ScheduleCommService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 进度管理验收接口
 * @Author : Mark_wang
 * @Date : 2019-9-10
 **/
@Api("进度管理验收controller")
@RestController
@CrossOrigin
@RequestMapping("/scheduleComm")
public class ScheduleCommController extends BaseController {
    @Autowired
    private ScheduleCommService scheduleCommService;

    @Autowired
    private FileUploadClient fileUploadClient;

    @ApiOperation("根据进度id查询进度下的所有验收信息")
    @RequestMapping(value = "/findByScheduleId/{scheduleId}", method = RequestMethod.GET)
    public Result findByScheduleId(@PathVariable("scheduleId") String scheduleId) {
        List<ScheduleComm> scheduleComms = scheduleCommService.findByScheduleId(scheduleId);
        if (CollectionUtils.isEmpty(scheduleComms)) {
            return new Result(false, StatusCode.ERROR, "没有验收信息，请增加");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", scheduleComms);
        }
    }

    @ApiOperation("保存验收信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody ScheduleComm scheduleComm) {
        String userCode = getLoginUser().getUsercode();
        boolean isNew = false;
        if (StringUtils.isEmpty(scheduleComm.getCreateBy())) {
            scheduleComm.setCreateBy(userCode);
            scheduleComm.setCreateDate(new Date());
            isNew = true;
        }
        scheduleCommService.save(scheduleComm);
        if (isNew) {
            // 为新增验收时上传的附件默认的pid修改pid
            fileUploadClient.updatePid(scheduleComm.getId(), "scheduleComm");
        }
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("保存验收信息")
    @RequestMapping(value = "/saves", method = RequestMethod.POST)
    public Result saves(@RequestBody List<ScheduleComm> scheduleComms) {
        scheduleCommService.saves(scheduleComms);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("验收信息归档，修改验收信息状态不可再编辑")
    @RequestMapping(value = "/upStatusByScheduleId/{scheduleId}", method = RequestMethod.GET)
    public Result updateStatusByScheduleId(@PathVariable("scheduleId") String scheduleId) {
        scheduleCommService.updateStatusByScheduleId(scheduleId);
        return new Result(true, StatusCode.OK, "归档成功");
    }

    @ApiOperation("根据id删除验收信息")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        scheduleCommService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @ApiOperation("根据进程id删除进程下的所有验收信息")
    @RequestMapping(value = "/delete/{scheduleId}", method = RequestMethod.GET)
    public Result deleteByScheduleId(@PathVariable("scheduleId") String scheduleId) {
        scheduleCommService.deleteByScheduleId(scheduleId);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
