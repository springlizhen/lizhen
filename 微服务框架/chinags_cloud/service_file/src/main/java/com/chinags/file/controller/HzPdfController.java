package com.chinags.file.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.LoginUser;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.file.entity.*;
import com.chinags.file.service.FileUploadService;
import com.chinags.file.service.HzQzService;
import com.chinags.file.util.HzToPdf;
import com.chinags.file.util.PDFUtil;
import com.chinags.file.util.QzUtil;
import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.qiyuesuo.sdk.bean.contract.ContractDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Author : Mark_Wang
 * @Date : 2019/12/24
 **/
@Api("慧正Pdfcontroller")
@RestController
@CrossOrigin
@RequestMapping("/hz/pdf")
public class HzPdfController extends BaseController {

    protected final Logger logger = LoggerFactory.getLogger(HzPdfController.class);

    @Autowired
    private HzQzService hzQzService;

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("排版预览")
    @RequestMapping(value = "/xw", method = RequestMethod.POST)
    @ResponseBody
    public Result createXwPdf(HzTopDown hzTopDown) throws IOException, DocumentException {
        HzToPdf hzToPdf = new HzToPdf();
        String path = null;
        if (StringUtils.isNotEmpty(hzTopDown.getFlowtype())) {
            if (hzTopDown.getFlowtype().equalsIgnoreCase("chushifahan") || hzTopDown.getFlowtype().equalsIgnoreCase("zhongxinfahan")) {
                path = hzToPdf.topToPdfZXCSFH(hzTopDown);
            }
        } else {
            if (hzTopDown.getType() == 3) {
                path = hzToPdf.topToPdfDw(hzTopDown);
            } else {
                path = hzToPdf.topToPdf(hzTopDown);
            }
        }
        if (StringUtils.isEmpty(path)) {
            return new Result(false, StatusCode.ERROR, "生成pdf失败");
        } else {
            return new Result(true, StatusCode.OK, "生成pdf成功", path);
        }
    }

    @ApiOperation("公文盖章")
    @RequestMapping(value = "/xwqz/{number}", method = RequestMethod.POST)
    @ResponseBody
    public Result preXwPdf(HzTopDown hzTopDown, @PathVariable("number") String number) throws Exception {
        // 判断合同是否生成过
        HzQz hzQz = hzQzService.selectByHzId(hzTopDown.getId());
        if (qzStatus(hzQz)) {
            HzToPdf hzToPdf = new HzToPdf();
            String path = null;
            if (StringUtils.isNotEmpty(hzTopDown.getFlowtype())) {
                if (hzTopDown.getFlowtype().equalsIgnoreCase("chushifahan") || hzTopDown.getFlowtype().equalsIgnoreCase("zhongxinfahan")) {
                    path = hzToPdf.topToPdfZXCSFH(hzTopDown);
                }
            } else {
                if (hzTopDown.getType() == 3) {
                    path = hzToPdf.topToPdfDw(hzTopDown);
                } else {
                    path = hzToPdf.topToPdf(hzTopDown);
                }
            }
            if (StringUtils.isEmpty(path)) {
                return new Result(false, StatusCode.ERROR, "生成pdf失败");
            } else {
                return getResult(hzQz, path, hzTopDown.getTitle(), hzTopDown.getId(), number, 1);
            }
        } else {
            return getResult(hzQz, number);
        }
    }

    @ApiOperation("Word公文盖章")
    @RequestMapping(value = "/xwqzWord/{number}", method = RequestMethod.POST)
    @ResponseBody
    public Result preXwWord(HzWord hzWord, @PathVariable("number") String number) throws Exception {
        // 判断合同是否生成过
        HzQz hzQz = hzQzService.selectByHzId(hzWord.getId());
        if (qzStatus(hzQz)) {
            int pageSize = 2;//PDFUtil.getWordPage(hzWord.getPath());
            return getResult(hzQz, hzWord.getPath(), hzWord.getTitle(), hzWord.getId(), number, pageSize);
        } else {
            return getResult(hzQz, number);
        }
    }

    @ApiOperation("下载合同文档")
    @RequestMapping(value = "/qzfile/{hzId}", method = RequestMethod.GET)
    @ResponseBody
    public Result qzFileDownLoad(@PathVariable("hzId") String hzId) throws Exception {
        HzQz hzQz = hzQzService.selectByHzId(hzId);
        if (qzStatus(hzQz)) {
            return new Result(true, StatusCode.ERROR, "没有文档");
        } else {
            Long contractId = Long.parseLong(hzQz.getContractId());
            ContractDetail detail = QzUtil.detail(contractId);
            if (detail.getStatus().name().equals("COMPLETE")) {
                String path = QzUtil.downloadDoc(Long.parseLong(hzQz.getDocumentId()), hzId, "D://hzqz//");
                return new Result(true, StatusCode.OK, "下载文档成功", path);
            } else {
                return new Result(true, StatusCode.ERROR, "文档未盖章");
            }
        }
    }

