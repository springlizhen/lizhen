package com.chinags.device.check.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.device.check.entity.Document;
import com.chinags.device.check.entity.Standard;
import com.chinags.device.check.service.DocumentService;
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
@RequestMapping("/document")
public class DocumentController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentService documentService;
    @Autowired
    private FileUploadClient fileUploadClient;

    /**
     * 分页获取设备信息
     * @return
     */
        @ApiOperation("分页获取规范信息")
        @RequestMapping(value = "/select", method = RequestMethod.GET)
        @ResponseBody
        public Result officeListPage(Document document){
            PageResult<Document> page;
            try {
                //类似查询全部
                page = documentService.find(document);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(false, StatusCode.ERROR, "获取失败");
            }
            return new Result(true, StatusCode.OK, "获取成功",page);
        }
        @ApiOperation("获取规范信息")
        @RequestMapping(value = "/selectDocument", method = RequestMethod.GET)
        @ResponseBody
        public Result selectDocument(String id){
            Document document=null;
            try {
                //类似查询全部
                document = documentService.selectDocument(id);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(false, StatusCode.ERROR, "获取失败");
            }
            return new Result(true, StatusCode.OK, "获取成功",document);
        }






        @ApiOperation("保存设备")
        @RequestMapping(value = "/save", method = RequestMethod.POST)
        @ResponseBody
        public Result save(@RequestBody Document document) {
            Boolean isNew=false;
            if(document.getId()==null){
                isNew=true;
            }
            documentService.save(document);
            if(isNew){
                fileUploadClient.updatePid(document.getId(),"document");
            }
            return new Result(true, StatusCode.OK, "保存成功");
        }

        @ApiOperation("删除设备")
        @RequestMapping(value = "/delete", method = RequestMethod.GET)
        @ResponseBody
        public Result delete(Document document) {
            documentService.delete(document);
            return new Result(true, StatusCode.OK, "删除成功");








        }
}
