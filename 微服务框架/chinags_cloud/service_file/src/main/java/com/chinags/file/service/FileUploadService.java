package com.chinags.file.service;

import com.chinags.file.dao.FileUploadDao;
import com.chinags.file.entity.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/26
 **/
@Service
public class FileUploadService {

    @Autowired
    private FileUploadDao fileUploadDao;

    /**
     * 根据pid查询存储文件列表
     * @param pid
     * @return
     */
    public List<FileUpload> getByPid(String pid) {
        return fileUploadDao.getByPid(pid);
    }

    /**
     * 文件存储
     * @param fileUpload
     * @return
     */
    public FileUpload save(FileUpload fileUpload) {
        return fileUploadDao.save(fileUpload);
    }

    /**
     * 根据id删除文件
     * @param id
     */
    public void delById(String id) {
        fileUploadDao.deleteById(id);
    }

    /**
     * 根据id获取文件删除数据
     * @param id
     * @return
     */
    public FileUpload getById(String id) {
        return fileUploadDao.getById(id);
    }

    /**
     * 根据登录用户修改pid
     * @param pid
     * @param createBy
     */
    public void updatePid(String pid, String createBy) {
        fileUploadDao.updatePid(pid, createBy);
    }

    /**
     * 根据登录用户和默认的pid修改pid
     * @param pid pid
     * @param opid 默认pid
     * @param createBy 登录用户
     */
    public void updatePidByPid(String pid, String opid, String createBy) {
        fileUploadDao.updatePidByPid(pid, opid, createBy);
    }

    /**
     * 根据登录用户删除上次未保存的上传文件数据
     * @param createBy
     */
    public void deleteByCreateBy(String pid, String createBy) {
        fileUploadDao.deleteByCreateBy(pid, createBy);
    }

    /**
     * 根据createBy获取为保存的所有数据
     * @param createBy
     * @return
     */
    public List<FileUpload> findByCreateBy(String pid, String createBy) {
        return fileUploadDao.findByCreateBy(pid, createBy);
    }

    /**
     * 根据id修改文件页数
     * @param id
     * @param pageSize
     */
    public void updatePageSizeById(String id, String pageSize) {
        fileUploadDao.updatePageSizeById(id, pageSize);
    }
}