    @ApiOperation("自动归档转移文件")
    @RequestMapping(value = "/moveFile", method = RequestMethod.POST)
    @ResponseBody
    public Result moveFile(@RequestBody MoveFile moveFile) throws Exception {
        try {
            String userCode = moveFile.getUserCode();
            String userName = moveFile.getUserName();
            String pid = moveFile.getPid();
            String workId = moveFile.getWorkId();
            List<Map<String, Object>> fjList = moveFile.getFjList();
            logger.info("[userCode] " + userCode);
            logger.info("[userName] " + userName);
            logger.info("[pid] " + pid);
            logger.info("[workId] " + workId);
            logger.info("[fjListJson] " + JSON.toJSONString(fjList));
            // 总页数
            int sumPageSize = 0;
            // 公文
            HzQz hzQz = hzQzService.selectByHzId(workId);
            if (!qzStatus(hzQz)) {
                Long contractId = Long.parseLong(hzQz.getContractId());
                ContractDetail detail = QzUtil.detail(contractId);
                if (detail.getStatus().name().equals("COMPLETE")) {
                    String path = QzUtil.downloadDoc(Long.parseLong(hzQz.getDocumentId()), workId, "D://hzqz//");
                    FileUpload fileUpload = new FileUpload();
                    fileUpload.setAddress(path);
                    fileUpload.setName("公文.pdf");
                    fileUpload.setPid(pid);
                    fileUpload.setCreateBy(userCode);
                    fileUpload.setCreateDate(new Date());
                    fileUpload.setUserName(userName);
                    fileUpload.setStatus(1);
                    int pageSize = 1;
                    try {
                        pageSize = PDFUtil.getPdfPage(path);
                    } catch (Exception e) {
                        logger.warn("自动归档获取公文页数异常：", e);
                    }
                    sumPageSize += pageSize;
                    logger.info("公文 " + "页数[" + pageSize + "]");
                    fileUpload.setPageSize(String.valueOf(pageSize));
                    fileUploadService.save(fileUpload);
                    logger.info("公文保存成功");
                }
            }
            // 办文单
            HzQz hzQzGz = hzQzService.selectByHzId(workId + "_gz");
            if (!qzStatus(hzQzGz)) {
                Long contractId = Long.parseLong(hzQzGz.getContractId());
                ContractDetail detail = QzUtil.detail(contractId);
                if (detail.getStatus().name().equals("COMPLETE")) {
                    String path = QzUtil.downloadDoc(Long.parseLong(hzQzGz.getDocumentId()), workId + "_gz", "D://hzqz//");
                    FileUpload fileUpload = new FileUpload();
                    fileUpload.setAddress(path);
                    fileUpload.setName("办文单.pdf");
                    fileUpload.setPid(pid);
                    fileUpload.setCreateBy(userCode);
                    fileUpload.setCreateDate(new Date());
                    fileUpload.setUserName(userName);
                    fileUpload.setStatus(1);
                    int pageSize = 1;
                    try {
                        pageSize = PDFUtil.getPdfPage(path);
                    } catch (Exception e) {
                        logger.warn("自动归档获取办文单页数异常：", e);
                    }
                    sumPageSize += pageSize;
                    logger.info("办文单 " + "页数[" + pageSize + "]");
                    fileUpload.setPageSize(String.valueOf(pageSize));
                    fileUploadService.save(fileUpload);
                    logger.info("办文单保存成功");
                }
            }
            // 附件
            if (fjList.size() > 0) {
                for (Map<String, Object> fj : fjList) {
                    String fileName = (String) fj.get("FILE_NAME");
                    String saveName = (String) fj.get("SAVE_NAME");
                    String extention = (String) fj.get("EXTENTION");
                    String savePath = (String) fj.get("SAVE_PATH");
                    FileUpload fileUpload = new FileUpload();
                    fileUpload.setAddress(savePath + "/" + saveName + "." + extention);
                    fileUpload.setName(fileName + "." + extention);
                    fileUpload.setPid(pid);
                    fileUpload.setCreateBy(userCode);
                    fileUpload.setCreateDate(new Date());
                    fileUpload.setUserName(userName);
                    fileUpload.setStatus(1);
                    int pageSize = 1;
                    if (new File(fileUpload.getAddress()).exists()) {
                        logger.info("附件[" + fileName + "] " + "地址[" + fileUpload.getAddress() + "]");
                        try {
                            if (extention.equals("pdf")) {
                                pageSize = PDFUtil.getPdfPage(fileUpload.getAddress());
                            } else if (extention.equals("doc") || extention.equals("docx")) {
                                pageSize = PDFUtil.getWordPage(fileUpload.getAddress());
                            }
                        } catch (Exception e) {
                            logger.warn("自动归档获取附件页数异常：", e);
                        }
                    }
                    sumPageSize += pageSize;
                    logger.info("附件[" + fileName + "] " + "页数[" + pageSize + "]");
                    fileUpload.setPageSize(String.valueOf(pageSize));
                    fileUploadService.save(fileUpload);
                }
                logger.info("附件保存成功");
            }
            return new Result(true, StatusCode.OK, "转移成功", sumPageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "转移失败");
        }
    }

