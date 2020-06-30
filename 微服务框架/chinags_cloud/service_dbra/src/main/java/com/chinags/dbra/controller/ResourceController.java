package com.chinags.dbra.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.Resource;
import com.chinags.dbra.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Mark_Wang
 * @date 2019/7/17
 **/
@Api("资源目录管理controller")
@RestController
@CrossOrigin
@RequestMapping("/resource")
public class ResourceController extends BaseController {

    @Autowired
    private ResourceService resourceService;

    @ApiOperation("分页查询资源目录")
    @RequestMapping(value = "/findByPage", method = RequestMethod.POST)
    public Result findByPage(@RequestBody BaseEntity entity) {
        PageResult<Resource> page = resourceService.findByPage(entity);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有注册的资源目录");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("保存数据资源")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody Resource res) {
        String userCode = getLoginUser().getUsercode();
        if (StringUtils.isEmpty(res.getCreateBy())) {
            res.setCreateBy(userCode);
            res.setCreateDate(new Date());
        }
        res.setUpdateBy(userCode);
        res.setUpdateDate(new Date());
        Resource resource = resourceService.save(res);
        if (resource != null) {
            return new Result(true, StatusCode.OK, "保存成功");
        } else {
            return new Result(false, StatusCode.ERROR, "保存失败");
        }
    }

    @ApiOperation("根据id查询数据资源目录后端显示")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        Resource resource = resourceService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", resource);
    }

    @ApiOperation("根据id查询数据资源目录前端显示")
    @RequestMapping(value = "/selectById/{id}", method = RequestMethod.GET)
    public Result selectById(@PathVariable("id") String id) {
        Resource resource = resourceService.findById(id);
        resourceService.updateCatNumById(id);
        return new Result(true, StatusCode.OK, "查询成功", resource);
    }

    @ApiOperation("根据id修改开放状态")
    @RequestMapping(value = "/updateStatus/{id}/{status}", method = RequestMethod.GET)
    public Result updateStatusById(@PathVariable("id") String id, @PathVariable("status") String status) {
        resourceService.updateStatusById(id, status);
        return new Result(true, StatusCode.OK, "修改成功");
    }
}
