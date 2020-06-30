package com.chinags.device.check.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.device.check.entity.Standard;
import com.chinags.device.check.service.StandardService;
import com.chinags.device.schedule.client.FileUploadClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 设备
 *
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("设备管理controller")
@RestController
@CrossOrigin
@RequestMapping("/standard")
public class StandardController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(StandardController.class);

    @Autowired
    private StandardService standardService;
    @Autowired
    private FileUploadClient fileUploadClient;

    /**
     * 分页获取设备信息
     * @return
     */
        @ApiOperation("分页获取规范信息")
        @RequestMapping(value = "/select", method = RequestMethod.GET)
        @ResponseBody
        public Result officeListPage(Standard standard){
            PageResult<Standard> page;
            try {
                //类似查询全部
                page = standardService.find(standard);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(false, StatusCode.ERROR, "获取失败");
            }
            return new Result(true, StatusCode.OK, "获取成功",page);
        }
        @ApiOperation("获取规范信息")
        @RequestMapping(value = "/selectStandard", method = RequestMethod.GET)
        @ResponseBody
        public Result selectStandard(String id){
            Standard standard=null;
            try {
                //类似查询全部
                standard = standardService.selectStandard(id);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(false, StatusCode.ERROR, "获取失败");
            }
            return new Result(true, StatusCode.OK, "获取成功",standard);
        }






        @ApiOperation("保存设备")
        @RequestMapping(value = "/save", method = RequestMethod.POST)
        @ResponseBody
        public Result save(@RequestBody Standard standard) {
            Boolean isNew=false;
            if(standard.getId()==null){
                isNew=true;
            }
            standardService.save(standard);
            if(isNew){
                fileUploadClient.updatePid(standard.getId(),"standard");
            }
            return new Result(true, StatusCode.OK, "保存成功");
        }

        @ApiOperation("删除设备")
        @RequestMapping(value = "/delete", method = RequestMethod.GET)
        @ResponseBody
        public Result delete(Standard standard) {
            standardService.delete(standard);
            return new Result(true, StatusCode.OK, "删除成功");








        }
}
