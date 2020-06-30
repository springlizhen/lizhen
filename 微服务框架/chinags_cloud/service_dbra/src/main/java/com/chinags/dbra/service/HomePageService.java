package com.chinags.dbra.service;

import com.alibaba.fastjson.JSON;
import com.chinags.dbra.entity.DayData;
import com.chinags.dbra.entity.DbDatabase;
import com.chinags.dbra.entity.ZdhData;
import com.chinags.dbra.util.JdbcUtil;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 首页service
 * @Author : Mark_Wang
 * @Date : 2020/4/1
 **/
@Service
public class HomePageService {
    private static final String HOST = "10.0.2.23";
    private static final String DB_NAME = "pdbmoni";
    private static final String USER = "moni";
    private static final String PWD = "moni";
    private static final String[] TB_NAMES = {"DATA_REG", "MSG_POINT_VALUE"};
    private static final String[] TB_TIMES = {"RECEIVE_DATE", "MSG_POINT_VALUE"};
    private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat HH = new SimpleDateFormat("HH");

    /**
     * 获取当日数据条目数
     * @return
     */
    public List<String> daySum() {
        String day = FMT.format(new Date());
        DbDatabase dbDatabase = getDbdataBase();
        List<String> sums = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt;
        try {
            int sum = 0;
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            // 自动化
            String sql = "SELECT COUNT(*) FROM DATA_REG where to_char(RECEIVE_DATE, 'yyyyMMdd')='" + day + "'";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sum += rs.getInt(1);
            }
            // 水质
            sql = "SELECT COUNT(*) FROM MSG_POINT_VALUE where qn like '" + day + "%'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                sum += rs.getInt(1);
            }
            // TODO 水文数据未知那个表未统计

