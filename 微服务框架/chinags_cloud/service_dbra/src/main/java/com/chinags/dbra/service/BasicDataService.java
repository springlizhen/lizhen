package com.chinags.dbra.service;

import com.alibaba.fastjson.JSON;
import com.chinags.common.lang.StringUtils;
import com.chinags.dbra.dao.DbDatabaseDao;
import com.chinags.dbra.dao.DbTableDao;
import com.chinags.dbra.entity.*;
import com.chinags.dbra.util.FunctionUtil;
import com.chinags.dbra.util.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/7/1
 **/
@Service
public class BasicDataService {

    @Autowired
    private DbDatabaseDao dbDatabaseDao;

    @Autowired
    private DbTableDao dbTableDao;

    /**
     * 根据数据库id和表名称查询字段属性和备注信息
     * @param dbId
     * @param tableName
     * @return
     */
    public List<BasicData> findColumnsCommentsByDbIdAndTableName(String dbId, String tableName) {
        List<BasicData> columns = new ArrayList<>();
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(dbId);
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = "SELECT A.COLUMN_NAME name, B.DATA_TYPE type, A.COMMENTS comments, B.NULLABLE able, E.CONSTRAINT_TYPE key " +
                    "FROM (select COLUMN_NAME, COMMENTS from user_col_comments where Table_Name= '" + tableName.toUpperCase() + "') A " +
                    "INNER JOIN (SELECT COLUMN_NAME, DATA_TYPE, NULLABLE FROM user_tab_columns WHERE table_name='" +
                    tableName.toUpperCase() + "') B on A.column_name = B.column_name " +
                    "LEFT JOIN (SELECT D.COLUMN_NAME AS COLUMN_NAME, C.CONSTRAINT_TYPE AS CONSTRAINT_TYPE FROM user_constraints C " +
                    "INNER JOIN user_cons_columns D ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME " +
                    "WHERE C.TABLE_NAME='" + tableName.toUpperCase() + "' AND C.CONSTRAINT_TYPE = 'P') E ON A.COLUMN_NAME = E.COLUMN_NAME";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                BasicData model = new BasicData();
                model.setName(rs.getString("name"));
                model.setType(rs.getString("type"));
                if (StringUtils.isEmpty(rs.getString("comments"))) {
                    model.setComments(rs.getString("name"));
                } else {
                    model.setComments(rs.getString("comments"));
                }
                model.setAble(rs.getString("able"));
                model.setKey(rs.getString("key"));
                columns.add(model);
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
     * 根据数据库id，表名和属性信息生成sql语句插入数据
     * @param dbId 数据库id
     * @param tableName 表名
     * @param basicDataList 属性信息
     * @return
     */
    public boolean saveDataToTable(String dbId, String tableName, List<BasicData> basicDataList) {
        boolean result = false;
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(dbId);
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = FunctionUtil.generatInsertSql(tableName, basicDataList);
            if (StringUtils.isNotEmpty(sql)) {
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
                pstmt.close();
                JdbcUtil.close(conn);
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 动态修改
     * @param dbId
     * @param tableName
     * @param id
     * @param basicDataList
     * @return
     */
    public boolean updateDataToTable(String dbId, String tableName, String id, List<BasicData> basicDataList) {
        boolean result = false;
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(dbId);
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = FunctionUtil.generatUpdateSql(tableName, id, basicDataList);
            if (StringUtils.isNotEmpty(sql)) {
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
                pstmt.close();
                JdbcUtil.close(conn);
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 根据数据库id，表名和id生成sql语句删除数据
     * @param dbId 数据库id
     * @param tableName 表名
     * @param id
     * @return
     */
    public boolean deleteDataToTable(String dbId, String tableName, String id) {
        boolean result = false;
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(dbId);
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = FunctionUtil.generatDeleteSql(tableName, id);
            if (StringUtils.isNotEmpty(sql)) {
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
                pstmt.close();
                JdbcUtil.close(conn);
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 生成动态实体分页查询
     * @param dbId 数据库id
     * @param tableName 数据表名
     * @param num 开始数值
     * @param size 结束数值
     * @return
     */
    public List<Object> find(String dbId, String tableName, int num, int size) {
        List<BasicData> basicDataList = this.findColumnsCommentsByDbIdAndTableName(dbId, tableName);
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(dbId);
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = FunctionUtil.generatSelectSql(basicDataList, tableName);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, size);
            pstmt.setInt(2, num);
            ResultSet rs = pstmt.executeQuery();
            List<Object> resultList = FunctionUtil.generateResultToJson(rs, basicDataList);
            rs.close();
            pstmt.close();
            JdbcUtil.close(conn);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 本地数据服务API获取数据接口
     * @param resourceApi
     * @param condition
     * @param size
     * @param num
     * @return
     */
    public List<Object> findByResourceApi(ResourceApi resourceApi, String condition, int size, int num) {
        List<BasicData> basicDataList = JSON.parseArray(resourceApi.getColumns(), BasicData.class);
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(resourceApi.getDbId());
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = resourceApi.getSql();
            if (StringUtils.isNotEmpty(condition)) {
                pstmt = conn.prepareStatement(sql.replaceAll("1=1", condition));
            } else {
                pstmt = conn.prepareStatement(sql);
            }
            pstmt.setInt(1, size);
            pstmt.setInt(2, num);
            ResultSet rs = pstmt.executeQuery();
            List<Object> resultList = FunctionUtil.generateResultToJson(rs, basicDataList);
            rs.close();
            pstmt.close();
            JdbcUtil.close(conn);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 本地数据服务API获取所有数据接口
     * @param resourceApi
     * @return
     */
    public List<Object> findAllByResourceApi(ResourceApi resourceApi) {
        List<BasicData> basicDataList = JSON.parseArray(resourceApi.getColumns(), BasicData.class);
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(resourceApi.getDbId());
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = resourceApi.getAllSql();
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List<Object> resultList = FunctionUtil.generateResultToJson(rs, basicDataList);
            rs.close();
            pstmt.close();
            JdbcUtil.close(conn);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据数据库id，数据表名和id查询数据
     * @param dbId
     * @param tableName
     * @param id
     * @return
     */
    public List<BasicData> findById(String dbId, String tableName, String id) {
        List<BasicData> basicDataList = this.findColumnsCommentsByDbIdAndTableName(dbId, tableName);
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(dbId);
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = FunctionUtil.generatGetSqlByTableNameAndId(tableName, id);
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List<BasicData> resultList = FunctionUtil.generateResultToBasicList(rs, basicDataList);
            rs.close();
            pstmt.close();
            JdbcUtil.close(conn);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取表中一共有多少数据
     * @param dbId
     * @param tableName
     * @return
     */
    public Long findCount(String dbId, String tableName) {
        DbDatabase dbDatabase = dbDatabaseDao.getDbDatabaseById(dbId);
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = "SELECT count(id) AS nums FROM " + tableName;
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            long nums = 0;
            while (rs.next()) {
                nums = rs.getLong(1);
            }
            rs.close();
            pstmt.close();
            JdbcUtil.close(conn);
            return nums;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
