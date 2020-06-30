package com.chinags.device.plan.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.client.DicDataClient;
import com.chinags.device.client.UserClient;
import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.entity.SubCentersPlan;
import com.chinags.device.plan.entity.TableData;
import com.chinags.device.plan.service.PlanService;
import com.chinags.device.plan.service.SubCentersPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("分中心计划管理controller")
@RestController
@CrossOrigin
@RequestMapping("/subCentersPlan")
public class SubCentersPlanController extends BaseController {

    @Autowired
    private SubCentersPlanService subCentersPlanService;

    @Autowired
    private DicDataClient dicDataClient;
    @Autowired
    private UserClient userClient;
    /**
     * 分页获取计划信息
     * @return
     */
    @ApiOperation("分页获取计划信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(SubCentersPlan subCentersPlan){
        PageResult<SubCentersPlan> page;
        Result result = null;
        try {
            //类似查询全部
            String usercode = getLoginUser().getUsercode();
            result = userClient.toUser(usercode);
            List<String> deceiveIds = (List<String>) result.getData();
            page = subCentersPlanService.find(subCentersPlan,deceiveIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 分页获取计划信息
     * @return
     */
    @ApiOperation("分页获取计划信息")
    @RequestMapping(value = "/listDataCL", method = RequestMethod.GET)
    @ResponseBody
    public Result listDataCL(String planId, String orgId){
        PageResult<TableData> page;
        try {
            //类似查询全部
            page = subCentersPlanService.findCL(planId, orgId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取计划信息
     * @return
     */
    @ApiOperation("获取计划信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String id){
        SubCentersPlan subCentersPlan = new SubCentersPlan();
        try {
            if(StringUtils.isNotEmpty(id)) {
                subCentersPlan = subCentersPlanService.getOne(id);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",subCentersPlan);
    }
    @ApiOperation("保存计划")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody SubCentersPlan subCentersPlan) {
        if(subCentersPlan.getIsNewRecord()) {
            subCentersPlan.setPlanReport("0");
            subCentersPlan.setStatus("0");
            subCentersPlan.setPlanPepName(getLoginUser().getUsername());
            Result office = dicDataClient.getUserOffice(getLoginUser().getUsercode());
            Map<String,String> data = (Map) office.getData();
            if(data!=null){
                subCentersPlan.setPlanOffice(data.get("code"));
                subCentersPlan.setPlanOfficeName(data.get("name"));
            }else{
                subCentersPlan.setPlanOffice("1");
            }
        }else{
            SubCentersPlan SubCentersPlanParent = subCentersPlanService.getOne(subCentersPlan.getId());
            subCentersPlan.setStatus(SubCentersPlanParent.getStatus());
        }
        subCentersPlan.setCreateBy(getLoginUser().getUsercode());
        subCentersPlan.setUpdateBy(getLoginUser().getUsercode());
        subCentersPlan.setCreateDate(new Date());
        subCentersPlan.setUpdateDate(new Date());
        this.subCentersPlanService.save(subCentersPlan);
        return new Result(true, StatusCode.OK, "保存成功!");
    }
    @ApiOperation("起草处室")
    @RequestMapping(value = "/sdd/{userName}", method = RequestMethod.GET)
    @ResponseBody
    public Result sdd(@PathVariable("userName") String userName) {
        Result result = null;
        result = userClient.sdd(userName);
        List<String> tk = (List<String>) result.getData();
        return new Result(true, StatusCode.OK,"获取成功",tk.get(0));
    }

    @ApiOperation("删除计划")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String id) {
        if(StringUtils.isNotEmpty(id)){
            SubCentersPlan subCentersPlan = subCentersPlanService.delete(id);
            return new Result(true, StatusCode.OK, "删除计划“"+subCentersPlan.getPlanName()+"”成功");
        }else {
            return new Result(true, StatusCode.ERROR, "删除计划失败");
        }
    }

    @ApiOperation("分局审核状态修改")
    @RequestMapping(value = "/savePlanStatus", method = RequestMethod.GET)
    @ResponseBody
    public Result savePlanStatus(String id, String status) {
        if(StringUtils.isNotEmpty(id)){
            subCentersPlanService.savePlanStatus(id, status);
            return new Result(true, StatusCode.OK, "修改计划状态成功");
        }else {
            return new Result(true, StatusCode.ERROR, "修改计划失败");
        }
    }

    @ApiOperation("分局上报状态修改")
    @RequestMapping(value = "/savePlanReport", method = RequestMethod.GET)
    @ResponseBody
    public Result savePlanReport(String id, String status) {
        if(StringUtils.isNotEmpty(id)){
            subCentersPlanService.savePlanReport(id, status);
            return new Result(true, StatusCode.OK, "修改计划状态成功");
        }else {
            return new Result(true, StatusCode.ERROR, "修改计划失败");
        }
    }
  
}
