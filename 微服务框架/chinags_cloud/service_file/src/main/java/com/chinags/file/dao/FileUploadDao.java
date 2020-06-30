package com.chinags.file.dao;

import com.chinags.file.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/26
 **/
public interface FileUploadDao extends JpaRepository<FileUpload, String>, JpaSpecificationExecutor<FileUpload> {

    @Query(value = "SELECT * FROM T_PUB_FILE WHERE PID=:pid ORDER BY CREATE_DATE DESC", nativeQuery = true)
    List<FileUpload> getByPid(@Param("pid") String pid);

    @Query(value = "SELECT * FROM T_PUB_FILE WHERE ID=:id", nativeQuery = true)
    FileUpload getById(String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_PUB_FILE SET PID=:pid WHERE PID='' AND CREATE_BY=:createBy", nativeQuery = true)
    void updatePid(String pid, String createBy);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_PUB_FILE SET PID=:pid WHERE PID=:opid AND CREATE_BY=:createBy", nativeQuery = true)
    void updatePidByPid(String pid, String opid, String createBy);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM T_PUB_FILE WHERE PID=:pid AND CREATE_BY=:createBy", nativeQuery = true)
    void deleteByCreateBy(String pid, String createBy);

    @Query(value = "SELECT * FROM T_PUB_FILE WHERE PID=:pid AND CREATE_BY=:createBy  ORDER BY CREATE_DATE DESC", nativeQuery = true)
    List<FileUpload> findByCreateBy(String pid, String createBy);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_PUB_FILE SET page_size=:pageSize WHERE id=:id", nativeQuery = true)
    void updatePageSizeById(String id, String pageSize);
}
