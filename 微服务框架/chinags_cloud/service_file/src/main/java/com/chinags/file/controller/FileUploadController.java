package com.chinags.file.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.io.FileUtils;
import com.chinags.common.utils.StringUtils;
import com.chinags.file.entity.Base64;
import com.chinags.file.entity.FileUpload;
import com.chinags.file.service.FileUploadService;
import com.chinags.file.util.PDFUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mark_Wang
 * @date 2019/7/23
 **/
@Api("文件上传controller")
@RestController
@CrossOrigin
@RequestMapping("/file")
@SuppressWarnings("all")
public class FileUploadController extends BaseController {

    private String savePath = "E:/fileUpload/upload/";
    private String tempPath = "E:/fileUpload/temp/";
    private HzPdfController hzPdfController;

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("上传文件查询")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public Result find(String pid, int num) {
        String userCode = getLoginUser().getUsercode();
        List<FileUpload> files = new ArrayList<>();
        if (pid.length() < 20) {
            if (num == 0) {
                File temp = new File(tempPath + userCode);
                temp.delete();
                // 清理上次未保存的上传文件
                List<FileUpload> fileUploads = fileUploadService.findByCreateBy(pid, userCode);
                for (FileUpload fileUpload : fileUploads) {
                    File file = new File(fileUpload.getAddress());
                    if (file.exists()) {
                        file.delete();
                    }
                    if (StringUtils.isNotEmpty(fileUpload.getPdfAddress())) {
                        File pdf = new File(fileUpload.getPdfAddress());
                        if (pdf.exists()) {
                            pdf.delete();
                        }
                    }
                }
                fileUploadService.deleteByCreateBy(pid, userCode);
            }
            files = fileUploadService.findByCreateBy(pid, userCode);
        } else {
            files = fileUploadService.getByPid(pid);
        }
        return new Result(true, StatusCode.OK, "查询成功", files);
    }

