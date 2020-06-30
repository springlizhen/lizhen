package com.chinags.dbra.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.JwtUtil;
import com.chinags.dbra.entity.*;
import com.chinags.dbra.service.*;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Mark_Wang
 * @date 2019/7/22
 **/
@Api("提供的服务接口")
@RestController
@CrossOrigin
@RequestMapping("/service")
public class ServicesController extends BaseController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private DbTableService dbTableService;

    @Autowired
    private ResourceApiService resourceApiService;

    @Autowired
    private BasicDataService basicDataService;

    @Autowired
    private ServiceApplicantService serviceApplicantService;

    @Autowired
    private OtherResourceService otherResourceService;

    @Autowired
    private ApiParamService apiParamService;

    @ApiOperation("本地服务接口")
    @RequestMapping(value = "/this/{resourceId}/data", method = RequestMethod.POST)
    public Result getData(@PathVariable("resourceId") String resourceId, @RequestParam("pageNo") int pageNo,
                          @RequestParam("pageSize") int pageSize, @RequestParam("token") String token, @RequestBody String condition) {
        // 根据用户token获取用户登录名
        Claims claims = JwtUtil.parseJWT(token);
        String createBy = claims.getId();
        // 用户权限验证
        ServiceApplicant serviceApplicant = serviceApplicantService.findByCreateByAndResourceId(createBy, resourceId);
        if (serviceApplicant == null || !serviceApplicant.getStatus().equalsIgnoreCase("已审核")) {
            return new Result(false, StatusCode.ERROR, "没有访问该服务的权限");
        }
        // 服务验证
        Resource resource = resourceService.findById(resourceId);
        if (resource == null) {
            return new Result(false, StatusCode.ERROR, "未找到改服务");
        }
        if (resource.getStatus().equals("禁用")) {
            return new Result(false, StatusCode.ERROR, "该服务已禁用，请联系管理员");
        }
        DbTable dbTable = dbTableService.findById(resource.getTbId());
        ResourceApi resourceApi = resourceApiService.findByReId(resource.getId());
        int num = 0;
        int size = pageSize;
        if (pageNo != 1) {
            num = pageSize * (pageNo - 1);
            size = pageSize * pageNo;
        }
        Long nums = basicDataService.findCount(dbTable.getDbId(), dbTable.getName());
        List<Object> resultList = basicDataService.findByResourceApi(resourceApi, condition, size, num);
        PageResult<Object> page = PageResult.getPageResult(resultList, nums, pageNo);
        resourceApiService.updateCallNumById(resourceApi.getId());
        if (CollectionUtils.isEmpty(resultList)) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("本地服务统计数据量接口")
    @RequestMapping(value = "/this/{resourceId}/data_count", method = RequestMethod.POST)
    public Result getDataCount(@PathVariable("resourceId") String resourceId, @RequestParam("token") String token) {
        // 根据用户token获取用户登录名
        Claims claims = JwtUtil.parseJWT(token);
        String createBy = claims.getId();
        // 用户权限验证
        ServiceApplicant serviceApplicant = serviceApplicantService.findByCreateByAndResourceId(createBy, resourceId);
        if (serviceApplicant == null || !serviceApplicant.getStatus().equalsIgnoreCase("已审核")) {
            return new Result(false, StatusCode.ERROR, "没有访问该服务的权限");
        }
        // 服务验证
        Resource resource = resourceService.findById(resourceId);
        if (resource == null) {
            return new Result(false, StatusCode.ERROR, "未找到改服务");
        }
        if (resource.getStatus().equals("禁用")) {
            return new Result(false, StatusCode.ERROR, "该服务已禁用，请联系管理员");
        }
        DbTable dbTable = dbTableService.findById(resource.getTbId());
        Long nums = basicDataService.findCount(dbTable.getDbId(), dbTable.getName());
        return new Result(true, StatusCode.OK, "查询成功", nums);
    }

    @ApiOperation("本地服务数据预览接口")
    @RequestMapping(value = "/this/{resourceId}/preview", method = RequestMethod.GET)
    public Result getPreviewData(@PathVariable("resourceId") String resourceId) {
        // 服务验证
        Resource resource = resourceService.findById(resourceId);
        if (resource == null) {
            return new Result(false, StatusCode.ERROR, "未找到改服务");
        }
        if (resource.getStatus().equals("禁用")) {
            return new Result(false, StatusCode.ERROR, "该服务已禁用，请联系管理员");
        }
        DbTable dbTable = dbTableService.findById(resource.getTbId());
        ResourceApi resourceApi = resourceApiService.findByReId(resource.getId());
        Long nums = basicDataService.findCount(dbTable.getDbId(), dbTable.getName());
        List<Object> resultList = basicDataService.findByResourceApi(resourceApi, null, 20, 0);
        PageResult<Object> page = PageResult.getPageResult(resultList, nums, 1);
        if (CollectionUtils.isEmpty(resultList)) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("数据服务接口")
    @RequestMapping(value = "/other/{resourceId}", method = RequestMethod.GET)
    public Result otherResource(@PathVariable("resourceId") String resourceId, @RequestParam("token") String token, @RequestBody Map<String, Object> param) throws Exception {
        // 根据用户token获取用户登录名
        Claims claims = JwtUtil.parseJWT(token);
        String createBy = claims.getId();
        // 用户权限验证
        ServiceApplicant serviceApplicant = serviceApplicantService.findByCreateByAndResourceId(createBy, resourceId);
        if (serviceApplicant == null || !serviceApplicant.getStatus().equalsIgnoreCase("已审核")) {
            return new Result(false, StatusCode.ERROR, "没有访问该服务的权限");
        }
        // 服务验证
        OtherResource otherResource = otherResourceService.getById(resourceId);
        if (otherResource == null) {
            return new Result(false, StatusCode.ERROR, "未找到改服务");
        }
        if (otherResource.getStatus().equals("禁用")) {
            return new Result(false, StatusCode.ERROR, "该服务已禁用，请联系管理员");
        }
        String url = otherResource.getUrl();
        List<ApiParam> apiParams = apiParamService.find(null, resourceId);
        if (!CollectionUtils.isEmpty(apiParams)) {
            url = url + "?";
            int i = 0;
            for (ApiParam apiParam : apiParams) {
                Object value = param.get(apiParam.getName());
                if (value == null && apiParam.getIsNull().equals("否")) {
                    return new Result(false, StatusCode.ERROR, "参数错误，必填项为空");
                } else {
                    url = url + apiParam.getName() + "=" + String.valueOf(value);
                }
                if (i != apiParams.size() - 1) {
                    url = url + "&";
                }
                i++;
            }
        }
        Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").timeout(5000).get();
        return new Result(true, StatusCode.OK, "查询成功", doc.select("body").text());
    }

    @ApiOperation("保存服务注册信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody ServiceApplicant serviceApplicant) {
        String userCode = getLoginUser().getLogincode();
        serviceApplicant.setCreateBy(userCode);
        ServiceApplicant serviceApplicant1 = serviceApplicantService.findByCreateByAndResourceId(serviceApplicant.getCreateBy(), serviceApplicant.getResourceId());
        if (serviceApplicant1 == null) {
            serviceApplicant.setStatus("未审核");
            serviceApplicantService.save(serviceApplicant);
            return new Result(true, StatusCode.OK, "申请成功");
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
}
