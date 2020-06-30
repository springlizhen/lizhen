package com.chinags.dbra.controller;

import com.alibaba.fastjson.JSON;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.client.FileClient;
import com.chinags.dbra.entity.*;
import com.chinags.dbra.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-9-19
 **/
@Api("前端controller")
@RestController
@CrossOrigin
@RequestMapping("/front")
public class FrontResourceController {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private ThemeService themeService;

    @ApiOperation("资源目录基本信息接口")
    @RequestMapping(value = "/re/selectById/{id}", method = RequestMethod.GET)
    public Result selectById(@PathVariable("id") String id) {
        Resource resource = resourceService.findOneById(id);
        Theme theme = themeService.findThemeById(resource.getTheme());
        resource.setThemeName(theme.getName());
        resourceService.updateCatNumById(id);
        return new Result(true, StatusCode.OK, "查询成功", resource);
    }

    @ApiOperation("分页查询资源目录列表接口")
    @RequestMapping(value = "/re/findByPage/{theme}", method = RequestMethod.POST)
    public Result findByPage(@PathVariable("theme") String theme, @RequestBody BaseEntity entity) {
        PageResult<Resource> page;
        if (theme.equals("00")) {
            page = resourceService.findByPage(entity);
        } else {
            if (entity.getPageNo() == null || entity.getPageSize() == null || entity.getOrderBy() == null) {
                entity.setPageNo(1);
                entity.setPageSize(20);
                entity.setOrderBy("createDate");
            }
            page = resourceService.findByTheme(entity.getPageNo(), entity.getPageSize(), entity.getOrderBy(), theme);
        }
        List<Resource> resources = page.getList();
        List<Resource> res = new ArrayList<>();
        for (Resource resource : resources) {
            DbTable dbTable = dbTableService.findById(resource.getTbId());
            Long nums = basicDataService.findCount(dbTable.getDbId(), dbTable.getName());
            resource.setDataSize(nums);
            res.add(resource);
        }
        page.setList(res);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有注册的资源目录");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @Autowired
    private ResourceApiService resourceApiService;

    @ApiOperation("查询数据项接口")
    @RequestMapping(value = "/api/col/{reId}", method = RequestMethod.GET)
    public Result findColByReId(@PathVariable("reId") String reId) {
        ResourceApi resourceApi = resourceApiService.findByReId(reId);
        List<BasicData> basicDatas = JSON.parseArray(resourceApi.getColumns(), BasicData.class);
        return new Result(true, StatusCode.OK, "查询成功", basicDatas);
    }

    @ApiOperation("资源目录Api服务查询接口")
    @RequestMapping(value = "/re/find/{reId}", method = RequestMethod.GET)
    public Result findApiByReId(@PathVariable("reId") String reId) {
        ResourceApi resourceApi = resourceApiService.findByReId(reId);
        return new Result(true, StatusCode.OK, "查询成功", resourceApi);
    }

    @Autowired
    private ApiParamService apiParamService;

    @ApiOperation("根据apiId查询接口参数")
    @RequestMapping(value = "/param/{apiId}", method = RequestMethod.GET)
    public Result findByApiId(@PathVariable("apiId") String apiId) {
        List<ApiParam> apiParams = apiParamService.find(null, apiId);
        return new Result(true, StatusCode.OK, "查询成功", apiParams);
    }

    @Autowired
    private BasicDataService basicDataService;

    @Autowired
    private DbTableService dbTableService;

    @ApiOperation("数据预览接口")
    @RequestMapping(value = "/re/{resourceId}/preview", method = RequestMethod.GET)
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

    @ApiOperation("统计数据量接口")
    @RequestMapping(value = "/re/{resourceId}/data_count", method = RequestMethod.POST)
    public Result getDataCount(@PathVariable("resourceId") String resourceId) {
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

    @ApiOperation("数据下载接口")
    @RequestMapping(value = "/re/{resourceId}/all_data", method = RequestMethod.POST)
    public Result getAllData(@PathVariable("resourceId") String resourceId) {
        // 服务验证
        Resource resource = resourceService.findById(resourceId);
        if (resource == null) {
            return new Result(false, StatusCode.ERROR, "未找到改服务");
        }
        if (resource.getStatus().equals("禁用")) {
            return new Result(false, StatusCode.ERROR, "该服务已禁用，请联系管理员");
        }
        ResourceApi resourceApi = resourceApiService.findByReId(resource.getId());
        List<Object> resultList = basicDataService.findAllByResourceApi(resourceApi);
        // 增加数据下载量
        resourceService.updateDownNumById(resourceId);
        if (CollectionUtils.isEmpty(resultList)) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", resultList);
        }
    }

    @ApiOperation("首页数据集接口")
    @RequestMapping(value = "/re/count", method = RequestMethod.GET)
    public Result countResource() {
        return new Result(true, StatusCode.OK, "查询成功", resourceService.countResource());
    }

    @ApiOperation("首页下载量接口")
    @RequestMapping(value = "/re/downSum", method = RequestMethod.GET)
    public Result sumDownNum() {
        return new Result(true, StatusCode.OK, "查询成功", resourceService.sumDownNum());
    }

    @ApiOperation("首页数据量接口")
    @RequestMapping(value = "/re/dataSum", method = RequestMethod.GET)
    public Result dataSum() {
        Long sum = 0L;
        List<String> tables = resourceService.findAllTables();
        if (CollectionUtils.isEmpty(tables))
            return new Result(true, StatusCode.OK, "查询成功", sum);
        for (String table : tables) {
            DbTable dbTable = dbTableService.findById(table);
            Long nums = basicDataService.findCount(dbTable.getDbId(), dbTable.getName());
            sum = sum + nums;
        }
        return new Result(true, StatusCode.OK, "查询成功", sum);
    }

    @Autowired
    private FileClient fileClient;

    @ApiOperation("查询所有主题分类")
    @RequestMapping(value = "/theme", method = RequestMethod.GET)
    public Result findAllThemes() {
        List<Theme> themes = themeService.findAll();
        List<Theme> result = new ArrayList<>();
        for (Theme theme : themes) {
            theme.setImg(fileClient.downByPid(theme.getId()));
            result.add(theme);
        }
        return new Result(true, StatusCode.OK, "查询成功", result);
    }

    @ApiOperation("主题分类数据集数量统计接口")
    @RequestMapping(value = "/theme/sum", method = RequestMethod.GET)
    public Result findThemeResourceSum() {
        return new Result(true, StatusCode.OK, "查询成功", themeService.findThemeResourceSum());
    }

    @Autowired
    private ServiceApplicantService serviceApplicantService;

    @ApiOperation("保存服务申请")
    @RequestMapping(value = "/application/save", method = RequestMethod.POST)
    public Result save(@RequestBody ServiceApplicant serviceApplicant) {
        serviceApplicantService.save(serviceApplicant);
        // 如果是档案服务要增加申请次数
        if (serviceApplicant.getType().equals("file")) {
            fileResourceService.updateApplyNumById(serviceApplicant.getResourceId());
        }
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @Autowired
    private OtherResourceService otherResourceService;

    @ApiOperation("分页查询数据服务信息")
    @RequestMapping(value = "/ore/findAll/{serviceClass}/{name}", method = RequestMethod.POST)
    public Result findAllByPage(@RequestBody BaseEntity entity, @PathVariable("serviceClass") String serviceClass, @PathVariable("name") String name) {
        if (serviceClass.equalsIgnoreCase("N")) {
            serviceClass = null;
        }
        if (name.equalsIgnoreCase("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNN")) {
            name = null;
        }
        PageResult<OtherResource> page = otherResourceService.findAllByPage(entity, serviceClass, name);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("获取服务分类")
    @RequestMapping(value = "/ore/findServiceClass", method = RequestMethod.GET)
    public Result findServiceClass() {
        return new Result(true, StatusCode.OK, "查询成功", otherResourceService.getServiceClasses());
    }

    @ApiOperation("根据id数据服务详情")
    @RequestMapping(value = "/ore/findById/{id}", method = RequestMethod.GET)
    public Result getById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "查询成功", otherResourceService.getById(id));
    }

    @ApiOperation("分页查询全部发布档案")
    @RequestMapping(value = "/file/find/{type}", method = RequestMethod.POST)
    public Result findAllByPage(@RequestBody FileResource entity, @PathVariable("type") String type) {
        if (type.equalsIgnoreCase("N")) {
            type = null;
        }
        if (entity.getType().equalsIgnoreCase("N")) {
            entity.setType(null);
        }
        PageResult<FileResource> page = fileResourceService.findAllByPage(entity, type);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有发布的档案");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("根据id查询档案服务详情")
    @RequestMapping(value = "/file/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        FileResource fileResource = fileResourceService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功", fileResource);
    }

    @ApiOperation("获取所有文件服务分类")
    @RequestMapping(value = "/file/findType", method = RequestMethod.GET)
    public Result findAllType() {
        List<String> types = fileResourceService.findAllType();
        return new Result(true, StatusCode.OK, "查询成功", types);
    }

    @ApiOperation("根据用户和服务分类查询用户申请通过的服务")
    @RequestMapping(value = "/service/find/{createBy}/{type}", method = RequestMethod.GET)
    public Result findByCreateByAndType(@PathVariable("createBy") String createBy, @PathVariable("type") String type) {
        return new Result(true, StatusCode.OK, "查询成功", serviceApplicantService.findByUserAndType(createBy, type));
    }

    @ApiOperation("获取服务申请信息")
    @RequestMapping(value = "/service/status/{createBy}/{reId}", method = RequestMethod.GET)
    public Result findByCreateByAndReId(@PathVariable("createBy") String createBy, @PathVariable("reId") String reId) {
        ServiceApplicant serviceApplicant = serviceApplicantService.findByCreateByAndResourceId(createBy, reId);
        return new Result(true, StatusCode.OK, "查询成功", serviceApplicant);
    }

    @ApiOperation("根据档案服务Id下载档案接口")
    @RequestMapping(value = "/file/down/{createBy}/{reId}", method = RequestMethod.GET)
    public Result downLoadFile(@PathVariable("reId") String reId, @PathVariable("createBy") String createBy) {
        // 验证用户的服务申请是否已通过
        ServiceApplicant serviceApplicant = serviceApplicantService.findByCreateByAndResourceId(createBy, reId);
        if (serviceApplicant == null || !serviceApplicant.getStatus().equalsIgnoreCase("已审核")) {
            return new Result(false, StatusCode.ERROR, "没有访问该服务的权限");
        }
        // 根据resourceId获取出档案服务的信息,通过档案服务信息获取档案id
        FileResource fileResource = fileResourceService.selectById(reId);
        if (fileResource != null) {
            return new Result(true, StatusCode.OK, "查询成功", fileResource.getFileId());
        } else {
            return new Result(false, StatusCode.ERROR, "没有档案服务信息");
        }
    }
}
