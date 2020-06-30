package com.chinags.dbra.service;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.dbra.dao.DbDatabaseDao;
import com.chinags.dbra.entity.DbDatabase;
import com.chinags.dbra.entity.TableComments;
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
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库操作实现
 * @author Mark_Wang
 * @date 2019/6/26
 * @time 16:29
 **/
@Service
public class DbDatabaseService {
    @Autowired
    private DbDatabaseDao dbDatabaseDao;

    /**
     * 分页查询数据库列表
     * @return 数据库list集合
     */
    public PageResult<DbDatabase> selectAll(BaseEntity entity) {
        PageRequest pageable;
        Sort sort = new Sort(Sort.Direction.DESC,"updateDate");
        pageable = PageRequest.of(entity.getPageNo(), entity.getPageSize(), sort);
        Page<DbDatabase> page = dbDatabaseDao.findAll(pageable);
        return PageResult.getPageResult(page);
    }

    /**
     * 查询所有数据库配置信息
     * @return
     */
    public List<DbDatabase> findAll() {
        return dbDatabaseDao.findAll();
    }

    /**
     * 根据数据库id查询数据库信息
     * @param id 数据库id
     * @return 数据库信息
     */
    public DbDatabase getDbDatabaseById(String id) {
        return dbDatabaseDao.getDbDatabaseById(id);
    }

    /**
     * 数据库管理新建数据库
     * @param dbDatabase 数据库实体
     * @return
     */
    public boolean saveDbDatabase(DbDatabase dbDatabase) {
        boolean result = false;
        // 数据库信息验证
        Connection conn = JdbcUtil.getConn(dbDatabase);
        if (conn == null) {
            return result;
        } else {
            try {
                conn.close();
                if (StringUtils.isEmpty(dbDatabase.getCreateDate())) {
                    dbDatabase.setCreateDate(new Date());
                }
                dbDatabase.setUpdateDate(new Date());
                dbDatabaseDao.save(dbDatabase);
                result = true;
                JdbcUtil.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                return result;
            }
        }
    }

    /**
     * 根据所选择的数据库查询数据库下所有的表名
     * @param id 数据库id
     * @return
     */
    public List<String> findAllTableNameById(String id) {
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(id);
        PreparedStatement dbPstmt;
        List<String> tableNames = new ArrayList<>();
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = "select * from user_tab_comments";
            dbPstmt = conn.prepareStatement(sql);
            ResultSet rs = dbPstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("table_name");
                tableNames.add(name);
            }
            rs.close();
            dbPstmt.close();
            JdbcUtil.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return tableNames;
        }
    }

    /**
     * 根据id删除数据库配置信息
     * @param id
     */
    public void deleteById(String id) {
        dbDatabaseDao.deleteById(id);
    }

    /**
     * 根据数据库id查询数据表的名称和注释
     * @param id
     * @return
     */
    public List<TableComments> findAllTableNameAndCommentsById(String id) {
        List<TableComments> tableMap = new ArrayList<>();
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(id);
        PreparedStatement dbPstmt;
        List<String> tableNames = new ArrayList<>();
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = "select * from user_tab_comments";
            dbPstmt = conn.prepareStatement(sql);
            ResultSet rs = dbPstmt.executeQuery();
            while (rs.next()) {
                TableComments tableComments = new TableComments();
                String name = rs.getString("table_name");
                String comments = rs.getString("comments");
                tableComments.setTbName(name);
                if (StringUtils.isEmpty(comments)) {
                    tableComments.setComments(name);
                } else {
                    tableComments.setComments(comments);
                }
                tableMap.add(tableComments);
            }
            rs.close();
            dbPstmt.close();
            JdbcUtil.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return tableMap;
        }

    }

    /**
     * 获取配置数据表的数据库信息
     * @return
     */
    public List<DbDatabase> getDbDatabasesInTable() {
        return dbDatabaseDao.getDbDatabasesInTable();
    }

    /**
     * 获取配置字段表的数据库信息
     * @return
     */
    public List<DbDatabase> getDbDatabasesInColumn() {
        return dbDatabaseDao.getDbDatabasesInColumn();
    }

    /**
     * 根据数据库地址，用户名，用户密码查询数据库（用于验证数据库唯一性）
     * @param address
     * @param dbUser
     * @param dbPwd
     * @return
     */
    public List<DbDatabase> getDbDatabaseByAddressAndDbUserAndDbPwd(String address, String dbUser, String dbPwd) {
        return dbDatabaseDao.getDbDatabaseByAddressAndDbUserAndDbPwd(address, dbUser, dbPwd);
    }

    /**
     * 根据数据库名查询数据库（用于验证数据库唯一性）
     * @param name
     * @return
     */
    public List<DbDatabase> getDbDataBaseByName(String name) {
        return dbDatabaseDao.getDbDataBaseByName(name);
    }


}
