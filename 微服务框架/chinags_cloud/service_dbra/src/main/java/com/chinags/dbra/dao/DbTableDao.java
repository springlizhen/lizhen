package com.chinags.dbra.dao;

import com.chinags.dbra.entity.DbTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 数据表表接口
 * @author Mark_Wang
 * @date 2019/6/28
 * @time 8:55
 **/
public interface DbTableDao extends JpaRepository<DbTable, String>, JpaSpecificationExecutor<DbTable> {

    @Query(value = "select * from t_meta_table where id=:id", nativeQuery = true)
    DbTable findDbTableById(@Param("id") String id);

    @Query(value = "SELECT A.* FROM (SELECT * FROM t_meta_table WHERE DB_ID=:dbId) A WHERE ROWNUM BETWEEN :num AND :size", nativeQuery = true)
    List<DbTable> findDbTablesByDbIdAndPage(String dbId, int num, int size);

    @Query(value = "SELECT * FROM t_meta_table WHERE DB_ID=:dbId", nativeQuery = true)
    List<DbTable> findDbTablesByDbId(String dbId);

    @Query(value = "select count(*) from t_meta_table where db_id=:dbId", nativeQuery = true)
    Long getCountByDbId(String dbId);

    @Modifying
    @Transactional
    @Query(value = "delete from t_meta_table where db_id=:dbId", nativeQuery = true)
    void deleteByDbId(String dbId);

    @Query(value = "select DISTINCT a.* from t_meta_table a inner join t_meta_column b on a.id = b.tb_id", nativeQuery = true)
    List<DbTable> getDbTablesInColumn();

    @Query(value = "SELECT * FROM t_meta_table WHERE DB_ID=:dbId AND NAME=:name", nativeQuery = true)
    List<DbTable> getDbTableByDbIdAndName(String dbId, String name);
}
