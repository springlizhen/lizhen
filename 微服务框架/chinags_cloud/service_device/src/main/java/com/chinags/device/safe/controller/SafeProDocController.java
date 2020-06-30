package com.chinags.device.safe.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.safe.client.FileClient;
import com.chinags.device.safe.entity.SafeProDoc;
import com.chinags.device.safe.service.SafeProDocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author XieWenqing
 * @date 2019/12/5 上午 10:54
 */

@Api("安全工程档案controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/safe")
public class SafeProDocController extends BaseController {
    @Autowired
    private SafeProDocService safeProDocService;
    @Autowired
    private FileClient fileClient;


    @ApiOperation("保存安全工程档案")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody SafeProDoc safeProDoc) {
        //获取登录用户
        String userid = getLoginUser().getUsercode();
//        String id = safeProDoc.getId();
        String createBy = safeProDoc.getCreateBy();
        if(StringUtils.isEmpty(createBy)){  //为空说明是新增操作
            safeProDoc.setCreateBy(userid);
            safeProDoc.setCreateDate(new Date());
        }
        safeProDoc.setUpdateBy(userid);
        safeProDoc.setUpdateDate(new Date());
        safeProDocService.save(safeProDoc);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("查询安全工程档案list")
    @RequestMapping(value ="/listData", method = RequestMethod.POST)
    public Result listData(@RequestBody SafeProDoc safeProDoc) {
        return new Result(true, StatusCode.OK, "",safeProDocService.listDate(safeProDoc));
    }

    @ApiOperation("根据id查询安全工程档案")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        SafeProDoc safeProDoc = safeProDocService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", safeProDoc);
    }

    @ApiOperation("根据id删除安全工程档案")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        //先删除所有关联的附件
        fileClient.deleteFileListByPid(id);
        safeProDocService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
