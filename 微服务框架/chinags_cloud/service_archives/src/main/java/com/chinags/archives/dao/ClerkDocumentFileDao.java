package com.chinags.archives.dao;

import com.chinags.archives.entity.ClerkDocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author XieWenqing
 * @date 2019/9/29 下午 2:35
 */
public interface ClerkDocumentFileDao extends JpaRepository<ClerkDocumentFile, String>, JpaSpecificationExecutor<ClerkDocumentFile> {
    /**
     * 查询公共档案
     */
    @Query(value = "select * from t_coa_clerk_document_file where is_public='是'", nativeQuery = true)
    List<ClerkDocumentFile> findClerkDocumentFilesByIsPublic();

    /**
     * 根据id查询卷内文件
     */
    @Query(value = "select * from t_coa_clerk_document_file where id=:id", nativeQuery = true)
    ClerkDocumentFile selectById(String id);

    /**
     * 根据工程档案案卷著录id查询卷内文件list
     * @param id
     * @return
     */
    @Query(value = "select * from t_coa_clerk_document_file where clerk_document_id=:id", nativeQuery = true)
    List<ClerkDocumentFile> findClerkDocumentFileByCkId(String id);

    /**
     * 删除工程档案案卷著录
     */
    @Override
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM t_coa_clerk_document_file WHERE id=:id", nativeQuery = true)
    void deleteById(String id);

    /**
     * 根据卷内文件id和案卷题名查找记录，案卷标题可为空
     */
    @Query(value = "select cd.title cdtitle, cdf.* from t_coa_clerk_document_file cdf join T_COA_CLERK_DOCUMENT cd on cdf.CLERK_DOCUMENT_ID=cd.id where cdf.id=:id and cdf.title like concat('%', concat(:title, '%'))", nativeQuery = true)
    List<Map<String, Object>> findClerkDocumentFile(String id, String title);
}
