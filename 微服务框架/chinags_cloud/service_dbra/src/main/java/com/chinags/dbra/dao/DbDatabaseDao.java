package com.chinags.dbra.dao;

import com.chinags.dbra.entity.DbDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据库表接口
 * @author Mark_Wang
 * @date 2019/6/26
 * @time 16:19
 **/
public interface DbDatabaseDao extends JpaRepository<DbDatabase, String>, JpaSpecificationExecutor<DbDatabase> {
    @Query(value = "select * from t_meta_database where id=:id", nativeQuery = true)
    DbDatabase getDbDatabaseById(@Param("id") String id);

    @Query(value = "select DISTINCT a.* from t_meta_database a inner join t_meta_table b on a.id = b.db_id", nativeQuery = true)
    List<DbDatabase> getDbDatabasesInTable();

    @Query(value = "select DISTINCT a.* from t_meta_database a inner join t_meta_column b on a.id = b.db_id", nativeQuery = true)
    List<DbDatabase> getDbDatabasesInColumn();

    @Query(value = "select * from t_meta_database where address=:address and db_user=:dbUser and db_pwd=:dbPwd", nativeQuery = true)
    List<DbDatabase> getDbDatabaseByAddressAndDbUserAndDbPwd(String address, String dbUser, String dbPwd);

    @Query(value = "select * from t_meta_database where name=:name", nativeQuery = true)
    List<DbDatabase> getDbDataBaseByName(String name);
}
