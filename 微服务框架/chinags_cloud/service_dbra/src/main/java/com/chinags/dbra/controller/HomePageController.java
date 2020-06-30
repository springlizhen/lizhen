package com.chinags.dbra.controller;

import com.alibaba.fastjson.JSON;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.service.HomePageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

/**
 * 胶东调水数据交换平台首页Controller
 * @Author : Mark_Wang
 * @Date : 2020/3/19
 **/
@Api("数据交换首页controller")
@RestController
@CrossOrigin
@RequestMapping("/page")
public class HomePageController {
    @Autowired
    private HomePageService homePageService;

    @ApiOperation("当日数据条目数")
    @RequestMapping(value = "/daySum", method = RequestMethod.GET)
    public Result daySum() {
        return new Result(true, StatusCode.OK, "查询成功", homePageService.daySum());
    }

    @ApiOperation("数据交换类型比例")
    @RequestMapping(value = "/tybt", method = RequestMethod.GET)
    public Result dataType() {
        List<Integer> types = homePageService.dataType();
        String result = "[{value: "+ types.get(0) +", name: '水文'}, {value: "+ types.get(1) +", name: '水质'}, {value: "+ types.get(2) +", name: '自动化'}]";
        return new Result(true, StatusCode.OK, "查询成功", JSON.parseArray(result));
    }

    @ApiOperation("自动化数据清洗比例柱状图")
    @RequestMapping(value = "/zdhsjzt", method = RequestMethod.GET)
    public Result zdhsjZtData() {
        return new Result(true, StatusCode.OK, "查询成功", homePageService.zdhsjZtData());
    }

    @ApiOperation("当日小时数据统计")
    @RequestMapping(value = "/dayZD", method = RequestMethod.GET)
    public Result dayZD() {
        List<List<Integer>> lists = new ArrayList<>();
        lists.add(homePageService.dayZDH());
        lists.add(homePageService.daySZ());
        lists.add(homePageService.daySW());
        return new Result(true, StatusCode.OK, "查询成功", lists);
    }

    @ApiOperation("每日水情交换分类统计")
    @RequestMapping(value = "/daySq", method = RequestMethod.GET)
    public Result daySq() {
        return new Result(true, StatusCode.OK, "查询成功", homePageService.daySq());
    }

    @ApiOperation("当日数据交换条目")
    @RequestMapping(value = "/dayData", method = RequestMethod.GET)
    public Result getDayData() {
        return new Result(true, StatusCode.OK, "查询成功", homePageService.getDayData());
    }

    @ApiOperation("预警数据交换条目")
    @RequestMapping(value = "/yjData", method = RequestMethod.GET)
    public Result getYjData() {
        return new Result(true, StatusCode.OK, "查询成功", homePageService.getDayDataError());
    }

    @ApiOperation("清洗异常数据top")
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public Result getTopError() {
        return new Result(true, StatusCode.OK, "查询成功", homePageService.getTopError());
    }

}
