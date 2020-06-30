package com.chinags.dbra.dao;

import com.chinags.dbra.entity.OtherResource;
import com.chinags.dbra.entity.Resource;
import com.chinags.dbra.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/17
 **/
public interface ResourceDao extends JpaRepository<Resource, String>, JpaSpecificationExecutor<Resource> {

    @Query(value = "select * from t_meta_resource where id=:id", nativeQuery = true)
    Resource selectById(String id);

    @Query(value = "select * from t_meta_resource where tb_id=:tbId", nativeQuery = true)
    List<Resource> selectByTbId(String tbId);

    @Query(value = "select * from t_meta_resource where db_id=:dbId", nativeQuery = true)
    List<Resource> selectByDbId(String dbId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_meta_resource SET CAT_NUM=CAT_NUM+1 WHERE ID=:id", nativeQuery = true)
    void updateCatNumById(String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_meta_resource SET DOWN_NUM=DOWN_NUM+1 WHERE ID=:id", nativeQuery = true)
    void updateDownNumById(String id);

    @Query(value = "SELECT COUNT(*) FROM T_META_RESOURCE", nativeQuery = true)
    int countResource();

    @Query(value = "SELECT SUM(DOWN_NUM) FROM T_META_RESOURCE", nativeQuery = true)
    int sumDownNum();

    @Query(value = "SELECT distinct(TB_ID) FROM T_META_RESOURCE", nativeQuery = true)
    List<String> findAllTables();

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_meta_resource SET status=:status WHERE ID=:id", nativeQuery = true)
    void updateStatusById(String id, String status);

}
