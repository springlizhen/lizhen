package com.chinags.dbra.controller;

import com.alibaba.fastjson.JSON;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.OtherResource;
import com.chinags.dbra.service.ApiParamService;
import com.chinags.dbra.service.OtherResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Mark_Wang
 * @date 2019/7/18
 **/
@Api("资源管理controller")
@RestController
@CrossOrigin
@RequestMapping("/or")
public class OtherResourceController extends BaseController {

    @Autowired
    private OtherResourceService otherResourceService;

    @Autowired
    private ApiParamService apiParamService;

    @ApiOperation("分页查询所有第三方服务")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public Result findAllByPage(BaseEntity entity) {
        PageResult<OtherResource> page = otherResourceService.findAllByPage(entity, null, null);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有数据，请注册");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("保存第三方服务")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody OtherResource otherResource) {
        if (otherResourceService.checkSave(otherResource.getId())) {
            return new Result(false, StatusCode.ERROR, "服务id已存在");
        }
        String userCode = getLoginUser().getUsercode();
        if (StringUtils.isEmpty(otherResource.getCreateBy())) {
            otherResource.setCreateBy(userCode);
            otherResource.setCreateDate(new Date());
            otherResource.setUrl("/service/other/" + otherResource.getId());
        }
        otherResource.setUpdateBy(userCode);
        otherResource.setUpdateDate(new Date());
        otherResourceService.save(otherResource);
        // 修改参数的接口id
        apiParamService.updateApiIdByUserCode(otherResource.getId(), userCode);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("更新第三方服务")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody OtherResource otherResource) {
        otherResource.setUpdateBy(getLoginUser().getUsercode());
        otherResource.setUpdateDate(new Date());
        otherResource.setStatus("停用");
        OtherResource otherResource1 = otherResourceService.save(otherResource);
        if (otherResource != null) {
            return new Result(true, StatusCode.OK, "保存成功");
        } else {
            return new Result(false, StatusCode.ERROR, "保存失败");
        }
    }

    @ApiOperation("根据id修改状态")
    @RequestMapping(value = "/updateStatusById/{id}/{status}", method = RequestMethod.GET)
    public Result updateStatusById(@PathVariable("id") String id, @PathVariable("status") String status) {
        otherResourceService.updateStatusById(status, id);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @ApiOperation("根据条件分页查询")
    @RequestMapping(value = "findByEntity", method = RequestMethod.POST)
    public Result findByEntity(@RequestBody OtherResource otherResource) {
        PageResult<OtherResource> page = otherResourceService.findByEntity(otherResource);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("获取去重的状态")
    @RequestMapping(value = "/findStatus", method = RequestMethod.GET)
    public Result findStatus() {
        return new Result(true, StatusCode.OK, "查询成功", otherResourceService.getStatus());
    }

    @ApiOperation("获取去重的接入类型")
    @RequestMapping(value = "/findAcessType", method = RequestMethod.GET)
    public Result findAccessType() {
        return new Result(true, StatusCode.OK, "查询成功", otherResourceService.getAccessType());
    }

    @ApiOperation("获取去重的服务分类")
    @RequestMapping(value = "/findServiceClass", method = RequestMethod.GET)
    public Result findServiceClass() {
        return new Result(true, StatusCode.OK, "查询成功", otherResourceService.getServiceClasses());
    }

    @ApiOperation("根据id查询第三方服务详情")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result getById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "查询成功", otherResourceService.getById(id));
    }

    @ApiOperation("根据id删除第三方服务")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        otherResourceService.deleteById(id);
        // 清理参数
        apiParamService.deleteByApiId(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