            String strSum = String.valueOf(sum);
            for (int c = 0; c < strSum.length(); c++) {
                sums.add(strSum.substring(c, c + 1));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return sums;
        }
    }

    /**
     * 获取分类周数据量统计
     * @return
     */
    public List<Integer> dataType() {
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, - 6);
        Date d = c.getTime();
        String day = FMT.format(d);
        DbDatabase dbDatabase = getDbdataBase();
        List<Integer> sums = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt;
        try {
            int sum = 0;
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            // TODO 水文数据未知那个表未统计
            sums.add(sum);

            // 水质
            sum = 0;
            String sql = "SELECT COUNT(*) FROM MSG_POINT_VALUE where qn > '" + day + "000000000'";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sum += rs.getInt(1);
            }
            sums.add(sum);
            // 自动化
            sum = 0;
            sql = "SELECT COUNT(*) FROM DATA_REG where to_char(RECEIVE_DATE, 'yyyyMMdd')>='" + day + "'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                sum = rs.getInt(1);
            }
            sums.add(sum);
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return sums;
        }
    }

    /**
     * 一周中每天自动化数据量
     * @return
     */
    public List<ZdhData> zdhsjZtData() {
        List<ZdhData> zdhDataList = new ArrayList<>();
        DbDatabase dbDatabase = getDbdataBase();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            for (int i = 6; i >= 0; i--) {
                int sum = 0;
                Calendar c = Calendar.getInstance();
                //过去七天
                c.setTime(new Date());
                c.add(Calendar.DATE, - i);
                Date d = c.getTime();
                String day = FMT.format(d);
                String sql = "SELECT COUNT(*) FROM DATA_REG where to_char(RECEIVE_DATE, 'yyyyMMdd')='" + day + "'";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    sum = rs.getInt(1);
                }
                ZdhData zdhData = new ZdhData();
                String week = dateToWeek(day);
                zdhData.setName(week);
                zdhData.setValue(sum);
                zdhDataList.add(zdhData);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return zdhDataList;
        }
    }

    /**
     * 自动化当日小时数据统计
     * @return
     */
    public List<Integer> dayZDH() {
        String hour = HH.format(new Date());
        int hr = Integer.parseInt(hour);
        DbDatabase dbDatabase = getDbdataBase();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> result = new ArrayList<>();
        try {
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            for (int i = 0; i <= hr; i++) {
                int sum = 0;
                String day = FMT.format(new Date());
                if (i < 10) {
                    day = day + "0" + i;
                } else {
                    day = day + i;
                }
                String sql = "SELECT COUNT(*) FROM DATA_REG where to_char(RECEIVE_DATE, 'yyyyMMddHH24')='" + day + "'";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    sum = rs.getInt(1);
                }
                result.add(sum);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return result;
        }
    }

    /**
     * 水质当日小时数据统计
     * @return
     */
    public List<Integer> daySZ() {
        String hour = HH.format(new Date());
        int hr = Integer.parseInt(hour);
        DbDatabase dbDatabase = getDbdataBase();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> result = new ArrayList<>();
        try {
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            for (int i = 0; i <= hr; i++) {
                int sum = 0;
                String day = FMT.format(new Date());
                if (i < 10) {
                    day = day + "0" + i;
                } else {
                    day = day + i;
                }
                String sql = "SELECT COUNT(*) FROM MSG_POINT_VALUE where qn like '" + day + "%'";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    sum = rs.getInt(1);
                }
                result.add(sum);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return result;
        }
    }

    /**
     * 水文当日小时数据统计
     * @return
     */
    public List<Integer> daySW() {
        String hour = HH.format(new Date());
        int hr = Integer.parseInt(hour);
        /*DbDatabase dbDatabase = getDbdataBase();
        Connection conn = null;
        PreparedStatement pstmt;*/
        List<Integer> result = new ArrayList<>();
        try {
            //conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            for (int i = 0; i <= hr; i++) {
                int sum = 0;
                /*String day = FMT.format(new Date());
                if (i < 10) {
                    day = day + "0" + i;
                } else {
                    day = day + i;
                }
                String sql = "SELECT COUNT(*) FROM DATA_REG where to_char(RECEIVE_DATE, 'yyyyMMddHH')='" + day + "'";
                pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    sum = rs.getInt(1);
                }*/
                result.add(sum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //JdbcUtil.close(conn);
            return result;
        }
    }

    /**
     * 每日水情交换分析图数据
     * @return
     */
    public List<Integer> daySq() {
        List<String> sqTypes = Arrays.asList("%池水位","泵组状态","闸%水位","闸门开度","阀%压力","阀门开度","瞬时流量","累计流量");
        String day = FMT.format(new Date());
        DbDatabase dbDatabase = getDbdataBase();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> result = new ArrayList<>();
        try {
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            for (String type : sqTypes) {
                int sum = 0;
                String sql = "SELECT COUNT(*) FROM DATA_REG where  to_char(RECEIVE_DATE, 'yyyyMMdd')='" + day + "' and att_type like '" + type + "'";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    sum = rs.getInt(1);
                }
                result.add(sum);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return result;
        }
    }

    /**
     * 当日数据交换条目
     * @return
     */
    // TODO 省厅的水文数据没有加入
    public List<DayData> getDayData() {
        DbDatabase dbDatabase = getDbdataBase();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DayData> result = new ArrayList<>();
        try {
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            int sum = 1;
            String sql = "SELECT time, type, value FROM ((SELECT TO_CHAR(RECEIVE_DATE, 'yyyy-MM-dd') AS time, '自动化' AS type, CONCAT('数据值：', REVISE_VALUE) AS value FROM \"DATA_REG\" WHERE TO_CHAR(RECEIVE_DATE, 'yyyyMMdd')=TO_CHAR(SYSDATE, 'yyyyMMdd') AND COMM_STATUS='0' AND ALARM_STATUS='0' AND ROWNUM < 101) UNION ALL (SELECT TO_CHAR(SYSDATE, 'yyyy-MM-dd') as time, '水质' as type, CONCAT('数据值：', value) AS value FROM MSG_POINT_VALUE WHERE SUBSTR(qn, 0, 8) = TO_CHAR(SYSDATE, 'yyyyMMdd') AND ROWNUM < 101))";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DayData dayData = new DayData();
                dayData.setId(sum);
                dayData.setTime(rs.getString("time"));
                dayData.setType(rs.getString("type"));
                dayData.setValue(rs.getString("value"));
                result.add(dayData);
                sum++;
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return result;
        }
    }

    /**
     * 当日预警数据交换条目
     * @return
     */
    // TODO 省厅的水文数据没有加入
    public List<DayData> getDayDataError() {
        DbDatabase dbDatabase = getDbdataBase();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DayData> result = new ArrayList<>();
        try {
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            int sum = 1;
            String sql = "SELECT time, type, value FROM ((SELECT TO_CHAR(RECEIVE_DATE, 'yyyy-MM-dd') AS time, '自动化' AS type, CONCAT('预警：', ALARM_INFO) AS value FROM DATA_REG WHERE TO_CHAR(RECEIVE_DATE, 'yyyyMMdd')=TO_CHAR(SYSDATE, 'yyyyMMdd') AND ALARM_STATUS='1' AND ROWNUM < 51) UNION ALL (SELECT TO_CHAR(RECEIVE_DATE, 'yyyy-MM-dd') AS time, '自动化' AS type, '预警：连接预警' AS value FROM DATA_REG WHERE TO_CHAR(RECEIVE_DATE, 'yyyyMMdd')=TO_CHAR(SYSDATE, 'yyyyMMdd') AND COMM_STATUS='1' AND ROWNUM < 51) UNION ALL (SELECT TO_CHAR(TIME, 'yyyy-MM-dd') AS time, '自动化' AS type, '预警：未匹配基础数据' AS value FROM DATA_REG_BASIC_ERROR WHERE TO_CHAR(TIME, 'yyyyMMdd')=TO_CHAR(SYSDATE, 'yyyyMMdd') AND ROWNUM < 81) UNION ALL (SELECT TO_CHAR(SYSDATE, 'yyyy-MM-dd') as time, '水质' as type, '预警：水文信息数据错误' as value FROM MSG_POINT_VALUE_ERROR WHERE SUBSTR(qn, 0, 8) = TO_CHAR(SYSDATE, 'yyyyMMdd') AND ROWNUM < 101))";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DayData dayData = new DayData();
                dayData.setId(sum);
                dayData.setTime(rs.getString("time"));
                dayData.setType(rs.getString("type"));
                dayData.setValue(rs.getString("value"));
                result.add(dayData);
                sum++;
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return result;
        }
    }

    /**
     * 处理异常数据top5
     * @return
     */
    public List<DayData> getTopError() {
        DbDatabase dbDatabase = getDbdataBase();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DayData> result = new ArrayList<>();
        try {
            conn = JdbcUtil.getConn(dbDatabase, DB_NAME);
            String sql = "SELECT '自动化匹配基础数据异常' AS value, COUNT(*) AS id, '0' AS type FROM DATA_REG_BASIC_ERROR UNION ALL SELECT '水质数据报文解析异常' AS value,COUNT(*) AS id, '1' AS type  FROM MSG_POINT_LOG WHERE STATUS<>1";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DayData dayData = new DayData();
                dayData.setId(rs.getInt("id"));
                dayData.setType(rs.getString("type"));
                dayData.setValue(rs.getString("value"));
                result.add(dayData);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn);
            return result;
        }
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public String dateToWeek(String datetime) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = FMT.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    public DbDatabase getDbdataBase() {
        DbDatabase dbDatabase = new DbDatabase();
        dbDatabase.setAddress(HOST);
        dbDatabase.setDbUser(USER);
        dbDatabase.setDbPwd(PWD);
        return dbDatabase;
    }

    public static void main(String[] args) {
        HomePageService homePageService = new HomePageService();
        System.out.println(JSON.toJSONString(homePageService.daySZ()));
    }

}
