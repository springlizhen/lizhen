package com.chinags.dbra.dao;

import com.chinags.dbra.entity.DbColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 数据字段表接口
 * @author Mark_Wang
 * @date 2019/6/28
 * @time 10:59
 **/
public interface DbColumnDao extends JpaRepository<DbColumn, String>, JpaSpecificationExecutor<DbColumn> {

    @Query(value = "select * from t_meta_column where id=:id", nativeQuery = true)
    DbColumn findDbColumnById(String id);

    @Query(value = "select * from t_meta_column where tb_id=:tbId", nativeQuery = true)
    List<DbColumn> findDbColumnsByTbId(String tbId);

    @Query(value = "SELECT A.* FROM (SELECT * FROM t_meta_column WHERE tb_id=:tbId) A WHERE ROWNUM BETWEEN :num AND :size", nativeQuery = true)
    List<DbColumn> findDbColumnsByTbIdAndPage(String tbId, int num, int size);

    @Modifying
    @Transactional
    @Query(value = "delete from t_meta_column where tb_id=:tbId", nativeQuery = true)
    void deleteByTbId(String tbId);

    @Query(value = "select * from t_meta_column where db_id=:dbId", nativeQuery = true)
    List<DbColumn> findDbColumnsByDbId(String dbId);

    @Query(value = "SELECT A.* FROM (SELECT * FROM t_meta_column WHERE db_id=:dbId) A WHERE ROWNUM BETWEEN :num AND :size", nativeQuery = true)
    List<DbColumn> findDbColumnsByDbIdAndPage(String dbId, int num, int size);

    @Query(value = "SELECT count(*) FROM t_meta_column WHERE db_id=:dbId", nativeQuery = true)
    Long findCountByDbId(String dbId);

    @Query(value = "SELECT count(*) FROM t_meta_column WHERE tb_id=:tbId", nativeQuery = true)
    Long findCountByTbId(String tbId);

    @Query(value = "SELECT * FROM t_meta_column WHERE TB_ID=:tbId AND DB_ID=:dbId AND NAME=:name", nativeQuery = true)
    List<DbColumn> getDbColumnByTbIdAndDbIdAndName(String tbId, String dbId, String name);

}
