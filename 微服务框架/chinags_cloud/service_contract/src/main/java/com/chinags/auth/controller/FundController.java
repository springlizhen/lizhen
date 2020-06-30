package com.chinags.auth.controller;

import com.chinags.auth.entity.File;
import com.chinags.auth.entity.Fund;
import com.chinags.auth.service.FileService;
import com.chinags.auth.service.FundService;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api("文件管理controller")
@RestController
@CrossOrigin
@RequestMapping("/fund")
public class FundController extends BaseController {


    @Autowired
    private FundService fundService;

    @ApiOperation("创建合同")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody Fund fund) {
        return fundService.saveFund(fund);
    }

    @ApiOperation("条件查询")
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Result select(Fund fund) {
        PageResult<Fund> list=fundService.selectFile(fund);
        if(!StringUtils.isEmpty(list.getList())) {

            return new Result(true, StatusCode.OK, "查询成功", list);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", list);

        }
    }
    @ApiOperation("删除合同")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result deleteFundById(String uuid) {
         fundService.deleteFundById(uuid);
        return new Result(true, StatusCode.OK, "删除成功");


    }




}
