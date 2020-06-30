package com.chinags.archives.controller;

import com.chinags.archives.client.EmployeeClient;
import com.chinags.archives.client.FileClient;
import com.chinags.archives.entity.ClerkDocument;
import com.chinags.archives.entity.ClerkDocumentFile;
import com.chinags.archives.entity.DocumentPermission;
import com.chinags.archives.service.ClerkDocumentFileService;
import com.chinags.archives.service.ClerkDocumentService;
import com.chinags.archives.service.DocumentPermissionService;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.file.entity.FileUpload;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author XieWenqing
 * @date 2019/11/20 上午 10:39
 */
@Api("公共文档查询controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/gongcheng")
public class GongchengController extends BaseController {
    @Autowired
    private ClerkDocumentService clerkDocumentService;
    @Autowired
    private ClerkDocumentFileService clerkDocumentFileService;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private DocumentPermissionService documentPermissionService;
    @Autowired
    private EmployeeClient employeeClient;

    @ApiOperation("查询工程档案案卷著录list")
    @RequestMapping(value ="/myCdList", method = RequestMethod.POST)
    public Result myCdList(@RequestBody ClerkDocument clerkDocument) {
        return new Result(true, StatusCode.OK, "",clerkDocumentService.myClerkDocumentList(clerkDocument));
    }

