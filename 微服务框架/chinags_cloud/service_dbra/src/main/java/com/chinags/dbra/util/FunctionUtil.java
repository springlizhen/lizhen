package com.chinags.dbra.util;

import com.chinags.common.lang.StringUtils;
import com.chinags.dbra.entity.BasicData;
import org.springframework.util.CollectionUtils;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeString.toLowerCase;

/**
 * @author Mark_Wang
 * @date 2019/7/1
 **/
public class FunctionUtil {

    /**
     * 动态生成数据插入语句
     * @param tableName 数据表名
     * @param basicDataList 数据表属性值
     * @return
     */
    public static String generatInsertSql(String tableName, List<BasicData> basicDataList) {
        String sql = "insert into " + tableName + " (";
        String columns = "";
        String values = "";
        int num = 0;
        for (BasicData basicData : basicDataList) {
            // 判断数值是否为空，如果为空则不参与insert语句生成
            if (StringUtils.isEmpty(basicData.getValue())) {
                num++;
                continue;
            } else if ((num != 0) && (num < basicDataList.size() - 1)) {
                columns +=", ";
                values += ", ";
            }

            columns += basicData.getName();
            values += formatType(basicData.getType(), basicData.getValue());

            num++;
        }
        if (StringUtils.isNotEmpty(columns) && StringUtils.isNotEmpty(values)) {
            sql = sql + columns + ") values (" + values + ")";
            return sql;
        }
        return null;
    }

    /**
     * 根据数据表名和id生成删除sql
     * @param tableName 数据表名称
     * @param id 数据表属性值
     * @return
     */
    public static String generatDeleteSql(String tableName, String id) {
        String sql = "delete from " + tableName + " where id='";
        if (StringUtils.isNotEmpty(id)) {
            sql = sql + id + "'";
            return sql;
        }
        return null;
    }

    /**
     * 根据数据表名和id生成查询sql
     * @param tableName
     * @param id
     * @return
     */
    public static String generatGetSqlByTableNameAndId(String tableName, String id) {
        if (StringUtils.isNotEmpty(tableName) && StringUtils.isNotEmpty(id)) {
            String sql = "select * from " + tableName + " where id='" + id + "'";
            return sql;
        }
        return null;
    }

    /**
     * 根据属性类型转换value值在sql中的类型
     * @param type 属性类型
     * @param value 属性值
     * @return
     */
    public static String formatType(String type, String value) {
        String result = null;
        switch (type) {
            case "NUMBER":
                result = value;
                break;
            case "DATE":
                result =  "to_date('" + value + "', 'yyyy-mm-dd hh24:mi:ss')";
                break;
            default:
                result =  "'" + value + "'";
                break;
        }
        return result;
    }


    /**
     * 动态生成数据分页语句
     * @param cols list basicData
     * @param tableName
     * @return
     */
    public static String generatSelectSql(List<BasicData> cols, String tableName) {
        String sql = "SELECT * FROM (SELECT a.*, ROWNUM rn FROM (SELECT ";
        int num = 0;
        for (BasicData col : cols) {
            sql += col.getName().toUpperCase();
            if (num < cols.size() - 1) {
                sql += ", ";
            }
            num++;
        }
        sql = sql + " FROM " + tableName + " WHERE 1=1 ) a WHERE ROWNUM <= ?) WHERE rn > ?";
        return sql;
    }

    /**
     * 动态生成数据统计语句
     * @param tableName
     * @return
     */
    public static String generatSelectCountSql(String tableName) {
        String sql = "SELECT COUNT(*) AS COUNT FROM " + tableName;
        return sql;
    }

    /**
     * 动态生成查询所有数据的语句
     * @param cols list basicData
     * @param tableName
     * @return
     */
    public static String generatSql(List<BasicData> cols, String tableName) {
        String sql = "SELECT ";
        int num = 0;
        for (BasicData col : cols) {
            sql += col.getName().toUpperCase();
            if (num < cols.size() - 1) {
                sql += ", ";
            }
            num++;
        }
        sql = sql + " FROM " + tableName;
        return sql;
    }

    /**
     * 根据列属性集合和查询结果动态生成实体集合
     * @param resultSet 查询结果
     * @param basicDataList 属性集合
     * @return
     */
    public static List<Object> generateResultToJson(ResultSet resultSet, List<BasicData> basicDataList) {
        if (CollectionUtils.isEmpty(basicDataList)) {
            return null;
        }
        try {
            HashMap hashMap = new HashMap();
            for (BasicData basicData : basicDataList) {
                hashMap.put(toLowerCase(basicData.getName()), Class.forName("java.lang.String"));
            }
            List<Object> resultList = new ArrayList<>();
            while (resultSet.next()) {
                DynamicBean bean = new DynamicBean(hashMap);
                for (BasicData basicData : basicDataList) {
                    bean.setValue(toLowerCase(basicData.getName()), resultSet.getString(basicData.getName()));
                }
                resultList.add(bean.getObject());
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 属性集合值
     * @param resultSet 查询结果
     * @param basicDataList 属性集合
     * @return
     */
    public static List<BasicData> generateResultToBasicList(ResultSet resultSet, List<BasicData> basicDataList) {
        if (CollectionUtils.isEmpty(basicDataList)) {
            return null;
        }
        try {
            List<BasicData> basicDatas = new ArrayList<>();
            while (resultSet.next()) {
                for (BasicData basicData : basicDataList) {
                    basicData.setValue(resultSet.getString(basicData.getName()));
                    basicDatas.add(basicData);
                }
            }
            return basicDatas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成update语句
     * @param tbName
     * @param id
     * @param basicDataList
     * @return
     */
    public static String generatUpdateSql(String tbName, String id, List<BasicData> basicDataList) {
        String sql = "update " + tbName + " set ";
        String values = "";
        int num = 0;
        for (BasicData basicData : basicDataList) {
            if (!basicData.getName().equalsIgnoreCase("id")) {
                values = values + basicData.getName() + "=" + formatType(basicData.getType(), basicData.getValue());
                if ((num != 0) && (num < basicDataList.size() - 1)) {
                    values += ", ";
                }
            }
            num++;
        }
        if (StringUtils.isNotEmpty(values)) {
            sql = sql + values + " where id = '" + id + "'";
            return sql;
        }
        return null;
    }

}