    @ApiOperation("后台下载合同档案")
    @RequestMapping(value = "/qzOption/{hzId}", method = RequestMethod.GET)
    @ResponseBody
    public Result qzFileDown(@PathVariable("hzId") String hzId) throws Exception {
        HzQz hzQz = hzQzService.selectByHzId(hzId);
        if (qzStatus(hzQz)) {
            return new Result(true, StatusCode.ERROR, "没有签章文件文档");
        } else {
            Long contractId = Long.parseLong(hzQz.getContractId());
            ContractDetail detail = QzUtil.detail(contractId);
            String optionPath = "X://15//oa//horizon//_tmp//zlcOption//";
            File filePath = new File(optionPath);
            if (!filePath.exists()) {
                filePath.mkdir();
            }
            String path = QzUtil.downloadDoc(Long.parseLong(hzQz.getDocumentId()), hzId, optionPath);
            return new Result(true, StatusCode.OK, "下载文档成功");
        }
    }


    @ApiOperation("下载文件")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String download(HttpServletResponse response, Model model, String address, String fileName) {
        try {
            File file = new File(address);
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            InputStream fis = new BufferedInputStream(new FileInputStream(address));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "true";
    }

    @ApiOperation("中心发文稿纸pdf签章")
    @RequestMapping(value = "/zxfw/{number}", method = RequestMethod.POST)
    @ResponseBody
    public Result createGzZxfwPdfQZ(HzZxfwGz hzZxfwGz, @PathVariable("number") String number) throws Exception {
        HzQz hzQz = hzQzService.selectByHzId(hzZxfwGz.getId());
        if (qzStatus(hzQz)) {
            HzToPdf hzToPdf = new HzToPdf();
            String path = hzToPdf.gzzxfwToPdf(hzZxfwGz);
            if (StringUtils.isEmpty(path)) {
                return new Result(false, StatusCode.ERROR, "生成pdf失败");
            } else {
                return getResult(hzQz, path, hzZxfwGz.getTitle(), hzZxfwGz.getId(), number, 1);
            }
        } else {
            return getResult(hzQz, number);
        }
    }

    @ApiOperation("党委发文稿纸pdf签章")
    @RequestMapping(value = "/dwfw/{number}", method = RequestMethod.POST)
    @ResponseBody
    public Result createDwfwPdfQZ(HzDwfwGz hzDwfwGz, @PathVariable("number") String number) throws Exception {
        HzQz hzQz = hzQzService.selectByHzId(hzDwfwGz.getId());
        if (qzStatus(hzQz)) {
            HzToPdf hzToPdf = new HzToPdf();
            String path = hzToPdf.gzdwfwToPdf(hzDwfwGz);
            if (StringUtils.isEmpty(path)) {
                return new Result(false, StatusCode.ERROR, "生成pdf失败");
            } else {
                return getResult(hzQz, path, hzDwfwGz.getId(), hzDwfwGz.getId(), number, 1);
            }
        } else {
            return getResult(hzQz, number);
        }
    }

    @ApiOperation("党委收文稿纸转pdf签章")
    @RequestMapping(value = "/sw/{number}", method = RequestMethod.POST)
    @ResponseBody
    public Result createSwPdfQZ(HzDwSwGz hzDwSwGz, @PathVariable("number") String number) throws Exception {
        HzQz hzQz = hzQzService.selectByHzId(hzDwSwGz.getId());
        if (qzStatus(hzQz)) {
            HzToPdf hzToPdf = new HzToPdf();
            String path = hzToPdf.gzDwSwToPdf(hzDwSwGz);
            if (StringUtils.isEmpty(path)) {
                return new Result(false, StatusCode.ERROR, "生成pdf失败");
            } else {
                return getResult(hzQz, path, hzDwSwGz.getId(), hzDwSwGz.getId(), number, 1);
            }
        } else {
            return getResult(hzQz, number);
        }
    }

    @ApiOperation("中心收文稿纸转pdf签章")
    @RequestMapping(value = "/zxsw/{number}", method = RequestMethod.POST)
    @ResponseBody
    public Result creaateZxSwPdfQZ(HzZxSwGz hzZxSwGz, @PathVariable("number") String number) throws Exception {
        HzQz hzQz = hzQzService.selectByHzId(hzZxSwGz.getId());
        if (qzStatus(hzQz)) {
            HzToPdf hzToPdf = new HzToPdf();
            String path = hzToPdf.gzZxswToPdf(hzZxSwGz);
            if (StringUtils.isEmpty(path)) {
                return new Result(false, StatusCode.ERROR, "生成pdf失败");
            } else {
                return getResult(hzQz, path, hzZxSwGz.getId(), hzZxSwGz.getId(), number, 1);
            }
        } else {
            return getResult(hzQz, number);
        }
    }

    @ApiOperation("中心发文稿纸pdf")
    @RequestMapping(value = "/zxfw", method = RequestMethod.POST)
    @ResponseBody
    public Result createGzZxfwPdf(HzZxfwGz hzZxfwGz) throws IOException, DocumentException {
        HzToPdf hzToPdf = new HzToPdf();
        String path = hzToPdf.gzzxfwToPdf(hzZxfwGz);
        if (StringUtils.isEmpty(path)) {
            return new Result(false, StatusCode.ERROR, "生成pdf失败");
        } else {
            return new Result(true, StatusCode.OK, "生成pdf成功", path);
        }
    }

    @ApiOperation("党委发文稿纸pdf")
    @RequestMapping(value = "/dwfw", method = RequestMethod.POST)
    @ResponseBody
    public Result createDwfwPdf(HzDwfwGz hzDwfwGz) throws IOException, DocumentException {
        HzToPdf hzToPdf = new HzToPdf();
        String path = hzToPdf.gzdwfwToPdf(hzDwfwGz);
        if (StringUtils.isEmpty(path)) {
            return new Result(false, StatusCode.ERROR, "生成pdf失败");
        } else {
            return new Result(true, StatusCode.OK, "生成pdf成功", path);
        }
    }

    @ApiOperation("党委收文稿纸转pdf")
    @RequestMapping(value = "/sw", method = RequestMethod.POST)
    @ResponseBody
    public Result createSwPdf(HzDwSwGz hzDwSwGz) throws IOException, DocumentException {
        HzToPdf hzToPdf = new HzToPdf();
        String path = hzToPdf.gzDwSwToPdf(hzDwSwGz);
        if (StringUtils.isEmpty(path)) {
            return new Result(false, StatusCode.ERROR, "生成pdf失败");
        } else {
            return new Result(true, StatusCode.OK, "生成pdf成功", path);
        }
    }

    @ApiOperation("中心收文稿纸转pdf")
    @RequestMapping(value = "/zxsw", method = RequestMethod.POST)
    @ResponseBody
    public Result creaateZxSwPdf(HzZxSwGz hzZxSwGz) throws IOException, DocumentException {
        HzToPdf hzToPdf = new HzToPdf();
        String path = hzToPdf.gzZxswToPdf(hzZxSwGz);
        if (StringUtils.isEmpty(path)) {
            return new Result(false, StatusCode.ERROR, "生成pdf失败");
        } else {
            return new Result(true, StatusCode.OK, "生成pdf成功", path);
        }
    }

    @ApiOperation("通过word文件生成签章页面")
    @RequestMapping(value = "/upfileqz/{number}", method = RequestMethod.GET)
    @ResponseBody
    public Result upFileQz(String path, String hzId, @PathVariable("number") String number) {
        HzQz hzQz = hzQzService.selectByHzId(hzId);
        if (hzQz == null) {
            return getResult(hzQz, path, hzId, hzId, number, 1);
        } else {
            String url = null;
            try {
                Long documentId = QzUtil.createFile(path, hzId);
                List<Long> documents = new ArrayList<>();
                documents.add(documentId);
                Long contractId = QzUtil.createContract(documents, hzId, number);
                hzQz.setDocumentId(String.valueOf(documentId));
                hzQz.setContractId(String.valueOf(contractId));
                hzQzService.save(hzQz);
                url = QzUtil.signUrl(contractId, number);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (url == null) {
                    return new Result(true, StatusCode.ERROR, "签章页面生成失败");
                } else {
                    return new Result(true, StatusCode.OK, "签章页面生成成功", url);
                }
            }
        }
    }

    /**
     * 根据获取的流程合同信息获取签章页面
     * @param hzQz
     * @return
     */
    private Result getResult(HzQz hzQz, String number) {
        String url = null;
        try {
            url = QzUtil.signUrl(Long.parseLong(hzQz.getContractId()), number);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (url == null) {
                return new Result(true, StatusCode.ERROR, "签章页面生成失败");
            } else {
                return new Result(true, StatusCode.OK, "签章页面生成成功", url);
            }
        }
    }

    /**
     * 根据文件路径重新生成合同文档返回签章页面
     * @param path
     * @param title
     * @param id
     * @return
     */
    private Result getResult(HzQz hzQz, String path, String title, String id, String number, int pageSize) {
        String url = null;
        try {
            Long documentId = QzUtil.createFile(path, title);
            List<Long> documents = new ArrayList<>();
            documents.add(documentId);
            Long contractId = QzUtil.createContract(documents, id, number);
            HzQz hzQz1 = new HzQz();
            if (hzQz != null) {
                hzQz1.setId(hzQz.getId());
            }
            hzQz1.setHzId(id);
            hzQz1.setDocumentId(String.valueOf(documentId));
            hzQz1.setContractId(String.valueOf(contractId));
            hzQz1.setPageSize(String.valueOf(pageSize));
            hzQzService.save(hzQz1);
            url = QzUtil.signUrl(contractId, number);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (url == null) {
                return new Result(true, StatusCode.ERROR, "签章页面生成失败");
            } else {
                return new Result(true, StatusCode.OK, "签章页面生成成功", url);
            }
        }
    }

    /**
     * 保存慧正上传的Word版办文单
     * @param number 慧正用户登陆名
     */
    @ResponseBody
    @RequestMapping(value = "wordUpload")
//    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public Result wordUpload(@RequestParam("file") MultipartFile[] file, String id, String number, HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json; charset=UTF-8");
        String url = null;
        try {
            if(file.length > 0){
                //设置存储的文件全限定名
                String fileName = "D:\\banwendanmuban\\wordupload\\" +
                        UUID.randomUUID().toString().replaceAll("-", "") + ".docx";
                File newFile  = new File(fileName);
                newFile.createNewFile();
                file[0].transferTo(newFile);

                //通过word文件生成签章页面
                HzQz hzQz = hzQzService.selectByHzId(id);
                if (hzQz == null) {
                    url = getResult(hzQz, fileName, id, id, number, 1).getData().toString();
                }else {
                    Long documentId = QzUtil.createFile(fileName, id);
                    List<Long> documents = new ArrayList<>();
                    documents.add(documentId);
                    Long contractId = QzUtil.createContract(documents, id, number);
                    hzQz.setDocumentId(String.valueOf(documentId));
                    hzQz.setContractId(String.valueOf(contractId));
                    hzQzService.save(hzQz);
                    url = QzUtil.signUrl(contractId, number);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if (url == null) {
                return new Result(true, StatusCode.ERROR, "签章页面生成失败");
            } else {
                return new Result(true, StatusCode.OK, "签章页面生成成功", url);
            }
        }
    }

    @ApiOperation("通过慧正workId获取附件公文的页数")
    @RequestMapping(value = "/pageSize/{hzId}", method = RequestMethod.GET)
    @ResponseBody
    public Result getPageSize(@PathVariable("hzId") String hzId) {
        HzQz hzQz = hzQzService.selectByHzId(hzId);
        if (hzQz == null || StringUtils.isEmpty(hzQz.getPageSize()))
            return new Result(false, StatusCode.ERROR, "没有页数", "0");
        else {
            return new Result(true, StatusCode.OK, "获取成功", hzQz.getPageSize());
        }
    }

    /**
     * 判断合同是否签章过
     * @param hzQz
     * @return
     * @throws Exception
     */
    private boolean qzStatus(HzQz hzQz) throws Exception {
        boolean isQZ = true;
        if (hzQz != null) {
            ContractDetail contractDetail = QzUtil.detail(Long.parseLong(hzQz.getContractId()));
            if (contractDetail.getStatus().equals("COMPLETE") || contractDetail.getStatusDesc().equalsIgnoreCase("已完成")) {
                isQZ = false;
            }
        }
        return isQZ;
    }

}
