package com.chinags.device.schedule.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.device.schedule.client.FileUploadClient;
import com.chinags.device.schedule.entity.Schedule;
import com.chinags.device.schedule.service.ScheduleCommService;
import com.chinags.device.schedule.service.ScheduleProjectService;
import com.chinags.device.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * 进度管理接口
 * @Author : Mark_wang
 * @Date : 2019-9-10
 **/
@Api("进度管理controller")
@RestController
@CrossOrigin
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleProjectService scheduleProjectService;

    @Autowired
    private ScheduleCommService scheduleCommService;

    @Autowired
    private FileUploadClient fileUploadClient;

    @ApiOperation("分页查询工程进度列表")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ResponseBody
    public Result findByPage(@RequestBody Schedule schedule) {
        PageResult<Schedule> page = scheduleService.findByPage(schedule);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有进度信息，请配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("根据id删除进度管理")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        // 删除进度管理下的项目信息
        scheduleProjectService.deleteByScheduleId(id);
        // 删除进度管理下的验收信息
        scheduleCommService.deleteByScheduleId(id);
        // 删除进度管理信息
        scheduleService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @ApiOperation("根据id获取工程进度")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        Schedule schedule = scheduleService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功", schedule);
    }

    @ApiOperation("保存进度管理")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody Schedule schedule) {
        boolean isNew = false;
        String userCode = getLoginUser().getUsercode();
        if (StringUtils.isEmpty(schedule.getCreateBy())) {
            schedule.setCreateBy(userCode);
            schedule.setCreateDate(new Date());
            isNew = true;
        }
        schedule.setUpdateBy(userCode);
        schedule.setUpdateDate(new Date());
        scheduleService.save(schedule);
        if (isNew) {
            // 为新增进度时建立的项目增加进度id
            scheduleProjectService.updateScheduleIdByCreateBy(schedule.getId(), userCode);
            // 为新增进度时上传的附件修改进度id
            fileUploadClient.updatePid(schedule.getId(), "schedule");
        }
        // 判断是否有已完成的项目
        Integer num = scheduleProjectService.getEndMoneypRrojectsByScheduleId(schedule.getId());
        // 如果存在已完成的项目则完成额度需要根据已完成的项目统计，如果没有则已完成额度为0
        if (num != 0) {
            Double endMoney = scheduleProjectService.getEndMoneyByScheduleId(schedule.getId());
            schedule.setEndMoney(endMoney);
        } else {
            schedule.setEndMoney(0.0);
        }
        scheduleService.save(schedule);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("查询所有进度信息")
    @RequestMapping(value = "/find/year", method = RequestMethod.GET)
    public Result findYears() {
        List<String> years = scheduleService.findAllYear();
        return new Result(true, StatusCode.OK, "查询成功", years);
    }

}
