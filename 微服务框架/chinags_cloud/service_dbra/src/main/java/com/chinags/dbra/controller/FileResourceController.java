package com.chinags.dbra.controller;

import com.chinags.archives.entity.ClerkDocumentFile;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.client.ClerkDocumentFileClient;
import com.chinags.dbra.entity.FileResource;
import com.chinags.dbra.service.FileResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-8-29
 **/
@Api("档案API controller")
@RestController
@CrossOrigin
@RequestMapping("/file")
public class FileResourceController extends BaseController {
    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private ClerkDocumentFileClient clerkDocumentFileClient;

    @ApiOperation("分页查询全部发布档案")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public Result findAllByPage(FileResource entity) {
        PageResult<FileResource> page = fileResourceService.findAllByPage(entity, null);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Result(false, StatusCode.ERROR, "没有发布的档案");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("保存档案服务")
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody FileResource fileResource) {
        boolean isAdd = false;
        String userCode = getLoginUser().getUsercode();
        // 第一次创建的时候需要增加创建人和创建时间
        if (StringUtils.isEmpty(fileResource.getCreateBy())) {
            fileResource.setCreateBy(userCode);
            fileResource.setCreateDate(new Date());
            isAdd = true;
        }
        // 所有保存需要保存最后一次的操作人和操作时间
        fileResource.setUpdateBy(userCode);
        fileResource.setUpdateDate(new Date());
        fileResourceService.save(fileResource);
        fileResource.setHost("/front/file/down/" + fileResource.getId());
        fileResourceService.save(fileResource);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("根据id删除档案服务")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        fileResourceService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @ApiOperation("根据id查询档案服务后台显示")
    @RequestMapping(value = "/selectById/{id}", method = RequestMethod.GET)
    public Result selectById(@PathVariable("id") String id) {
        FileResource fileResource = fileResourceService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功", fileResource);
    }

    @ApiOperation("获取所有已发布的档案API")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result findAll() {
        List<FileResource> fileResources = fileResourceService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", fileResources);
    }

    @ApiOperation("分页获取可发布的档案")
    @RequestMapping(value = "/public/file", method = RequestMethod.POST)
    public Result findPublicFileByPage(ClerkDocumentFile clerkDocumentFile) {
        return clerkDocumentFileClient.findClerkDocumentFilesByIsPublic(clerkDocumentFile);
    }

    @ApiOperation("根据id获取可发布档案信息")
    @RequestMapping(value = "/publicFile/{id}", method = RequestMethod.GET)
    public Result findFileById(@PathVariable("id") String id) {
        return clerkDocumentFileClient.findById(id);
    }

    @ApiOperation("根据id修改开放状态")
    @RequestMapping(value = "/updateStatus/{id}/{status}", method = RequestMethod.GET)
    public Result updateStatusById(@PathVariable("id") String id, @PathVariable("status") String status) {
        fileResourceService.updateStatusById(id, status);
        return new Result(true, StatusCode.OK, "修改成功");
    }

}
