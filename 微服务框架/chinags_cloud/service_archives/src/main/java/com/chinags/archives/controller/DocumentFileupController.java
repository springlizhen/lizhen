package com.chinags.archives.controller;

import com.chinags.archives.entity.DocumentFileup;
import com.chinags.archives.service.DocumentFileupService;
import com.chinags.common.controller.BaseController;
import com.chinags.common.io.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * @author XieWenqing
 * @date 2019/11/5 下午 2:11
 */
@Api("文档附件下载controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/fileDownZip")
public class DocumentFileupController extends BaseController {
    @Autowired
    private DocumentFileupService documentFileupService;

    @ApiOperation("下载卷内文件附件zip")
    @RequestMapping(value="{id}",method = RequestMethod.GET)
    public String fileDownZip(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response){
        //记录文件夹路径，将所有被下载的文件复制到这个文件夹中，最后打包这个文件夹
        String bagpath = "D:/jeesite文件上传/" + UUID.randomUUID().toString().replaceAll("-", "") + File.separator;
        try{
            List<DocumentFileup> dfList = documentFileupService.findDocumentFileupList(id);
            //如果该卷内文件下没有附件，提示前台用户
            if(dfList.size() < 1){
                return "null";
            }
            /*
            能查询到附件，进行打包下载
             */
            //创建要进行打包的文件夹
            File bag = new File(bagpath);
            if (!bag.exists()) {
                bag.mkdirs();
            }

            for(DocumentFileup df :dfList){
                //将文件拷贝到目标路径
                FileUtils.copyFile2(df.getAddress() + df.getFileName(), bagpath);
            }
            //设置下载zip名字
            String zipName = "档案附件";
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

        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }finally {
            //删除文件
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
        return "success";
    }
}
