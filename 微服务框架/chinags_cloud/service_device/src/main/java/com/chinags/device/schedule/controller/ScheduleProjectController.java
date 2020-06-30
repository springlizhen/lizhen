package com.chinags.device.schedule.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.device.schedule.client.FileUploadClient;
import com.chinags.device.schedule.entity.ScheduleProject;
import com.chinags.device.schedule.service.ScheduleProjectService;
import com.chinags.device.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * 进度管理项目接口
 * @Author : Mark_wang
 * @Date : 2019-9-10
 **/
@Api("进度管理项目controller")
@RestController
@CrossOrigin
@RequestMapping("/schedulePro")
public class ScheduleProjectController extends BaseController {

    @Autowired
    private ScheduleProjectService scheduleProjectService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private FileUploadClient fileUploadClient;

    @ApiOperation("根据进度id查询进度管理下的所有项目信息")
    @RequestMapping(value = "/findByScheduleId/{scheduleId}", method = RequestMethod.GET)
    public Result findByScheduleId(@PathVariable("scheduleId") String scheduleId) {
        // 新进度添加项目，把用户名作为进度id，后续保存进度的时候再变成进度id
        if (scheduleId.equals("0000"))
            scheduleId = getLoginUser().getUsercode();
        List<ScheduleProject> scheduleProjects = scheduleProjectService.findByScheduleId(scheduleId);
        return new Result(true, StatusCode.OK, "查询成功", scheduleProjects);
    }

    @ApiOperation("保存进度项目信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody ScheduleProject scheduleProject) {
        String userCode = getLoginUser().getUsercode();
        boolean isNew = false;
        if (StringUtils.isEmpty(scheduleProject.getCreateBy())) {
            scheduleProject.setCreateBy(userCode);
            scheduleProject.setCreateDate(new Date());
            isNew = true;
            if (StringUtils.isEmpty(scheduleProject.getScheduleId()))
                scheduleProject.setScheduleId(userCode);
        }
        scheduleProject.setUpdateBy(userCode);
        scheduleProject.setUpdateDate(new Date());
        // 对进度中完成金额的修改
        // 新增项目就是已完成的项目
        if (StringUtils.isEmpty(scheduleProject.getId()) && scheduleProject.getStatus().equals("已完成")) {
            // 不是在新建进度中加入项目，需要把项目金额加入到进度完成金额中，如果是新进度在进度保存时统计完成金额
            if (!scheduleProject.getScheduleId().equals(userCode))
                scheduleService.addEndMoneyById(scheduleProject.getPlanMoney(), scheduleProject.getScheduleId());
        } else if (!StringUtils.isEmpty(scheduleProject.getId())){
            ScheduleProject oldProject = scheduleProjectService.selectById(scheduleProject.getId());
            if (oldProject.getStatus().equals("已完成")) {
                // 修改项目从已完成修改到未完成需要从完成金额中减去本项目的金额
                if (!scheduleProject.getStatus().equals("已完成"))
                    scheduleService.redEndMoneyById(oldProject.getPlanMoney(), oldProject.getScheduleId());
                else if (scheduleProject.getPlanMoney() != oldProject.getPlanMoney()) {
                    // 修改项目金额项目进度还是已完成，需要在进度完成金额中去掉本项目的旧金额，然后加入新金额
                    scheduleService.redEndMoneyById(oldProject.getPlanMoney(), oldProject.getScheduleId());
                    scheduleService.addEndMoneyById(scheduleProject.getPlanMoney(), scheduleProject.getScheduleId());
                }
            } else {
                // 项目进度从未完成变为已完成，需要在进度完成金额中加入本项目的金额
                if (scheduleProject.getStatus().equals("已完成"))
                    scheduleService.addEndMoneyById(scheduleProject.getPlanMoney(), scheduleProject.getScheduleId());
            }
        }
        scheduleProjectService.save(scheduleProject);
        if (isNew) {
            // 为新增项目是上传的默认pid的附件修改pid
            fileUploadClient.updatePid(scheduleProject.getId(), "schedulePro");
        }
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("根据id删除进度项目信息")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        ScheduleProject oldProject = scheduleProjectService.selectById(id);
        // 删除项目时需要确定项目是否是已完成状态，若是则在进度完成金额中把该项目的金额减去
        if (oldProject.getStatus().equals("已完成")) {
            scheduleService.redEndMoneyById(oldProject.getPlanMoney(), oldProject.getScheduleId());
        }
        scheduleProjectService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @ApiOperation("根据id查询进度项目信息")
    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        ScheduleProject scheduleProject = scheduleProjectService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功", scheduleProject);
    }
    @ApiOperation("根据登录用户清理用户之前添加但没有保存进度的项目信息")
    @RequestMapping(value = "/deleteOldByCreateBy", method = RequestMethod.GET)
    public Result deleteByCreateBy() {
        String userCode = getLoginUser().getUsercode();
        scheduleProjectService.deleteOldBycreateBy(userCode);
        return new Result(true, StatusCode.OK, "清理完成");
    }

}
