package com.example.backend.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */
public class DateUtil {
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
        return new Timestamp(System.currentTimeMillis()/1000);
    }
}
