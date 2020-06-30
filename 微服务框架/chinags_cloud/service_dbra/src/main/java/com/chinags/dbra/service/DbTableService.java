package com.chinags.dbra.service;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.dbra.dao.DbDatabaseDao;
import com.chinags.dbra.dao.DbTableDao;
import com.chinags.dbra.entity.DbDatabase;
import com.chinags.dbra.entity.DbTable;
import com.chinags.dbra.util.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据表实现
 * @author Mark_Wang
 * @date 2019/6/28
 * @time 9:15
 **/
@Service
public class DbTableService {
    @Autowired
    private DbTableDao dbTableDao;

    @Autowired
    private DbDatabaseDao dbDatabaseDao;

    /**
     * 分页查询所有数据表管理配置的表
     * @return
     */
    public PageResult<DbTable> findAllByPage(BaseEntity entity) {
        PageRequest pageable;
        Sort sort = new Sort(Sort.Direction.DESC,"updateDate");
        pageable = PageRequest.of(entity.getPageNo(), entity.getPageSize(), sort);
        Page<DbTable> page = dbTableDao.findAll(pageable);
        return PageResult.getPageResult(page);
    }

    /**
     * 查询所有数据表配置
     * @return
     */
    public List<DbTable> findAll() {
        return dbTableDao.findAll();
    }


    /**
     * 根据表管理配置表的id查询表具体信息
     * @param id 表管理配置的id
     * @return
     */
    public DbTable findById(String id) {
        return dbTableDao.findDbTableById(id);
    }

    /**
     * 根据数据库id查询该数据库下配置的表
     * @param dbId
     * @return
     */
    public List<DbTable> findByDbId(String dbId) {
        return dbTableDao.findDbTablesByDbId(dbId);
    }

    /**
     * 分页根据数据库id查询该数据库下配置的表
     * @param dbId
     * @return
     */
    public List<DbTable> findByDbIdAndPage(String dbId, int num, int size) {
        return dbTableDao.findDbTablesByDbIdAndPage(dbId, num, size);
    }

    /**
     * 根据数据库id查询数据库下有多少个数据表配置
     * @param dbId
     * @return
     */
    public Long getCountByDbId(String dbId) {
        return dbTableDao.getCountByDbId(dbId);
    }
    /**
     * 保存数据表配置信息
     * @param dbTable
     */
    public DbTable save(DbTable dbTable) {
        if (StringUtils.isEmpty(dbTable.getCreateDate())) {
            dbTable.setCreateDate(new Date());
        }
        dbTable.setUpdateDate(new Date());
        return dbTableDao.save(dbTable);
    }

    /**
     * 根据数据库id和数据表名查询数据表下所有的字段
     * @param dbId
     * @param tableName
     * @return
     */
    public List<String> findColumnsNameByDbIdAndTableName(String dbId, String tableName) {
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(dbId);
        PreparedStatement pstmt;
        List<String> columns = new ArrayList<>();
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = "select * from user_col_comments where Table_Name='" + tableName.toUpperCase() + "' order by column_name";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("column_name");
                columns.add(name);
            }
            rs.close();
            pstmt.close();
            JdbcUtil.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return columns;
        }
    }

    /**
     * 根据数据表id删除数据表配置信息
     * @param id
     */
    public void deleteById(String id) {
        dbTableDao.deleteById(id);
    }

    /**
     * 根据数据库id删除数据表配置信息
     * @param dbId
     */
    public void deleteByDbId(String dbId) {
        dbTableDao.deleteByDbId(dbId);
    }

    /**
     * 获取配置字段的数据表信息
     * @return
     */
    public List<DbTable> getDbTablesInColumn() {
        return dbTableDao.getDbTablesInColumn();
    }

    /**
     * 根据数据库id和表明查询数据表配置
     * @param dbId
     * @param name
     * @return
     */
    public List<DbTable> getDbTableByDbIdAndName(String dbId, String name) {
        return dbTableDao.getDbTableByDbIdAndName(dbId, name);
    }

}
