package com.chinags.archives.dao;

import com.chinags.archives.entity.ClerkDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author XieWenqing
 * @date 2019/11/20 上午 10:29
 */
public interface ClerkDocumentDao extends JpaRepository<ClerkDocument, String>, JpaSpecificationExecutor<ClerkDocument> {
    /**
     * 根据id查询工程档案案卷著录记录
     * @param id  工程档案案卷著录id
     */
    @Query(value = "SELECT * FROM t_coa_clerk_document WHERE ID=:id", nativeQuery = true)
    ClerkDocument selectById(String id);

    /**
     * 删除工程档案案卷著录
     */
    @Override
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM t_coa_clerk_document WHERE id=:id", nativeQuery = true)
    void deleteById(String id);
}
