package com.chinags.archives.dao;

import com.chinags.archives.entity.DocumentFileup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author XieWenqing
 * @date 2019/11/4 上午 11:00
 */
public interface DocumentFileupDao extends JpaRepository<DocumentFileup, String>, JpaSpecificationExecutor<DocumentFileup> {
    /**
     * 根据档案卷内文件id查询附件list
     * @param id  卷内文件id
     * @return
     */
    @Query(value = "select * from T_COA_DOCUMENT_FILEUP up JOIN T_COA_CLERK_DOCUMENT_FILE cdf on cdf.id = up.cdfid where cdf.id=:id", nativeQuery = true)
    List<DocumentFileup> findDocumentFileupList(@Param("id") String id);
}
