package com.chinags.dbra.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.Resource;
import com.chinags.dbra.entity.ServiceApplicant;
import com.chinags.dbra.service.OtherResourceService;
import com.chinags.dbra.service.ResourceApiService;
import com.chinags.dbra.service.ResourceService;
import com.chinags.dbra.service.ServiceApplicantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/22
 **/
@Api("注册审批接口")
@RestController
@CrossOrigin
@RequestMapping("/sa")
public class ServiceApplicantController extends BaseController {

    @Autowired
    private ServiceApplicantService serviceApplicantService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceApiService resourceApiService;

    @Autowired
    private OtherResourceService otherResourceService;

    @ApiOperation("根据条件分页查询所有的注册服务")
    @RequestMapping(value = "/findByPage", method = RequestMethod.POST)
    public Result findByPage(@RequestBody ServiceApplicant serviceApplicant) {
        PageResult<ServiceApplicant> page = serviceApplicantService.findByPage(serviceApplicant);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("根据id修改状态/审批")
    @RequestMapping(value = "/ups/{id}/{status}", method = RequestMethod.GET)
    public Result updateStatusById(@PathVariable("id") String id, @PathVariable("status") String status) {
        String updateBy = getLoginUser().getUsercode();
        serviceApplicantService.updateStatusById(id, status, updateBy, new Date());
        // 审批通过对api的应用连接数+1
        if (status.equals("已审核")) {
            ServiceApplicant serviceApplicant = serviceApplicantService.findById(id);
            // 本地数据服务
            if (serviceApplicant.getType().equals("this")) {
                Resource resource = resourceService.findById(serviceApplicant.getResourceId());
                resourceApiService.updateConnNumByReId(resource.getId());
            } else if (serviceApplicant.getType().equals("other")) { // 第三方服务
                otherResourceService.addConnNum(serviceApplicant.getResourceId());
            }
        }
        return new Result(true, StatusCode.OK, "审批成功");
    }

    @ApiOperation("根据id查询服务注册信息")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "查询成功", serviceApplicantService.findById(id));
    }

    @ApiOperation("用户查询自己申请的服务")
    @RequestMapping(value = "/findByUser", method = RequestMethod.GET)
    public Result findByUser() {
        List<ServiceApplicant> serviceApplicants = serviceApplicantService.findByUser(getLoginUser().getUsercode());
        return new Result(true, StatusCode.OK, "查询成功", serviceApplicants);
    }

    @ApiOperation("保存服务注册信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody ServiceApplicant serviceApplicant) {
        ServiceApplicant serviceApplicant1 = serviceApplicantService.findByCreateByAndResourceId(serviceApplicant.getCreateBy(), serviceApplicant.getResourceId());
        if (serviceApplicant1 == null) {
            serviceApplicantService.save(serviceApplicant);
            return new Result(true, StatusCode.OK, "保存成功");
        } else {
            if (serviceApplicant1.getStatus().equals("未审核")) {
                return new Result(false, StatusCode.ERROR, "已申请过服务,审核中");
            } else if (serviceApplicant1.getStatus().equals("已审核")) {
                return new Result(false, StatusCode.ERROR, "已申请过服务,审核通过");
            } else {
                serviceApplicantService.updateStatusById(serviceApplicant1.getId(), "未审核", serviceApplicant.getCreateBy(), new Date());
                return new Result(true, StatusCode.OK, "保存成功");
            }
        }
    }

    @ApiOperation("根据类型分页查询")
    @RequestMapping(value = "/findByType/{type}", method = RequestMethod.POST)
    public Result findByType(@PathVariable("type") String type, BaseEntity entity) {
        PageResult<ServiceApplicant> page = serviceApplicantService.findByType(entity, type);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

}
