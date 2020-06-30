package com.chinags.device.check.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;

import com.chinags.device.check.entity.Check;
import com.chinags.device.check.service.CheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("设备管理controller")
@RestController
@CrossOrigin
@RequestMapping("/check")
public class InspectionControler extends BaseController {
    @Autowired
    private CheckService checkService;



    /**
     * 分页获取设备信息
     *
     * @return
     */

    @ApiOperation("分页获取巡检信息")
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListPage(Check check) {
        PageResult<Check> page;
        try {
            //类似查询全部
            page = checkService.find(check);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }

    @ApiOperation("获取规范信息")
    @RequestMapping(value = "/selectCheck", method = RequestMethod.GET)
    @ResponseBody
    public Result selectCheck(String id) {
        Check check = null;
        try {
            //类似查询全部
            check = checkService.selectCheck(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", check);
    }


    @ApiOperation("保存设备")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Check check) {


        checkService.save(check);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除设备")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(Check check) {
        checkService.delete(check);
        return new Result(true, StatusCode.OK, "删除成功");


    }
    }
