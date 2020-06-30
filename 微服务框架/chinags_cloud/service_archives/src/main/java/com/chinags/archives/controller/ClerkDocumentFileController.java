package com.chinags.archives.controller;

import com.chinags.archives.entity.ClerkDocumentFile;
import com.chinags.archives.service.ClerkDocumentFileService;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author XieWenqing
 * @date 2019/9/29 下午 2:50
 */

@Api("公共文档查询controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/documentfile")
public class ClerkDocumentFileController extends BaseController {
    @Autowired
    private ClerkDocumentFileService clerkDocumentFileService;

    @ApiOperation("查询公共文档")
    @RequestMapping(value ="/public", method = RequestMethod.POST)
    public Result findClerkDocumentFilesByIsPublic(ClerkDocumentFile clerkDocumentFile) {
        return new Result(true, StatusCode.OK, "",clerkDocumentFileService.findClerkDocumentFilesByIsPublic(clerkDocumentFile));
    }

    @ApiOperation("根据id查询档案")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "查询成功", clerkDocumentFileService.findById(id));
    }
}
