package com.chinags.auth.controller;

import com.chinags.auth.entity.Contract;
import com.chinags.auth.entity.File;
import com.chinags.auth.service.ContractService;
import com.chinags.auth.service.FileService;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api("文件管理controller")
@RestController
@CrossOrigin
@RequestMapping("/file")
public class FileController extends BaseController {


    @Autowired
    private FileService fileService;

    @ApiOperation("查询")
    @RequestMapping(value = "/selectFile", method = RequestMethod.GET)
    public Result select(String id) {
        File file=fileService.selectFileById(id);
        if(StringUtils.isNotNull(file)) {
            return  new Result(true, StatusCode.OK, "查询成功",file);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", file);
        }
    }
    @ApiOperation("条件查询")
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public Result select(@RequestBody File file) {
        PageResult<File> list=fileService.selectFile(file);
        if(!StringUtils.isEmpty(list.getList())) {

            return new Result(true, StatusCode.OK, "查询成功", list);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", list);

        }
    }
    @ApiOperation("新增")
    @RequestMapping(value = "/saveFile", method = RequestMethod.POST)
    public Result saveFile(@RequestBody File file) {
        return  fileService.saveFile(file);

    }




}
