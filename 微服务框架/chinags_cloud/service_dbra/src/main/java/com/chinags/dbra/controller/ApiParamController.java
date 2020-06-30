package com.chinags.dbra.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.dbra.entity.ApiParam;
import com.chinags.dbra.service.ApiParamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-8-27
 **/
@Api("接口参数controller")
@RestController
@CrossOrigin
@RequestMapping("/apiParam")
public class ApiParamController extends BaseController {

    @Autowired
    private ApiParamService apiParamService;

    @ApiOperation("保存接口参数信息")
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody ApiParam apiParam) {
        if (apiParam.getApiId().equals("0000")) {
            apiParam.setApiId(getLoginUser().getUsercode());
        }
        apiParamService.save(apiParam);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("根据类型和ApiId查询接口参数")
    @RequestMapping(value = "/find/{type}/{apiId}", method = RequestMethod.GET)
    public Result findByTypeAndApiId(@PathVariable("type") String type, @PathVariable("apiId") String apiId) {
        List<ApiParam> apiParams = apiParamService.find(type, apiId);
        return new Result(true, StatusCode.OK, "查询成功", apiParams);
    }

    @ApiOperation("根据apiId查询接口参数")
    @RequestMapping(value = "/find/{apiId}", method = RequestMethod.GET)
    public Result findByApiId(@PathVariable("apiId") String apiId) {
        if (apiId.equals("0000")) {
            apiId = getLoginUser().getUsercode();
        }
        List<ApiParam> apiParams = apiParamService.find(null, apiId);
        return new Result(true, StatusCode.OK, "查询成功", apiParams);
    }

    @ApiOperation("根据id查询接口参数")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result selectById(@PathVariable("id") String id) {
        ApiParam apiParam = apiParamService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功", apiParam);
    }

    @ApiOperation("根据id删除参数")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        apiParamService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @ApiOperation("根据apiId删除参数")
    @RequestMapping(value = "/delByApiId/{apiId}", method = RequestMethod.GET)
    public Result deleteByApiId(@PathVariable("apiId") String apiId) {
        if (apiId.equals("0000"))
            apiId = getLoginUser().getUsercode();
        apiParamService.deleteByApiId(apiId);
        return new Result(true, StatusCode.OK, "清理成功");
    }

}
