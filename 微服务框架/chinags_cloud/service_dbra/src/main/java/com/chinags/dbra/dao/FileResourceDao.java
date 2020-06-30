package com.chinags.dbra.dao;

import com.chinags.dbra.entity.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-8-29
 **/
public interface FileResourceDao extends JpaRepository<FileResource, String>, JpaSpecificationExecutor<FileResource> {

    @Query(value = "SELECT * FROM t_meta_file_resource WHERE ID=:id", nativeQuery = true)
    FileResource selectById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_meta_file_resource SET APPLY_NUM=APPLY_NUM+1 WHERE ID=:id", nativeQuery = true)
    void updateApplyNumById(String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_meta_file_resource SET DOWN_NUM=DOWN_NUM+1 WHERE ID=:id", nativeQuery = true)
    void updateDownNumById(String id);

    @Query(value = "SELECT DISTINCT TYPE FROM T_META_FILE_RESOURCE", nativeQuery = true)
    List<String> findAllType();

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_META_FILE_RESOURCE SET status=:status WHERE ID=:id", nativeQuery = true)
    void updateStatusById(String id, String status);

}