    @ApiOperation("删除文件")
    @RequestMapping(value = "del", method = RequestMethod.GET)
    public Result del(String id) {
        FileUpload fileUpload = fileUploadService.getById(id);
        File file = new File(fileUpload.getAddress());
        if (file.exists()) {
            file.delete();
        }
        if (StringUtils.isNotEmpty(fileUpload.getPdfAddress())) {
            File pdf = new File(fileUpload.getPdfAddress());
            if (pdf.exists()) {
                pdf.delete();
            }
        }
        fileUploadService.delById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @ApiOperation("下载文件")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String download(HttpServletResponse response, Model model, String id) {
        try {
            FileUpload fileUpload = fileUploadService.getById(id);
            File file = new File(fileUpload.getAddress());
            String filename = fileUpload.getName();
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            InputStream fis = new BufferedInputStream(new FileInputStream(fileUpload.getAddress()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
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

    /**
     * 下载PDF水印文件（能下PDF则下，下不了PDF下载原文件），主要提供给jeesite下载文件使用
     *
     * @param id 文件id
     */
    @ApiOperation("下载PDF水印文件")
    @RequestMapping(value = "/download2", method = RequestMethod.GET)
    public void download2(HttpServletResponse response, Model model, String id) {
        //记录pdf水印文件全限定名
        String newPdf = "";
        //设置水印内容（下载用户id和当前时间）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //水印内容：下载用户id+下载时间
        //        String userCode = getLoginUser().getUsercode();  //本机测试，虽然手动输入taken，但无法进行登录验证
        String userCode = "system";  //无法进行登录验证，测试使用
        String shuiyin = userCode + "  " + sdf.format(new Date());
        try {
            //记录要下载的文件全限定名和原文件名
            String filepath = "";
            //获取文件信息
            FileUpload fileUpload = fileUploadService.getById(id);
            String filename = fileUpload.getName();  //原文件名
            String pdfAddress = fileUpload.getPdfAddress();  //文件PDF地址
            if (StringUtils.isNotEmpty(pdfAddress)) {  //有PDF地址，说明原文件转换成了PDF文件
                /*
                给PDF文件打水印
                 */
                //设置水印PDF文件名称
                newPdf = savePath + UUID.randomUUID().toString().replaceAll("-", "") + ".pdf";
                PDFUtil.makePdfShuiyin(pdfAddress, newPdf, shuiyin);
                filepath = newPdf;
                filename = filename.substring(0, filename.lastIndexOf(".")) + ".pdf";
            } else {  //无法转化PDF文件的，下载原文件
                filepath = fileUpload.getAddress();  //原文件地址
            }
            //开始下载
            File file = new File(filepath);
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            InputStream fis = new BufferedInputStream(new FileInputStream(filepath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //删除pdf水印文件
            if (StringUtils.isNotEmpty(newPdf)) {
                File file = new File(newPdf);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 勾选的卷内文件记录是否有关联附件进行下载
     *
     * @param ids 勾选的卷内文件id，用“,”分割
     */
    @ApiOperation("下载PDF水印文件")
    @RequestMapping(value = "/canDownZip", method = RequestMethod.GET)
    public String canDownZip(String ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] idlist = ids.split(",");
        //记录要进行下载的附件
        List<FileUpload> fileUploadList = Lists.newArrayList();
        for (String pid : idlist) {
            FileUpload fileUpload = new FileUpload();
            //获取关联的附件list
            List<FileUpload> fileList = fileUploadService.getByPid(pid);
            //如果存在附件，存入fileUploadList进行记录
            if (fileList.size() > 0) {
                for (FileUpload f : fileList) {
                    fileUploadList.add(f);
                }
            }
        }
        //没有附件，对前台用户进行提示
        if (fileUploadList.size() < 1) {
            return null;
        } else {
            //有的话随便传一个非空字符串，前台会调用下载方法
            return "success";
        }
    }

    /**
     * 打包下载用户勾选的记录所有关联的附件（PDF水印格式）
     *
     * @param ids    勾选的卷内文件id，用“,”分割
     * @param userid 登录用户id，打水印使用
     */
    @ApiOperation("下载PDF水印文件")
    @RequestMapping(value = "/downZip", method = RequestMethod.GET)
    public void downZip(String ids, String userid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] idlist = ids.split(",");
        //记录要进行下载的附件
        List<FileUpload> fileUploadList = Lists.newArrayList();
        for (String pid : idlist) {
            FileUpload fileUpload = new FileUpload();
            //获取关联的附件list
            List<FileUpload> fileList = fileUploadService.getByPid(pid);
            //如果存在附件，存入fileUploadList进行记录
            if (fileList.size() > 0) {
                for (FileUpload f : fileList) {
                    fileUploadList.add(f);
                }
            }
        }
        //没有附件，对前台用户进行提示
        if (fileUploadList.size() < 1) {
            return;
        }
        /*
        有附件，进行打包下载
         */
        //新建一个目录，将要下载的文件复制到该目录下，最后打包这个目录下的所有内容
        String bagpath = savePath + UUID.randomUUID().toString().replaceAll("-", "") + File.separator;
        File bag = new File(bagpath);
        if (!bag.exists()) {
            bag.mkdirs();
        }

        //准备待打包的文件，能打水印的打水印，不能打水印的下载原文件
        for (FileUpload fileUpload : fileUploadList) {
            String pdfAddress = fileUpload.getPdfAddress();
            if (StringUtils.isNotEmpty(pdfAddress)) {  //PDF地址不为空，说明有PDF文件，要打水印
                //设置水印内容（下载用户id和当前时间）
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                //水印内容：下载用户id+下载时间
                String shuiyin = userid + "  " + sdf.format(new Date());
                //设置水印PDF文件路径
                String newPdf = bagpath + UUID.randomUUID().toString().replaceAll("-", "") + ".pdf";
                PDFUtil.makePdfShuiyin(pdfAddress, newPdf, shuiyin);
            } else {  //无法转换PDF的文件，下载原文件
                String address = fileUpload.getAddress();
                FileUtils.copyFile2(address, bagpath);
            }
        }
        /*目标目录创建完毕*/

        try {
            //设置下载zip名字
            String zipName = "批量下载";
            //打包
            FileUtils.fileToZip(bagpath, bagpath, zipName);
            zipName += ".zip";
            /*
            下载zip
             */
            //获取zip全限定名
            String zipFullPath = bagpath + zipName;
            File zipFile = new File(zipFullPath);
            if (zipFile.exists()) {
                zipName = new String(zipName.getBytes("UTF-8"), "ISO-8859-1");
                InputStream fis = new BufferedInputStream(new FileInputStream(zipFullPath));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                response.reset();
                response.addHeader("Content-Disposition", "attachment;filename=" + zipName);
                response.addHeader("Content-Length", "" + zipFile.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/zip");// 定义输出类型
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("打包下载失败!");
        } finally {
            //删除要打包的文件夹和zip
            File file = new File(bagpath);
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                } else {
                    File[] files = file.listFiles();
                    for (File f : files) {
                        FileUtils.delFile(f);
                    }
                    file.delete();  //将空目录删除
                }
            }
        }
    }

    @ApiOperation("下载文件外部调用")
    @RequestMapping(value = "/down", method = RequestMethod.POST)
    public ResponseEntity<byte[]> down(String id) throws Exception {
        FileUpload fileUpload = fileUploadService.getById(id);
        File file = new File(fileUpload.getAddress());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment", java.net.URLEncoder.encode(fileUpload.getName(), "UTF-8"));
        httpHeaders.setContentType(MediaType.parseMediaType("application/octet-stream"));
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation("根据pid下载文件，供主题分类的图片下载显示")
    @RequestMapping(value = "/pid", method = RequestMethod.GET)
    public String downByPid(String pid) throws Exception {
        List<FileUpload> fileUploads = fileUploadService.getByPid(pid);
        // 判断是否上传过文件
        if (CollectionUtils.isEmpty(fileUploads)) {
            return null;
        }
        // 如果上传过，通过pid下载的时候只下载第一个文件
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(fileUploads.get(0).getAddress());
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            return null;
        }
        // 加编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    @ApiOperation("预览文件")
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public ResponseEntity<byte[]> preview(HttpServletResponse response, String path) throws Exception {
        File file = new File(path);
        HttpHeaders httpHeaders = new HttpHeaders();
        String fileName = file.getName();
        httpHeaders.setContentDispositionFormData("attachment", java.net.URLEncoder.encode(fileName, "UTF-8"));
        httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation("为文件上传的数据增加pid")
    @RequestMapping(value = "/updatePid/{pid}/{opid}", method = RequestMethod.GET)
    public Result updatePid(@PathVariable("pid") String pid, @PathVariable("opid") String opid) {
        String userCode = getLoginUser().getUsercode();
        if (StringUtils.isEmpty(opid))
            fileUploadService.updatePid(pid, userCode);
        else
            fileUploadService.updatePidByPid(pid, opid, userCode);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("清理上次未保存的上传文件数据")
    @RequestMapping(value = "/delete/{pid}", method = RequestMethod.POST)
    public Result deleteByCreateBy(@PathVariable("pid") String pid) {
        String userCode = getLoginUser().getUsercode();
        List<FileUpload> fileUploads = fileUploadService.findByCreateBy(pid, userCode);
        for (FileUpload fileUpload : fileUploads) {
            File file = new File(fileUpload.getAddress());
            if (file.exists()) {
                file.delete();
            }
        }
        fileUploadService.deleteByCreateBy(pid, userCode);
        return new Result(true, StatusCode.OK, "清理成功");
    }

    /**
     * 文件上传上传状态验证
     *
     * @param request
     * @param response
     * @param model
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public void save(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, InterruptedException {
        //        String userCode = getLoginUser().getUsercode();
        String userCode = "";
        //userId有值时，是从jeesite上传附件
        String userId = request.getParameter("userId");
        if (StringUtils.isNotEmpty(userId)) {
            userCode = userId;
        } else {  //否则是从微服务上传附件
            userCode = getLoginUser().getUsercode();
        }
        String action = request.getParameter("action");
        String fileMd5 = userCode + "/" + request.getParameter("fileMd5");
        if (action.equals("mergeChunks")) {
            String fileName = request.getParameter("fileName");
            String pid = request.getParameter("pid");
            String[] ext = fileName.split("\\.", -1);
            String ex = ext[ext.length - 1];
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //生成的保存文件名
            String uuidName = uuid + "." + ex;
            //生成pdf预览的文件名
            String pdfName = uuid + ".pdf";
            String pdfPath = savePath + userCode + "/pdf/";
            File pdfFile = new File(pdfPath);
            //验证pdf文件路径是否存在，不存在创建
            if (!pdfFile.exists()) {
                pdfFile.mkdirs();
            }
            String outPdf = pdfPath + pdfName;
            // 合并文件
            // 需要合并的文件的目录标记
            // 读取目录里的所有文件
            File f = new File(tempPath + fileMd5);
            File[] fileArray = f.listFiles(new FileFilter() {
                // 排除目录只要文件
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return false;
                    }
                    return true;
                }
            });
            // 转成集合，便于排序
            List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2
                            .getName())) {
                        return -1;
                    }
                    return 1;
                }
            });
            // UUID.randomUUID().toString()-->随机名
            //            String outFilePath = savePath + getLoginUser().getUsercode() + "/" + uuidName;
            String outFilePath = savePath + userCode + "/" + uuidName;
            FileUpload fileUpload = new FileUpload();
            fileUpload.setAddress(outFilePath);
            fileUpload.setName(fileName);
            fileUpload.setPid(pid);
            fileUpload.setCreateBy(userCode);
            fileUpload.setCreateDate(new Date());
            String userName = request.getParameter("userName");
            if (StringUtils.isNotEmpty(userName)) {
                fileUpload.setUserName(userName);
            } else {
                fileUpload.setUserName(getLoginUser().getUsername());
            }
            String[] exts = new String[]{"txt", "jpg", "jpeg", "bmp", "png", "pdf", "doc", "docx", "xls", "xlsx"};
            boolean flag = Arrays.asList(exts).contains(ex);
            if (flag) {
                fileUpload.setStatus(0);
                if (ex.equals("pdf")) {
                    fileUpload.setPdfAddress(outFilePath);
                    fileUpload.setPageSize(String.valueOf(PDFUtil.getPdfPage(outFilePath)));
                } else {
                    fileUpload.setPdfAddress(outPdf);
                }
            } else {
                fileUpload.setStatus(1);
            }
            File outputFile = new File(outFilePath);
            // 创建文件
            outputFile.createNewFile();
            // 输出流
            FileChannel outChnnel = new FileOutputStream(outputFile).getChannel();
            // 合并
            FileChannel inChannel;
            for (File file : fileList) {
                inChannel = new FileInputStream(file).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChnnel);
                inChannel.close();
                // 删除分片
                file.delete();
            }
            outChnnel.close();
            // 清除文件夹
            File tempFile = new File(tempPath + "/" + fileMd5);
            if (tempFile.isDirectory() && tempFile.exists()) {
                tempFile.delete();
            }
            switch (ex) {
                case "jpg":
                case "jpeg":
                case "bmp":
                case "png":
                    PDFUtil.Img2PDF(outFilePath, outPdf);
                    break;
                case "txt":
                    PDFUtil.text2pdf(outFilePath, outPdf);
                    break;
                case "doc":
                case "docx":
                    PDFUtil.word2pdf(outFilePath, outPdf);
                    fileUpload.setPageSize(String.valueOf(PDFUtil.getWordPage(outFilePath)));
                    break;
                /*case "ppt":
                case "pptx":
                    PDFUtil.ppt2pdf(outFilePath, outPdf);
                    break;*/
                case "xls":
                case "xlsx":
                    PDFUtil.excel2pdf(outFilePath, outPdf);
                    break;
                default:
                    break;
            }
            fileUploadService.save(fileUpload);
        } else if (action.equals("checkChunk")) {
            // 检查当前分块是否上传成功
            String chunk = request.getParameter("chunk");
            String chunkSize = request.getParameter("chunkSize");
            if (chunk.equals("0") && (Integer.parseInt(chunkSize) < 10 * 1024 * 1024))
                chunk = "null";
            File checkFile = new File(tempPath + "/" + fileMd5 + "/" + chunk);
            response.setContentType("text/html;charset=utf-8");
            // 检查文件是否存在，且大小是否一致
            if (checkFile.exists() && checkFile.length() == Integer.parseInt(chunkSize)) {
                // 上传过
                response.getWriter().write("{\"ifExist\":1}");
            } else {
                // 没有上传过
                response.getWriter().write("{\"ifExist\":0}");
            }
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userCode = "";
        //userId有值时，是从jeesite上传附件
        String userId = request.getParameter("userId");
        if (StringUtils.isNotEmpty(userId)) {
            userCode = userId;
        } else {  //否则是从微服务上传附件
            userCode = getLoginUser().getUsercode();
        }
        //        String fileMd5 = getLoginUser().getUsercode() + "/" + request.getParameter("fileMd5");
        String fileMd5 = userCode + "/" + request.getParameter("fileMd5");
        String chunk = request.getParameter("chunk");
        //判断存放上传文件的目录是否存在（不存在则创建）
        //        File f = new File(savePath + getLoginUser().getUsercode());
        File f = new File(savePath + userCode);
        if (!f.exists()) {
            f.mkdirs();
        }
        //判断临时目录是否存在（不存在则创建）
        File f1 = new File(tempPath + fileMd5);
        if (!f1.exists()) {
            f1.mkdirs();
        }
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            for (MultipartFile fil : files) {
                File file = new File(tempPath + fileMd5);
                if (!file.exists()) {
                    file.mkdir();
                }
                File chunkFile = new File(tempPath + fileMd5 + "/" + chunk);
                fil.transferTo(chunkFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/uploadBase64")
    public Result uploadBase64(@RequestBody Base64 base64) throws IOException {

        String username = base64.getUserName();
        String userCode = base64.getUserCode();
        String imgBase64 = base64.getImgBase64();

        String excension = "";
        if (imgBase64.contains("data:image/jpeg;base64,")) {
            excension = "jpeg";
            imgBase64 = imgBase64.replace("data:image/jpeg;base64,", "");
        } else if (imgBase64.contains("data:image/png;base64,")) {
            excension = "png";
            imgBase64 = imgBase64.replace("data:image/png;base64,", "");
        } else if (imgBase64.contains("data:image/jpg;base64,")) {
            excension = "jpg";
            imgBase64 = imgBase64.replace("data:image/jpg;base64,", "");
        }

        String destPath = savePath + userCode + "/" + UUID.randomUUID() + "." + excension;

        File f = new File(savePath + userCode);
        if (!f.exists()) {
            f.mkdirs();
        }

        File tempFile = new File(tempPath + UUID.randomUUID() + "." + excension);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }

        try {

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decoderBytes = decoder.decodeBuffer(imgBase64);

            FileOutputStream fis = new FileOutputStream(tempFile);
            fis.write(decoderBytes);
            fis.close();

            File destFile = new File(destPath);
            FileUtils.moveFile(tempFile, destFile);

            FileUpload fileUpload = new FileUpload();
            fileUpload.setAddress(destPath);
            fileUpload.setName(destFile.getName());
            fileUpload.setPid(userCode);
            fileUpload.setCreateBy(userCode);
            fileUpload.setCreateDate(new Date());
            fileUpload.setUserName(username);
            fileUpload.setStatus(1);
            fileUploadService.save(fileUpload);

            return new Result(true, StatusCode.OK, "上传成功", fileUpload);

        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "上传失败");
        } finally {
            FileUtils.deleteQuietly(tempFile);
        }

    }

    @ApiOperation("删除跟pid关联的所有文件记录")
    @RequestMapping(value = "/deleteFileListByPid", method = RequestMethod.GET)
    public void deleteFileListByPid(String pid) {
        List<FileUpload> fpList = fileUploadService.getByPid(pid);
        for (FileUpload fu : fpList) {
            String id = fu.getId();
            String address = fu.getAddress();
            String pdfAddress = fu.getPdfAddress();
            //删除上传的原文件及PDF文件
            if (StringUtils.isNotEmpty(address)) {
                File file = new File(address);
                if (file.exists()) {
                    file.delete();
                }
            }
            if (StringUtils.isNotEmpty(pdfAddress)) {
                File file = new File(pdfAddress);
                if (file.exists()) {
                    file.delete();
                }
            }
            fileUploadService.delById(id);
        }
    }

    @ApiOperation("根据pid查找上传附件list")
    @RequestMapping(value = "/fileuploadlistByPid", method = RequestMethod.GET)
    public List<FileUpload> fileuploadlistByPid(String pid) {
        return fileUploadService.getByPid(pid);
    }


    /**
     * todo 模拟接收流程转档案中的 文件+参数
     *
     * @param files @RequestParam("file")，file是前台消息头中的参数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/liuchengChangeToDangan", method = RequestMethod.POST)
    public String getInfo(@RequestParam("file") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            //测试接收参数
            String pid = request.getParameter("pid");
            String testval = request.getParameter("testval");
            String testval2 = request.getParameter("testval2");
            String testval3 = request.getParameter("testval3");  //没传值，打印null
            /*System.out.println(pid);
            System.out.println(testval);
            System.out.println(testval2);
            System.out.println(testval3);*/


            /*
            检查是否有存放的文件夹
             */
            //        private String savePath = "E:/fileUpload/upload/";
            //设置文件夹路径： savePath+userCode
            //        String userCode = getLoginUser().getUsercode();  //本机测试，虽然手动输入taken，但无法进行登录验证
            String userCode = "system";  //无法进行登录验证，测试使用
            String path = savePath + userCode;  //设置文件存放路径
            //创建文件存放目录（同时要检查pdf目录是否存在）
            File packageFile = new File(path);
            if (!packageFile.exists()) {
                packageFile.mkdirs();
            }
            File pdfPackageFile = new File(path + "/pdf");
            if (!pdfPackageFile.exists()) {
                pdfPackageFile.mkdirs();
            }

            //将上传的文件放到测试文件夹内
            for (MultipartFile file : files) {
                //获取文件尾缀，判断文件类型
                String weizhui = file.getOriginalFilename().lastIndexOf(".") != -1 ? file.getOriginalFilename().
                        substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length()) : null;

                //            System.out.println(file.getName());  //得到@RequestParam("file")中的参数file
                String oldFileName = file.getOriginalFilename();  //得到上传的文件名，用于保存数据库中数据
                System.out.println(oldFileName);

                //设置新的文件名
                String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + weizhui;
                String filePath = path + "/" + fileName;
                filePath = "d:/123" +fileName;
                File newFile = new File(filePath);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                file.transferTo(newFile);  //文件保存完毕

                //设置PDF文件全限定名
                String pdfName = path + "/pdf/" + UUID.randomUUID().toString().replaceAll("-", "") + ".pdf";
                //可转PDF文件尾缀
                String[] exts = new String[]{"txt", "jpg", "jpeg", "bmp", "png", "pdf", "doc", "docx", "xls", "xlsx"};
                boolean flag = Arrays.asList(exts).contains(weizhui);  //判断该类型文件是否可转PDF
                switch (weizhui) {
                    case "jpg":
                    case "jpeg":
                    case "bmp":
                    case "png":
                        PDFUtil.Img2PDF(filePath, pdfName);
                        break;
                    case "txt":
                        PDFUtil.text2pdf(filePath, pdfName);
                        break;
                    case "doc":
                    case "docx":
                        PDFUtil.word2pdf(filePath, pdfName);
                        break;
                /*case "ppt":
                case "pptx":
                    PDFUtil.ppt2pdf(outFilePath, outPdf);
                    break;*/
                    case "xls":
                    case "xlsx":
                        PDFUtil.excel2pdf(filePath, pdfName);
                        break;
                    default:
                        break;
                }

                 /*
                向数据库插入数据
                 */
                FileUpload fileUpload = new FileUpload();
                fileUpload.setPid(pid);  //设置pid
                fileUpload.setName(oldFileName);  //设置原文件名
                fileUpload.setAddress(filePath);  //设置文件地址
                fileUpload.setPdfAddress(pdfName);  //设置PDF地址
                fileUpload.setStatus(0);
                /*fileUpload.setCreateBy();
                fileUpload.setCreateDate();
                fileUpload.setUpdateBy();
                fileUpload.setUpdateDate();*/
                fileUploadService.save(fileUpload);
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @ApiOperation("根据ID修改文件页数")
    @RequestMapping(value = "/upsize/{id}/{pageSize}", method = RequestMethod.GET)
    public Result updatePageSizeById(@PathVariable("id") String id, @PathVariable("pageSize") String pageSize) {
        fileUploadService.updatePageSizeById(id, pageSize);
        return new Result(true, StatusCode.OK, "修改成功");
    }

}
