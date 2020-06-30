package com.chinags.device.plan.controller;

import com.alibaba.fastjson.JSONObject;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.Sensor;
import com.chinags.device.basic.service.SensorService;
import com.chinags.device.client.DicDataClient;
import com.chinags.device.client.UserClient;
import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.service.PlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api("计划管理controller")
@RestController
@CrossOrigin
@RequestMapping("/plan")
public class PlanController extends BaseController {

    @Autowired
    private PlanService planService;

    @Autowired
    private DicDataClient dicDataClient;
    @Autowired
    private UserClient userClient;

    /**
     * 获取计划信息
     * @return
     */
    @ApiOperation("获取计划信息")
    @RequestMapping(value = "/listDataCL", method = RequestMethod.GET)
    @ResponseBody
    public Result getAll(Plan plan){
        List<Plan> page;
        try {
            //类似查询全部
            String usercode = getLoginUser().getUsercode();
            if("system".equals(usercode)){
                plan = new Plan();
                //测试阶段 注释掉，将会展示所有
            }
            page = planService.findAll(plan);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",new PageResult(page));
    }

    /**
     * 获取计划信息
     * @return
     */
    @ApiOperation("获取计划信息")
    @RequestMapping(value = "/listDataCLSub", method = RequestMethod.GET)
    @ResponseBody
    public Result findSub(Plan plan){
        List<Plan> page;
        Result result = null;
        try {
            //类似查询全部
            String usercode = getLoginUser().getUsercode();
            if("system".equals(usercode)){
                String planid = plan.getPlanId();
                plan = new Plan();
                plan.setPlanId(planid);
                //测试阶段 注释掉，将会展示所有
            }
            result = userClient.toUser(usercode);
            List<String> deceiveIds = (List<String>) result.getData();
            page = planService.findSub(plan,usercode,deceiveIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",new PageResult(page));
    }

    /**
     * 分页获取计划信息
     * @return
     */
    @ApiOperation("分页获取计划信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody  public Result areaListPage(Plan plan){
        PageResult<Plan> page;
        Result result = null;
        try {
            //类似查询全部
            String usercode = getLoginUser().getUsercode();
            result = userClient.toUser(usercode);
            List<String> deceiveIds = (List<String>) result.getData();
            page = planService.find(plan,deceiveIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }
    /**
     *
     * 获取全部信息
     * @return
     */
    @ApiOperation("分页获取计划信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody  public Result findAll(Plan plan){
        List<Plan> list = new ArrayList<>();
        try {
            //类似查询全部
            list = planService.findAllTo(plan);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    /**
     * 获取计划信息
     * @return
     */
    @ApiOperation("获取计划信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String id){
        Plan plan = new Plan();
        try {
            if(StringUtils.isNotEmpty(id)) {
                plan = planService.getOne(id);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",plan);
    }

    @ApiOperation("保存计划")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Plan plan) {
        if(plan.getIsNewRecord()) {
            plan.setStatus("0");
            plan.setPlanReport("0");
            plan.setPlanPepName(getLoginUser().getUsername());
            Result office = dicDataClient.getUserOffice(getLoginUser().getUsercode());
            Map<String,String> data = (Map) office.getData();

            if(data!=null){
                plan.setPlanOffice(data.get("code"));
                plan.setPlanOfficeName(data.get("name"));
            }else{
                plan.setPlanOffice("1");
            }
        }else{
            Plan planParent = planService.getOne(plan.getId());
            plan.setStatus(planParent.getStatus());
        }
        plan.setCreateBy(getLoginUser().getUsercode());
        plan.setUpdateBy(getLoginUser().getUsercode());
        plan.setCreateDate(new Date());
        plan.setUpdateDate(new Date());
        this.planService.save(plan);
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(JSONObject.toJSONString(plan).getBytes());
        return new Result(true, StatusCode.OK, "保存成功!,"+ data);
    }

    @ApiOperation("删除计划")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String id) {
        if(StringUtils.isNotEmpty(id)){
            Plan plan = planService.delete(id);
            return new Result(true, StatusCode.OK, "删除计划“"+plan.getPlanName()+"”成功");
        }else {
            return new Result(true, StatusCode.ERROR, "删除计划失败");
        }
    }

    @ApiOperation("管理站审核状态修改")
    @RequestMapping(value = "/savePlanStatus", method = RequestMethod.GET)
    @ResponseBody
    public Result savePlanStatus(String id, String status) {
        if(StringUtils.isNotEmpty(id)){
           planService.savePlanStatus(id, status);
            return new Result(true, StatusCode.OK, "修改计划状态成功");
        }else {
            return new Result(true, StatusCode.ERROR, "修改计划失败");
        }
    }

    @ApiOperation("管理站上报状态修改")
    @RequestMapping(value = "/savePlanReport", method = RequestMethod.GET)
    @ResponseBody
    public Result savePlanReport(String id, String status) {
        if(StringUtils.isNotEmpty(id)){
            planService.savePlanReport(id, status);
            return new Result(true, StatusCode.OK, "修改计划状态成功");
        }else {
            return new Result(true, StatusCode.ERROR, "修改计划失败");
        }
    }

}
