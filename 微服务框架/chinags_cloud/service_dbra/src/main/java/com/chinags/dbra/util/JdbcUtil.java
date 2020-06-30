package com.chinags.dbra.util;

import com.alibaba.fastjson.JSON;
import com.chinags.common.utils.StringUtils;
import com.chinags.dbra.entity.BasicData;
import com.chinags.dbra.entity.DbDatabase;
import java.sql.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/6/26
 * @time 8:33
 **/
public class JdbcUtil {
    /**
     * 驱动
     */
    private static String driver = "oracle.jdbc.driver.OracleDriver";

    /**
     * 连接字符串
     */
    private static String url = "jdbc:oracle:thin:@//localhost:1521/orcl";

    /**
     * 获得连接对象
     * @param dbDatabase 数据库实体
     * @return
     */
    public static synchronized Connection getConn(DbDatabase dbDatabase) {
        Connection conn = null;
        try {
            Class.forName(driver);
            if (StringUtils.isNotEmpty(dbDatabase.getServiceName())) {
                conn = DriverManager.getConnection(url.replaceAll("localhost", dbDatabase.getAddress()).replaceAll("orcl", dbDatabase.getServiceName()), dbDatabase.getDbUser(),
                        dbDatabase.getDbPwd());
            } else {
                conn = DriverManager.getConnection(url.replaceAll("localhost", dbDatabase.getAddress()), dbDatabase.getDbUser(),
                        dbDatabase.getDbPwd());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return conn;
        }
    }

    /**
     * 获得连接对象
     * @param dbDatabase 数据库实体
     * @return
     */
    public static synchronized Connection getConn(DbDatabase dbDatabase, String dbName) {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url.replaceAll("localhost", dbDatabase.getAddress()).replaceAll("orcl", dbName), dbDatabase.getDbUser(),
                    dbDatabase.getDbPwd());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return conn;
        }
    }

    //关闭连接
    public static void close(Connection conn){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DbDatabase dbDatabase = new DbDatabase();
        dbDatabase.setAddress("192.168.1.2");
        dbDatabase.setDbUser("meta");
        dbDatabase.setDbPwd("meta");
        PreparedStatement dbPstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = "select * from T_META_SHARE";
            dbPstmt = conn.prepareStatement(sql);
            ResultSet rs = dbPstmt.executeQuery();
            while (rs.next()) {
                HashMap hashMap = new HashMap();
                hashMap.put("id", Class.forName("java.lang.String"));
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
                System.out.println(rs.getString(5));
                System.out.println(rs.getString(6));

                List<BasicData> basicDatas = JSON.parseArray(rs.getString("columns"), BasicData.class);
                for (BasicData basicData : basicDatas) {
                    System.out.println(basicData.getName() + "====" + basicData.getValue() + "==" + basicData.getType());
                }

                // 动态创建对象和赋值
                /*DynamicBean bean = new DynamicBean(hashMap);
                bean.setValue("id", rs.getString(1));
                System.out.println(JSON.toJSONString(bean));*/
            }
            rs.close();
            dbPstmt.close();
            JdbcUtil.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