    @ApiOperation("保存工程档案案卷著录")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody ClerkDocument clerkDocument) {
        //获取登录用户
        String logincode = getLoginUser().getLogincode();
        String id = clerkDocument.getId();
        if(StringUtils.isEmpty(id)){  //id为空是新增操作，加入创建人创建时间等信息
            clerkDocument.setCreateBy(logincode);
            clerkDocument.setCreateDate(new Date());
            clerkDocument.setUpdateBy(logincode);
            clerkDocument.setUpdateDate(new Date());
        }else{
            clerkDocument.setUpdateBy(logincode);
            clerkDocument.setUpdateDate(new Date());
        }
        clerkDocumentService.save(clerkDocument);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("根据id工程档案案卷著录记录")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        ClerkDocument clerkDocument = clerkDocumentService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功", clerkDocument);
    }


    @ApiOperation("根据id删除工程档案案卷著录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        List<ClerkDocumentFile> clerkDocumentFileList = clerkDocumentFileService.findClerkDocumentFileByCkId(id);
        if(clerkDocumentFileList.size() > 0){
            return new Result(true, StatusCode.ERROR, "存在关联的卷内文件，无法删除！");
        }
        clerkDocumentService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @ApiOperation("根据id删除工程档案卷内文件")
    @RequestMapping(value = "/deleteFile/{id}", method = RequestMethod.GET)
    public Result deleteFileById(@PathVariable("id") String id) {
        //先删除所有关联的附件
        fileClient.deleteFileListByPid(id);
        clerkDocumentFileService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @ApiOperation("根据当前用户查询工程档案卷内文件list")
    @RequestMapping(value ="/fileListData", method = RequestMethod.POST)
    public Result fileListData(@RequestBody ClerkDocumentFile clerkDocumentFile) {
        //获取登录用户
        String logincode = getLoginUser().getLogincode();
        return new Result(true, StatusCode.OK, "",clerkDocumentFileService.fileListData(clerkDocumentFile, logincode));
    }

    @ApiOperation("查询工程档案卷内文件list")
    @RequestMapping(value ="/fileAllListData", method = RequestMethod.POST)
    public Result fileAllListData(@RequestBody ClerkDocumentFile clerkDocumentFile) {
        return new Result(true, StatusCode.OK, "",clerkDocumentFileService.fileAllListData(clerkDocumentFile));
    }

    @ApiOperation("保存工程档案卷内文件")
    @RequestMapping(value = "/fileSave", method = RequestMethod.POST)
    public Result fileSave(@RequestBody ClerkDocumentFile clerkDocumentFile) {
        //获取登录用户
        String logincode = getLoginUser().getLogincode();
        String id = clerkDocumentFile.getId();
        ClerkDocumentFile cdf = clerkDocumentFileService.findById(id);
        if(cdf == null){  //cdf为null时，说明是新增操作，加入创建人创建时间等信息
            clerkDocumentFile.setCreateBy(logincode);
            clerkDocumentFile.setCreateDate(new Date());
            clerkDocumentFile.setUpdateBy(logincode);
            clerkDocumentFile.setUpdateDate(new Date());
        }else{
            clerkDocumentFile.setUpdateBy(logincode);
            clerkDocumentFile.setUpdateDate(new Date());
        }
        //保存文档
        clerkDocumentFileService.save(clerkDocumentFile);

        /*
        保存文档权限，保存之前，先删除该文档之前的永久权限
         */
        documentPermissionService.deleteListByCdfId(id);
        String receivetype = clerkDocumentFile.getReceivetype();
        if("0".equals(receivetype)){
            //保存全部用户
            List<Map<String, Object>> map = employeeClient.findEmpMap();
            for(Map<String, Object> m : map) {
                DocumentPermission documentPermission = new DocumentPermission();
                documentPermission.setCreateBy(getLoginUser().getLogincode());
                documentPermission.setCreateDate(new Date());
                documentPermission.setUpdateBy(getLoginUser().getLogincode());
                documentPermission.setUpdateDate(new Date());
                documentPermission.setCdfId(clerkDocumentFile.getId());  //卷内文件id
                documentPermission.setPermissionTime("0");  //权限设为永久
                documentPermission.setUserId(m.get("LOGIN_CODE").toString());  //因为慧正原因，这里只能存入授权用户登录名loginCode（loginCode也是唯一值）
                documentPermissionService.save(documentPermission);
            }
        }else if("1".equals(receivetype)){
            //按勾选用户进行保存
            String[] userCodes = clerkDocumentFile.getReceiveCodes().split(",");
            for(String userCode : userCodes){
                DocumentPermission documentPermission = new DocumentPermission();
                documentPermission.setCreateBy(getLoginUser().getLogincode());
                documentPermission.setCreateDate(new Date());
                documentPermission.setUpdateBy(getLoginUser().getLogincode());
                documentPermission.setUpdateDate(new Date());
                documentPermission.setCdfId(clerkDocumentFile.getId());  //卷内文件id
                documentPermission.setPermissionTime("0");  //权限设为永久
                documentPermission.setUserId(userCode);
                documentPermissionService.save(documentPermission);
            }
        }else if("2".equals(receivetype)){
            //按部门保存
            String[] officeCodes = clerkDocumentFile.getReceiveCodes().split(",");
            for(String officeCode : officeCodes){
                List<Map<String, Object>> map = employeeClient.findEmpByOfficeCode(officeCode);
                for(Map<String, Object> m : map) {
                    DocumentPermission documentPermission = new DocumentPermission();
                    documentPermission.setCreateBy(getLoginUser().getLogincode());
                    documentPermission.setCreateDate(new Date());
                    documentPermission.setUpdateBy(getLoginUser().getLogincode());
                    documentPermission.setUpdateDate(new Date());
                    documentPermission.setCdfId(clerkDocumentFile.getId());  //卷内文件id
                    documentPermission.setPermissionTime("0");  //权限设为永久
                    documentPermission.setUserId(m.get("LOGIN_CODE").toString());  //因为慧正原因，这里只能存入授权用户登录名loginCode（loginCode也是唯一值）
                    documentPermissionService.save(documentPermission);
                }
            }
        }

        //为防止管理员用户创建文档无法保存自己权限，检查当前登录登录用户是否有文档权限，没有的话进行保存
        int count = documentPermissionService.countLoginUser(getLoginUser().getLogincode(), id);
        if(count <1){
            DocumentPermission documentPermission = new DocumentPermission();
            documentPermission.setCreateBy(getLoginUser().getLogincode());
            documentPermission.setCreateDate(new Date());
            documentPermission.setUpdateBy(getLoginUser().getLogincode());
            documentPermission.setUpdateDate(new Date());
            documentPermission.setCdfId(clerkDocumentFile.getId());  //卷内文件id
            documentPermission.setPermissionTime("0");  //权限设为永久
            documentPermission.setUserId(getLoginUser().getLogincode());
            documentPermissionService.save(documentPermission);
        }
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("根据id工程档案卷内文件记录")
    @RequestMapping(value = "/findCdfById/{id}", method = RequestMethod.GET)
    public Result findCdfById(@PathVariable("id") String id) {
        ClerkDocumentFile clerkDocumentFile = clerkDocumentFileService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", clerkDocumentFile);
    }

    /**
     * 能否查看文档
     * @param cdfId  文档id
     * @return  返回1或者0
     */
    @ApiOperation("当前用户是否能查看该文档，返回1能看0不能看")
    @RequestMapping(value = "/cansee/{cdfId}", method = RequestMethod.GET)
    public Result cansee(@PathVariable("cdfId") String cdfId) {
        //获取登录用户登录名
        String logincode = getLoginUser().getLogincode();
        int cansee = documentPermissionService.cansee(logincode, cdfId);
        return new Result(true, StatusCode.OK, "查询成功", cansee);
    }

    /**
     * 获取当前登录用户能查看的文档附件list
     * @param userCode 当前用户登录名
     * @param title 案卷题名，可为空
     */
    @ApiOperation("根据案卷题名，查找当前用户能查看的附件list")
//    @RequestMapping(value = "/fileList/{userCode},{title}", method = RequestMethod.GET)
    @RequestMapping(value = "/fileList", method = RequestMethod.GET)
    @ResponseBody
//    public List<Map<String, Object>> fileList(@PathVariable("userCode") String userCode, @PathVariable("title") String title){file/preview
    public List<Map<String, Object>> fileList(@RequestParam("userCode") String userCode, @RequestParam("title") String title){
        //记录最终要返回的list
        List<Map<String ,Object>> reList = Lists.newArrayList();
        //title没传值时是查询所有
        if(StringUtils.isEmpty(title.trim())){
            title="";
        }

        //获取当前登录用户可查看的卷内文件id
        List<String> cdfIdList = documentPermissionService.CdfIdList(userCode);
        for(String cdfId :cdfIdList){
            //List<FileUpload> fileUploadList = fileClient.fileuploadlistByPid(cdfId);
            //获取需要展示的卷内文件+所属的案卷标题
            List<Map<String, Object>> list = clerkDocumentFileService.findClerkDocumentFile(cdfId, title);
           if(list.size() > 0 ){
               //获取关联的附件
               List<FileUpload> fileUploadList = fileClient.fileuploadlistByPid(cdfId);
               for(FileUpload fu : fileUploadList){
                   Map m = MapUtils.newHashMap();
                  /* m.put("cdtitle", list.get(0).get("CDTITLE"));  //案卷著录标题
                   m.put("startDate", list.get(0).get("START_DATE"));  //起止时间
                   m.put("title", list.get(0).get("TITLE"));  //卷内文件标题
                   m.put("storageTime", list.get(0).get("STORAGE_TIME"));  //保管期限
                   m.put("archivesNo", list.get(0).get("ARCHIVES_NO"));  //案卷号
                   m.put("year", list.get(0).get("YEAR"));  //年度
                   m.put("responsiblePerson", list.get(0).get("RESPONSIBLE_PERSON"));  //责任者
                   m.put("documentType", list.get(0).get("DOCUMENT_TYPE"));  //分类号
                   m.put("REMARKS", list.get(0).get("DOCUMENT_TYPE"));  //备注
                   m.put("boxNo", list.get(0).get("BOX_NO"));  //档号*/
                  m.put("info", list.get(0));
                   m.put("fu", fu);
                   reList.add(m);
               }
           }
        }
        return reList;
    }



}
