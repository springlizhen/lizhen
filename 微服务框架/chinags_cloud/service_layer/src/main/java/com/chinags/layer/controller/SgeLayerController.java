package com.chinags.layer.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.layer.entity.SgeLayer;
import com.chinags.layer.service.SgeLayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api("图层管理controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/sgeLayer")
public class SgeLayerController extends BaseController {
    @Autowired
    private SgeLayerService sgeLayerService;

    /**
     * 分页获取图层信息
     *
     * @return
     */
    @ApiOperation("分页获取图层信息")
    @RequestMapping(value = "/findList", method = RequestMethod.GET)
    @ResponseBody
    public Result findList(SgeLayer sgeLayer) {
        PageResult<SgeLayer> page;
        try {
            //类似查询全部
            page = sgeLayerService.findList(sgeLayer);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }

    @ApiOperation("获取图层信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    @ResponseBody
    public Result form(String id) {
        SgeLayer sgeLayer = sgeLayerService.getSgeLayer(id);
        return new Result(true, StatusCode.OK, "获取成功", sgeLayer);
    }

    @ApiOperation("保存图层")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody SgeLayer sgeLayer) {
        sgeLayer.setCreateBy(getLoginUser().getUsercode());
        sgeLayer.setUpdateBy(getLoginUser().getUsercode());
        sgeLayer.setCreateDate(new Date());
        sgeLayer.setUpdateDate(new Date());
        sgeLayerService.save(sgeLayer);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除图层")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(SgeLayer sgeLayer) {
        sgeLayerService.delete(sgeLayer);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
