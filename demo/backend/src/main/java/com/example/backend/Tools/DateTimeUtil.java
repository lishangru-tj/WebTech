package com.example.backend.Tools;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: 杨严
 * @Date: 2022/11/29/7:10 PM
 * @Description: DateTime相关的工具方法
 */
public class DateTimeUtil {

    public static Long convertTimeToLong(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(time, formatter);
        return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * @description: 获取某一个月的天数
     * @author: hym
     * @date: 2022/11/25 10:35
     * @param:
     * @param: year
     * @param: month
     * @return:
     * @return: int
     **/
    public static int getDayNumOfMonth(int year, int month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(Integer.toString(year) + "-" + Integer.toString(month) + "-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Timestamp getCurrentTimestamp(){
        return new Timestamp(new Date().getTime());
    }


    public static Date addDate(Date date, int day)   {
        long time = date.getTime(); // 得到指定日期的毫秒数
        long addSec=day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += addSec; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

}
