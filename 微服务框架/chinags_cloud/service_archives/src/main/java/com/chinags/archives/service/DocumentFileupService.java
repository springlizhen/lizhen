package com.chinags.archives.service;

import com.chinags.archives.dao.DocumentFileupDao;
import com.chinags.archives.entity.DocumentFileup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XieWenqing
 * @date 2019/11/4 上午 11:02
 */
@Service
public class DocumentFileupService {
    @Autowired
    private DocumentFileupDao documentFileupDao;

    /**
     * 根据档案卷内文件id查询附件list
     * @param id  卷内文件id
     * @return
     */
    public List<DocumentFileup> findDocumentFileupList(String id){
        return documentFileupDao.findDocumentFileupList(id);
    }
}
