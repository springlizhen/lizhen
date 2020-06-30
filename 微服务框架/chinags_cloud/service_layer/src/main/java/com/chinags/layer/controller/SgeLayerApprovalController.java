package com.chinags.layer.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.layer.entity.SgeLayerApproval;
import com.chinags.layer.service.SgeLayerApprovalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api("图层申请管理controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/sgeLayerApproval")
public class SgeLayerApprovalController extends BaseController {
    @Autowired
    private SgeLayerApprovalService sgeLayerApprovalService;

    /**
     * 分页获取图层申请信息
     * @return
     */
    @ApiOperation("分页获取图层申请信息")
    @RequestMapping(value = "/findList", method = RequestMethod.POST)
    @ResponseBody
    public Result findList(SgeLayerApproval sgeLayerApproval) {
        PageResult<SgeLayerApproval> page;
        try {
            //类似查询全部
            page = sgeLayerApprovalService.findList(sgeLayerApproval);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }

    @ApiOperation("获取图层申请信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    @ResponseBody
    public Result form(String id) {
        SgeLayerApproval sgeLayerApproval = sgeLayerApprovalService.getSgeLayer(id);
        return new Result(true, StatusCode.OK, "获取成功", sgeLayerApproval);
    }

    @ApiOperation("保存申请")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody SgeLayerApproval sgeLayerApproval) {
        String usercode = getLoginUser().getUsercode();
        if(sgeLayerApproval.getPepName()==null) {
            sgeLayerApproval.setPepName(usercode);
        }
        sgeLayerApproval.setCreateBy(getLoginUser().getUsercode());
        sgeLayerApproval.setUpdateBy(getLoginUser().getUsercode());
        sgeLayerApproval.setCreateDate(new Date());
        sgeLayerApproval.setUpdateDate(new Date());
        sgeLayerApprovalService.save(sgeLayerApproval);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除申请")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(SgeLayerApproval sgeLayerApproval) {
        sgeLayerApprovalService.delete(sgeLayerApproval);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
