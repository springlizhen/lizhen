package com.chinags.archives.service;

import com.chinags.archives.dao.DocumentPermissionDao;
import com.chinags.archives.entity.DocumentPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XieWenqing
 * @date 2019/11/21 下午 5:31
 */
@Service
public class DocumentPermissionService {
    @Autowired
    private DocumentPermissionDao documentPermissionDao;

    /**
     * 当前用户是否能查看该文档，返回1能看0不能看
     * @param userCode 用户id
     * @param cdfId 文档id
     */
    public Integer cansee(String userCode, String cdfId){
        return documentPermissionDao.cansee(userCode, cdfId);
    }

    public void deleteListByCdfId(String cdfId){
        documentPermissionDao.deleteListByCdfId(cdfId);
    }

    /**
     * 保存权限
     */
    public void save(DocumentPermission documentPermission) {
        documentPermissionDao.save(documentPermission);
    }

    public Integer countLoginUser(String userCode, String cdfId){
        return documentPermissionDao.countLoginUser(userCode, cdfId);
    }

    /**
     * 当前登录用户可查看的卷内文件idList
     * @param userCode 用户id
     */
    public List<String> CdfIdList(String userCode){
        return documentPermissionDao.CdfIdList(userCode);
    }
}
