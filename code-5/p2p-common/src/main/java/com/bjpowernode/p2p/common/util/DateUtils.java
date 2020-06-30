package com.bjpowernode.p2p.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ClassName：DateUtils
 * Package:com.bjpowernode.p2p.common.util
 * Description:
 *
 * @date:2019/3/1 14:00
 * @author:guoxin@bjpowernode.com
 */
public class DateUtils {
    public static Date getDateByAdds(Date date, Integer count) {
        //创建日期处理对象
        Calendar calendar = Calendar.getInstance();
        //设置日期值
        calendar.setTime(date);
        //添加天数
        calendar.add(Calendar.DATE,count);
        return calendar.getTime();
    }
    public static Date getDateByAddMonths(Date date, Integer count) {
        //创建日期处理对象
        Calendar calendar = Calendar.getInstance();
        //设置日期值
        calendar.setTime(date);
        //添加天数
        calendar.add(Calendar.MONTH,count);
        return calendar.getTime();
    }
    public static Date getDateByAdd(Date date,Integer count){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,count);
        return calendar.getTime();
    }


    public static String getTimeStamp() {

        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

    }
}
