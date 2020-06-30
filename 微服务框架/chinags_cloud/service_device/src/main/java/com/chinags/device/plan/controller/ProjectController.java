package com.chinags.device.plan.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.entity.Project;
import com.chinags.device.plan.service.PlanService;
import com.chinags.device.plan.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api("项目管理controller")
@RestController
@CrossOrigin
@RequestMapping("/project")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    /**
     * 分页获取项目信息
     * @return
     */
    @ApiOperation("分页获取项目信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(String enginneringId){
        PageResult<Project> page;
        try {
            //类似查询全部
            page = projectService.findAll(enginneringId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取项目信息
     * @return
     */
    @ApiOperation("获取项目信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String id){
        Project project = new Project();
        try {
            if(StringUtils.isNotEmpty(id)) {
                project = projectService.getOne(id);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",project);
    }

    @ApiOperation("保存项目")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Project project) {
        if(project.getIsNewRecord()) {
            project.setStatus("0");
        }else{
            Project pro = projectService.getOne(project.getId());
            project.setStatus(pro.getStatus());
        }
        project.setCreateBy(getLoginUser().getUsercode());
        project.setUpdateBy(getLoginUser().getUsercode());
        project.setCreateDate(new Date());
        project.setUpdateDate(new Date());
        this.projectService.save(project);
        return new Result(true, StatusCode.OK, "保存成功", project.getId());
    }

    @ApiOperation("删除项目")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String id) {
        if(StringUtils.isNotEmpty(id)){
            projectService.delete(id);
            return new Result(true, StatusCode.OK, "删除项目成功");
        }else {
            return new Result(true, StatusCode.ERROR, "删除项目失败");
        }
    }

}
